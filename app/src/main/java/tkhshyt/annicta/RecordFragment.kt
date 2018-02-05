package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chibatching.kotpref.Kotpref
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Record
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.pref.UserInfo
import javax.inject.Inject

class RecordFragment : Fragment() {

    @Inject
    lateinit var annict: AnnictService

    @Inject
    lateinit var message: MessageCreator

    private val recordItemAdapter = ItemAdapter<RecordItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fastAdapter = FastAdapter.with<RecordItem, ItemAdapter<RecordItem>>(recordItemAdapter)
        recyclerView.adapter = fastAdapter
        recyclerView.setHasFixedSize(true)

        swipeRefreshView.isEnabled = false

        swipeRefreshView.isRefreshing = true
        onRefresh()
    }

    private fun onRefresh() {
        recordItemAdapter.clear()

        val llm = LinearLayoutManager(context)
        llm.isAutoMeasureEnabled = true
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
                    val episodeId =
                            if (arguments?.containsKey("episode_id") == true) {
                                arguments?.getLong("episode_id")
                            } else {
                                null
                            }
                    annict.records(
                            access_token = accessToken,
                            filter_episode_id = episodeId,
                            filter_has_record_comment = true,
                            sort_id = "desc",
                            page = currentPage
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally {
                            loading = false
                            swipeRefreshView?.isRefreshing = false
                        }
                        .subscribe({ response ->
                            val records = response.body()
                            recordItemAdapter.add(records.records.map { RecordItem(it, activity) })
                            nextPage = records.next_page ?: 0
                        }, { throwable ->
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

        (activity?.application as? DaggerApplication)?.getComponent()?.inject(this)

//        EventBus.getDefault().register(this)
    }

//    override fun onStop() {
//        super.onStop()
//        EventBus.getDefault().unregister(this)
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("item_count", recordItemAdapter.adapterItemCount)
        recordItemAdapter.adapterItems.forEachIndexed({ i, item ->
            outState.putSerializable("item_$i", item.record)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            (0 until savedInstanceState.getInt("item_count")).forEach {
                val item = RecordItem(savedInstanceState.getSerializable("item_$it") as Record, activity)
                recordItemAdapter.add(it, item)
            }
        }
    }
}