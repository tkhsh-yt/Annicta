package tkhshyt.annicta

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
import tkhshyt.annict.AnnictService
import tkhshyt.annicta.extension.followingActivitiesWithStatus
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.pref.UserInfo
import javax.inject.Inject


class ActivityListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var annict: AnnictService

    @Inject
    lateinit var message: MessageCreator

    private val activityItemAdapter = ItemAdapter<ActivityItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fastAdapter = FastAdapter.with<ActivityItem, ItemAdapter<ActivityItem>>(activityItemAdapter)
        recyclerView.adapter = fastAdapter
        recyclerView.setHasFixedSize(true)

        swipeRefreshView.setOnRefreshListener(this)
        swipeRefreshView.setColorSchemeResources(R.color.green_500, R.color.red_500, R.color.indigo_500, R.color.yellow_500)
        swipeRefreshView.isRefreshing = true

        onRefresh()
    }

    override fun onRefresh() {
        activityItemAdapter.clear()

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
                    annict.followingActivitiesWithStatus(
                            access_token = accessToken,
                            sort_id = "desc",
                            page = currentPage
                    )({
                        activityItemAdapter.add(it.resources().map { ActivityItem(it, activity) })

                        nextPage = it.next_page ?: 0
                    }, {
                        loading = false
                        swipeRefreshView?.isRefreshing = false
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

        retainInstance = true
    }
}