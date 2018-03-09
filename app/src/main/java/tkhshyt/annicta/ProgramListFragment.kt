package tkhshyt.annicta

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annict.AnnictService
import tkhshyt.annicta.event.RecordedEvent
import tkhshyt.annicta.extension.defaultOn
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.pref.UserConfig
import tkhshyt.annicta.pref.UserInfo
import tkhshyt.annicta.util.AnnictUtil
import java.util.*
import javax.inject.Inject


class ProgramListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var annict: AnnictService

    @Inject
    lateinit var message: MessageCreator

    private val programItemAdapter = ItemAdapter<ProgramItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fastAdapter = FastAdapter.with<ProgramItem, ItemAdapter<ProgramItem>>(programItemAdapter)
        recyclerView.adapter = fastAdapter
        recyclerView.setHasFixedSize(true)

        swipeRefreshView.setOnRefreshListener(this)
        swipeRefreshView.setColorSchemeResources(R.color.green_500, R.color.red_500, R.color.indigo_500, R.color.yellow_500)
        swipeRefreshView.isRefreshing = true

        onRefresh()
    }

    override fun onRefresh() {
        programItemAdapter.clear()

        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm

        val listener = loadMoreListener(llm)
        listener.onLoadMore(1)

        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(listener)
    }

    private fun loadMoreListener(llm: LinearLayoutManager): EndlessScrollListener {
        return object : EndlessScrollListener(llm) {

            override fun onLoadMore(currentPage: Int) {
                val accessToken = UserInfo.accessToken
                if (accessToken != null) {
                    val startedAtLt = Calendar.getInstance()
                    startedAtLt.add(Calendar.DATE, UserConfig.startedAtLt)
                    annict.programs(
                            access_token = accessToken,
                            sort_started_at = "desc",
                            filter_started_at_lt = AnnictUtil.apiDateFormat.format(startedAtLt.time),
                            page = currentPage
                    ).defaultOn()
                        .doFinally {
                            loading = false
                            swipeRefreshView?.isRefreshing = false
                        }
                        .subscribe({ response ->
                            val programs = response.body()
                            programItemAdapter.add(programs.resources().map { ProgramItem(it, activity as Activity) })
                            nextPage = programs.next_page ?: 0
                        }, {
                            message.create()
                                .context(context)
                                .message("取得に失敗しました")
                                .build().show()
                        })
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as? MyApplication)?.getComponent()?.inject(this)

        EventBus.getDefault().register(this)

        retainInstance = true
    }

    override fun onDestroy() {
        super.onDestroy()
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