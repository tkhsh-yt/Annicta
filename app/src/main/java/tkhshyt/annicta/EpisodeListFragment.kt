package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.fragment_list.*
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Work
import tkhshyt.annicta.extension.defaultOn
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.pref.UserInfo
import tkhshyt.annicta.utils.notNullIf
import javax.inject.Inject


class EpisodeListFragment : Fragment() {

    @Inject
    lateinit var annict: AnnictService

    private val workInfoItemAdapter = ItemAdapter<WorkInfoItem>()
    private val episodeItemAdapter = ItemAdapter<EpisodeItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapters = listOf(workInfoItemAdapter, episodeItemAdapter)
        val fastAdapter = FastAdapter.with<AbstractItem<*, *>, ItemAdapter<*>>(adapters)
        recyclerView.adapter = fastAdapter
        recyclerView.setHasFixedSize(true)

        swipeRefreshView.setColorSchemeResources(R.color.green_500, R.color.red_500, R.color.indigo_500, R.color.yellow_500)
        swipeRefreshView.isRefreshing = true
        swipeRefreshView.isEnabled = false

        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm

        arguments.notNullIf({
            it.containsKey("work")
        }, {
            val work = it.getSerializable("work") as Work
            workInfoItemAdapter.add(WorkInfoItem(work, activity))
        })

        val listener = loadMoreListener(llm)
        recyclerView.addOnScrollListener(listener)
    }

    private fun loadMoreListener(llm: LinearLayoutManager): EndlessScrollListener {
        return object : EndlessScrollListener(llm) {

            override fun onLoadMore(currentPage: Int) {
                val accessToken = UserInfo.accessToken
                if (accessToken != null) {
                    val workId = arguments.notNullIf({
                        it.containsKey("work_id")
                    }, {
                        it.getLong("work_id")
                    })

                    annict.episodes(
                            access_token = accessToken,
                            filter_work_id = workId.toString(),
                            sort_sort_number = "asc",
                            page = currentPage
                    ).defaultOn()
                        .doFinally {
                            swipeRefreshView?.isRefreshing = false
                        }
                        .subscribe({
                            val episodes = it.body()
                            episodeItemAdapter.add(episodes.resources().map { EpisodeItem(it) })
                            nextPage = episodes.next_page ?: 0
                        }, {
                        })
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DI
        (activity?.application as? DaggerApplication)?.getComponent()?.inject(this)

        retainInstance = true
    }
}