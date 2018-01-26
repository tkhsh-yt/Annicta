package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
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
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Status
import tkhshyt.annict.json.Work
import tkhshyt.annicta.event.UpdateStatusEvent
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.pref.UserInfo
import trikita.log.Log
import javax.inject.Inject

class WorkFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var annict: AnnictService

    @Inject
    lateinit var message: MessageCreator

    private val workItemAdapter = ItemAdapter<WorkItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fastAdapter = FastAdapter.with<WorkItem, ItemAdapter<WorkItem>>(workItemAdapter)
        recyclerView.adapter = fastAdapter
        recyclerView.setHasFixedSize(true)

        swipeRefreshView.setOnRefreshListener(this)
        swipeRefreshView.setColorSchemeResources(R.color.greenPrimary, R.color.redPrimary, R.color.indigoPrimary, R.color.yellowPrimary)

        onRefresh()
    }

    private fun loadMoreListener(llm: LinearLayoutManager): EndlessScrollListener {
        return object : EndlessScrollListener(llm) {
            override fun onLoadMore(currentPage: Int) {
                val accessToken = UserInfo.accessToken
                if (accessToken != null) {
                    annict.works(
                            access_token = accessToken,
                            sort_watchers_count = "desc",
                            filter_season = "2018-winter",
                            page = currentPage
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally {
                            loading = false
                            swipeRefreshView.isRefreshing = false
                        }
                        .subscribe({ response ->
                            val works = response.body()
                            val count = workItemAdapter.adapterItemCount
                            workItemAdapter.add(works.works.map { WorkItem(it, context) })

                            // これ別のやり方がある気がする
                            annict.followingWorks(
                                    access_token = accessToken,
                                    sort_watchers_count = "desc",
                                    filter_ids = works.works.map { it.id }.joinToString(",")
                            ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ res ->
                                    val statusWorks = res.body().works

                                    for (i in (0 until statusWorks.size)) {
                                        for (j in (count until workItemAdapter.adapterItemCount)) {
                                            if (statusWorks[i].id == workItemAdapter.getAdapterItem(j).work.id) {
                                                workItemAdapter.set(j, WorkItem(statusWorks[i], context))
                                                break
                                            }
                                        }
                                    }
                                }, { throwable ->
                                    message.create()
                                        .context(context)
                                        .message("ステータス情報の取得に失敗しました")
                                        .build().show()
                                })

                            nextPage = works.next_page ?: 0
                        }, { throwable: Throwable? ->
                            message.create()
                                .context(context)
                                .message("読み込みに失敗しました")
                                .build().show()
                        })
                }
            }
        }
    }

    override fun onRefresh() {
        workItemAdapter.clear()

        val glm = GridLayoutManager(context, 2)
        recyclerView.layoutManager = glm

        val listener = loadMoreListener(glm)
        listener.onLoadMore(1)

        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as? DaggerApplication)?.getComponent()?.inject(this)

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onUpdateStatus(event: UpdateStatusEvent) {
        val accessToken = UserInfo.accessToken
        if (accessToken != null) {
            annict.updateState(
                    access_token = accessToken,
                    work_id = event.workId,
                    kind = event.status
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val index = workItemAdapter.adapterItems.indexOfFirst { it.work.id == event.workId }
                    val item = workItemAdapter.getAdapterItem(index)
                    if (item != null) {
                        workItemAdapter.set(index, WorkItem(item.work.copy(status = Status(event.status)), context))
                    }
                    message.create()
                        .context(context)
                        .message("ステータスを更新しました")
                        .build().show()
                }, { throwable ->
                    message.create()
                        .context(context)
                        .message("ステータスの更新に失敗しました")
                        .build().show()
                })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("item_count", workItemAdapter.adapterItemCount)
        workItemAdapter.adapterItems.forEachIndexed({ i, item ->
            outState.putSerializable("item_$i", item.work)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            (0 until savedInstanceState.getInt("item_count")).forEach {
                val item = WorkItem(savedInstanceState.getSerializable("item_$it") as Work, context)
                workItemAdapter.add(it, item)
            }
        }
    }
}