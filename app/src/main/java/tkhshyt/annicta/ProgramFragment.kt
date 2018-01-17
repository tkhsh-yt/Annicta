package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annict.AnnictClient
import tkhshyt.annicta.event.RecordedEvent
import tkhshyt.annicta.pref.UserInfo
import tkhshyt.annicta.utils.Utils

class ProgramFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val programItemAdapter = ItemAdapter<ProgramItem>()
    private var loading = false
    private var ended = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fastAdapter = FastAdapter.with<ProgramItem, ItemAdapter<ProgramItem>>(programItemAdapter)
        recyclerView.adapter = fastAdapter

        val llm = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = llm

        recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(currentPage: Int) {
                if(!loading && !ended) {
                    val accessToken = UserInfo.accessToken
                    if(accessToken != null) {
                        if(programItemAdapter.adapterItemCount != 0) {
                            val last = programItemAdapter.getAdapterItem(programItemAdapter.adapterItemCount-1)
                            val startedAt = Utils.apiDateFormat.format(last.program.started_at)

                            AnnictClient.service.programs(
                                    access_token = accessToken,
                                    sort_started_at = "desc",
                                    filter_started_at_lt = startedAt
                            ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ programs ->
                                    if(programs.programs.size != 1) {
                                        (1 until programs.programs.size).forEach {
                                            programItemAdapter.add(programItemAdapter.adapterItemCount, ProgramItem(programs.programs[it], context))
                                        }
                                        loading = false
                                    } else {
                                        ended = true
                                    }
                                }, { throwable: Throwable? ->
                                    Toast.makeText(context, "読み込みに失敗しました", Toast.LENGTH_LONG).show()
                                })
                        }
                    }
                }
            }
        })

        swipeRefreshView.setOnRefreshListener(this)
        swipeRefreshView.setColorSchemeResources(R.color.greenPrimary, R.color.redPrimary, R.color.indigoPrimary, R.color.yellowPrimary)

        swipeRefreshView.isRefreshing = true
        updatePrograms()
    }

    override fun onRefresh() {
        updatePrograms()
    }

    private fun updatePrograms() {
        val accessToken = UserInfo.accessToken
        if (accessToken != null) {
            val head =
                    if(programItemAdapter.adapterItemCount != 0) {
                        programItemAdapter.getAdapterItem(0)
                    } else { null }
            val startedAt = head?.let { Utils.apiDateFormat.format(it.program.started_at) }

            AnnictClient.service.programs(
                    access_token = accessToken,
                    sort_started_at = "desc",
                    filter_started_at_gt = startedAt
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ programs ->
                    programItemAdapter.add(programs.programs.map { ProgramItem(it, context) })
                    swipeRefreshView.isRefreshing = false
                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRecordedEvent(event: RecordedEvent) {
        val index = (0 until programItemAdapter.adapterItemCount).firstOrNull {
            programItemAdapter.getAdapterItem(it).program.episode.id == event.record.episode?.id
        }
        if (index != null) {
            programItemAdapter.remove(index)
        }
    }
}