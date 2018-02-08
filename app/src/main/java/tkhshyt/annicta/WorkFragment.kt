package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Status
import tkhshyt.annicta.event.UpdateStatusEvent
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.layout.recycler.Util
import tkhshyt.annicta.pref.UserInfo
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

        swipeRefreshView.isRefreshing = true

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
                        .subscribe({ response ->
                            val works = response.body()

                            // これ別のやり方がある気がする
                            annict.followingWorks(
                                    access_token = accessToken,
                                    sort_watchers_count = "desc",
                                    filter_ids = works.works.map { it.id }.joinToString(",")
                            ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doFinally {
                                    swipeRefreshView.isRefreshing = false
                                    loading = false
                                }
                                .subscribe({ response ->
                                    val statusWorks = response.body().works
                                    val merge = works.works.map { work ->
                                        statusWorks.find { it.id == work.id } ?: work
                                    }

                                    workItemAdapter.add(merge.map { WorkItem(it, context) })
                                }, { throwable ->
                                    message.create()
                                        .context(context)
                                        .message("ステータス情報の取得に失敗しました")
                                        .build().show()
                                })

                            nextPage = works.next_page ?: -1
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

        val columns = Util.calculateNoOfColumns(context)
        val glm = GridLayoutManager(context, columns)
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

        retainInstance = true
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
}