package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.DialogFragment
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
import tkhshyt.annicta.event.SeasonSpinnerSelectedEvent
import tkhshyt.annicta.event.UpdateStatusEvent
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.layout.recycler.Util
import tkhshyt.annicta.pref.UserInfo
import java.util.*
import javax.inject.Inject
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import tkhshyt.annicta.event.SeasonSelectedEvent
import tkhshyt.annicta.extension.worksWithStatus

class WorkListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    enum class Sort {
        ID, WATCHER_COUNT
    }

    @Inject
    lateinit var annict: AnnictService

    @Inject
    lateinit var message: MessageCreator

    private val workItemAdapter = ItemAdapter<WorkItem>()

    private var season: Season? = Season.season(Calendar.getInstance())
    private var sort: Sort = Sort.WATCHER_COUNT

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

    private fun loadMoreListener(): EndlessScrollListener {
        return object : EndlessScrollListener() {

            override fun onLoadMore(currentPage: Int) {
                val accessToken = UserInfo.accessToken
                if (accessToken != null) {
                    val request = when(sort) {
                        Sort.ID -> {
                            annict.worksWithStatus(
                                    access_token = accessToken,
                                    sort_id = "desc",
                                    page = currentPage
                            )
                        }
                        Sort.WATCHER_COUNT -> {
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

        val listener = loadMoreListener()
        listener.onLoadMore(1)

        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as? DaggerApplication)?.getComponent()?.inject(this)

        retainInstance = true
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
                .subscribe({ response ->
                    val index = workItemAdapter.adapterItems.indexOfFirst { it.work.id == event.workId }
                    val item = workItemAdapter.getAdapterItem(index)
                    if (item != null) {
                        workItemAdapter.set(index, WorkItem(item.work.copy(status = Status(event.status)), activity))
                    }
                    message.create()
                        .context(context)
                        .message(resources.getString(R.string.update_status))
                        .build().show()
                }, { throwable ->
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
        sort = Sort.WATCHER_COUNT
        when(event.season) {
            SeasonSpinner.NEXT_SEASON -> {
                season = season?.next()
            }
            SeasonSpinner.CURRENT_SEASON -> {
            }
            SeasonSpinner.PREV_SEASON -> {
                season = season?.prev()
            }
            SeasonSpinner.FAVORITE -> {
                season = null
            }
            SeasonSpinner.NEW -> {
                sort = Sort.ID
                season = null
            }
            SeasonSpinner.SELECT -> {
                val dialog = SeasonSelectDialogFragment()
                val bundle = Bundle()
                bundle.putSerializable("season", season)
                dialog.arguments = bundle
                dialog.show(fragmentManager, "select_season_dialog")
            }
        }

        if(event.season != SeasonSpinner.SELECT) {
            onRefresh()
        }
    }

    @Subscribe
    fun onSeasonSlectedEvent(event: SeasonSelectedEvent) {
        season = event.season
        onRefresh()
    }

    class SeasonSelectDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
            val season = arguments?.getSerializable("season") as Season
            val items = (season.year+1 downTo 1960).flatMap { y ->
                val seasons = Season.Type.values().map {
                    Season(y, it)
                }
                seasons.asIterable()
            }
            val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, items)

            val builder = context?.let {
                AlertDialog.Builder(it)
                    .setAdapter(adapter, { _, which ->
                        EventBus.getDefault().post(SeasonSelectedEvent(adapter.getItem(which)))
                    })
            }
            return builder!!.create()
        }
    }
}
