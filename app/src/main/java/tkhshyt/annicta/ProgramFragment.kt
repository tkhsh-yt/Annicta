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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import tkhshyt.annict.AnnictClient
import tkhshyt.annicta.pref.UserInfo
import tkhshyt.annicta.utils.Utils

class ProgramFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    val programItemAdapter = ItemAdapter<ProgramItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fastAdapter = FastAdapter.with<ProgramItem, ItemAdapter<ProgramItem>>(programItemAdapter)
        recyclerView.adapter = fastAdapter

        val llm = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = llm

        swipeRefreshView.setOnRefreshListener(this)
        swipeRefreshView.setColorSchemeResources(R.color.greenPrimary, R.color.redPrimary, R.color.indigoPrimary, R.color.yellowPrimary)

        swipeRefreshView.isRefreshing = true
        updatePrograms()
    }

    override fun onRefresh() {
        updatePrograms()
    }

    private fun updatePrograms() {
        val adapter = recyclerView.adapter

        val accessToken = UserInfo.accessToken
        if (accessToken != null) {
            val head =
                    if(programItemAdapter.adapterItemCount != 0) {
                        programItemAdapter.getAdapterItem(0)
                    } else { null }
            val startedAt = head?.let { Utils.apiDateFormat.format(it.program.started_at) }

            AnnictClient.service.programs(
                    access_token = accessToken,
                    sort_started_at = "asc",
                    filter_started_at_gt = startedAt
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ programs ->
                    programs?.programs?.forEach {
                        programItemAdapter.add(0, ProgramItem(it, context))
                                adapter.notifyItemInserted(0)
                            }
                            swipeRefreshView.isRefreshing = false
                        }, {
                            swipeRefreshView.isRefreshing = false
                        })
        }
    }
}