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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annict.AnnictClient
import tkhshyt.annict.json.Program
import tkhshyt.annicta.event.RecordedEvent
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.pref.UserInfo

class ProgramFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

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
        swipeRefreshView.setColorSchemeResources(R.color.greenPrimary, R.color.redPrimary, R.color.indigoPrimary, R.color.yellowPrimary)

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
                    AnnictClient.service.programs(
                            access_token = accessToken,
                            sort_started_at = "desc",
                            page = currentPage
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally {
                            loading = false
                            swipeRefreshView.isRefreshing = false
                        }
                        .subscribe({ programs ->
                            programItemAdapter.add(programs.programs.map { ProgramItem(it, context) })
                            nextPage = programs.next_page ?: 0
                        }, { throwable ->
                            Toast.makeText(context, "取得に失敗しました", Toast.LENGTH_LONG).show()
                        })
                }
            }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("item_count", programItemAdapter.adapterItemCount)
        programItemAdapter.adapterItems.forEachIndexed({ i, item ->
            outState.putSerializable("item_$i", item.program)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            (0 until savedInstanceState.getInt("item_count")).forEach {
                val item = ProgramItem(savedInstanceState.getSerializable("item_$it") as Program, context)
                programItemAdapter.add(it, item)
            }
        }
    }
}