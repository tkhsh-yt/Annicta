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
import tkhshyt.annict.Season
import tkhshyt.annict.json.Status
import tkhshyt.annicta.SeasonSpinner.*
import tkhshyt.annicta.WorkListFragment.SortWork.ID
import tkhshyt.annicta.WorkListFragment.SortWork.WATCHER_COUNT
import tkhshyt.annicta.event.SeasonSelectedEvent
import tkhshyt.annicta.event.SeasonSpinnerSelectedEvent
import tkhshyt.annicta.event.UpdateStatusEvent
import tkhshyt.annicta.extension.worksWithStatus
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.layout.recycler.Util
import tkhshyt.annicta.pref.UserInfo
import java.util.*
import javax.inject.Inject

class WorkListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    enum class SortWork {
        ID, WATCHER_COUNT
    }

    @Inject
    lateinit var annict: AnnictService

    @Inject
    lateinit var message: MessageCreator

    private val workItemAdapter = ItemAdapter<WorkItem>()

    private var season: Season? = Season.season(Calendar.getInstance())
    private var sort: SortWork = WATCHER_COUNT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as? MyApplication)?.getComponent()?.inject(this)

        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fastAdapter = FastAdapter.with<WorkItem, ItemAdapter<WorkItem>>(workItemAdapter)
        recyclerView.adapter = fastAdapter
        recyclerView.setHasFixedSize(true)

        swipeRefreshView.setOnRefreshListener(this)
        swipeRefreshView.setColorSchemeResources(R.color.green_500, R.color.red_500, R.color.indigo_500, R.color.yellow_500)

        onRefresh()
    }

    private fun loadMoreListener(llm: LinearLayoutManager): EndlessScrollListener {
        return object : EndlessScrollListener(llm) {

            override fun onLoadMore(currentPage: Int) {
                val accessToken = UserInfo.accessToken
                if (accessToken != null) {
                    val request = when (sort) {
                        ID -> {
                            annict.worksWithStatus(
                                    access_token = accessToken,
                                    sort_id = "desc",
                                    page = currentPage
                            )
                        }
                        WATCHER_COUNT -> {
                            annict.worksWithStatus(
                                    access_token = accessToken,
                                    sort_watchers_count = "desc",
                                    filter_season = season?.param(),
                                    page = currentPage
                            )
                        }
                    }
                    request({
                        workItemAdapter.add(it.resources().map { WorkItem(it, activity) })
                        nextPage = it.next_page ?: -1
                    }, {
                        swipeRefreshView?.isRefreshing = false
                        loading = false
                    }, {
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

        swipeRefreshView.isRefreshing = true

        val columns = Util.calculateNoOfColumns(context)
        val glm = GridLayoutManager(context, columns)
        recyclerView.layoutManager = glm

        val listener = loadMoreListener(glm)
        listener.onLoadMore(1)

        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(listener)
    }

    override fun onStart() {
        super.onStart()
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
                .subscribe({
                    val index = workItemAdapter.adapterItems.indexOfFirst { it.work.id == event.workId }
                    val item = workItemAdapter.getAdapterItem(index)
                    if (item != null) {
                        workItemAdapter.set(index, WorkItem(item.work.copy(status = Status(event.status)), activity))
                    }
                    message.create()
                        .context(context)
                        .message(resources.getString(R.string.update_status))
                        .build().show()
                }, {
                    message.create()
                        .context(context)
                        .message(resources.getString(R.string.fail_to_update_status))
                        .build().show()
                })
        }
    }

    @Subscribe
    fun onSeasonSpinnerSelectedEvent(event: SeasonSpinnerSelectedEvent) {
        season = Season.season(Calendar.getInstance())
        sort = WATCHER_COUNT
        when (event.season) {
            NEXT_SEASON -> {
                season = season?.next()
            }
            CURRENT_SEASON -> {
            }
            PREV_SEASON -> {
                season = season?.prev()
            }
            FAVORITE -> {
                season = null
            }
            NEW -> {
                sort = ID
                season = null
            }
            SELECT -> {
                val dialog = SeasonSelectDialogFragment()
                val bundle = Bundle()
                bundle.putSerializable("season", season)
                dialog.arguments = bundle
                dialog.show(fragmentManager, "select_season_dialog")
            }
        }

        if (event.season != SELECT) {
            onRefresh()
        }
    }

    @Subscribe
    fun onSeasonSlectedEvent(event: SeasonSelectedEvent) {
        season = event.season
        onRefresh()
    }
}
