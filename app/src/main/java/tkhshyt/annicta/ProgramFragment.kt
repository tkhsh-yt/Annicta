package tkhshyt.annicta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import tkhshyt.annict.AnnictClient
import tkhshyt.annicta.pref.UserInfo
import tkhshyt.annicta.utils.Utils

class ProgramFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProgramAdapter()
        recyclerView.adapter = adapter

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
        val adapter = recyclerView.adapter as ProgramAdapter

        val accessToken = UserInfo.accessToken
        if (accessToken != null) {
            val head = adapter.list.getOrNull(0)
            val startedAt = head?.let { Utils.apiDateFormat.format(it.started_at) }

            AnnictClient.service.programs(
                    access_token = accessToken,
                    sort_started_at = "asc",
                    filter_started_at_gt = startedAt
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ programs ->
                    programs?.programs?.forEach {
                        adapter.list.add(0, it)
                        adapter.notifyItemInserted(0)
                    }
                    swipeRefreshView.isRefreshing = false
                }, {
                    swipeRefreshView.isRefreshing = false
                })
        }
    }
}