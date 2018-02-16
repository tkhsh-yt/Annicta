package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.Work
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.pref.UserInfo
import trikita.log.Log
import javax.inject.Inject
import com.mikepenz.fastadapter.IItem



class EpisodeListFragment : Fragment() {

    @Inject
    lateinit var annict: AnnictService

    private val episodeItemAdapter = ItemAdapter<EpisodeItem>()

    private val workInfoItemAdapter = ItemAdapter<WorkInfoItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapters = listOf(workInfoItemAdapter, episodeItemAdapter)
        val fastAdapter = FastAdapter.with<AbstractItem<*, *>, ItemAdapter<*>>(adapters)
        recyclerView.adapter = fastAdapter
        recyclerView.setHasFixedSize(true)

        swipeRefreshView.setColorSchemeResources(R.color.greenPrimary, R.color.redPrimary, R.color.indigoPrimary, R.color.yellowPrimary)
        swipeRefreshView.isRefreshing = true
        swipeRefreshView.isEnabled = false

        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm

        if(arguments?.containsKey("work") == true) {
            val work = arguments?.getSerializable("work") as Work
            workInfoItemAdapter.add(WorkInfoItem(work, activity))
        }

        val listener = loadMoreListener(llm)
        recyclerView.addOnScrollListener(listener)
        // listener.onLoadMore(1)
    }

    private fun loadMoreListener(llm: LinearLayoutManager): EndlessScrollListener {
        return object : EndlessScrollListener(llm) {

            override fun onLoadMore(currentPage: Int) {
                val accessToken = UserInfo.accessToken
                if (accessToken != null) {
                    val workId =
                            if(arguments?.containsKey("work_id") == true) {
                                arguments?.getLong("work_id")
                            } else {
                                null
                            }
                    annict.episodes(
                            access_token = accessToken,
                            filter_work_id = workId.toString(),
                            sort_sort_number = "asc",
                            page = currentPage
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally {
                            swipeRefreshView?.isRefreshing = false
                        }
                        .subscribe({
                            val episodes = it.body()
                            episodeItemAdapter.add(episodes.episodes.map { EpisodeItem(it, activity) })
                            nextPage = episodes.next_page ?: 0
                        }, {
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