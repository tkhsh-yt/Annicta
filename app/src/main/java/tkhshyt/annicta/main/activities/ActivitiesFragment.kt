package tkhshyt.annicta.main.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_works.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annict.json.Work
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.FragmentActivitiesBinding
import tkhshyt.annicta.di.Injectable
import tkhshyt.annicta.event.UpdateWorkStatusEvent
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.work_info.WorkInfoActivity
import tkhshyt.annicta.work_info.WorkInfoActivity.Companion.WORK
import javax.inject.Inject

class ActivitiesFragment : Fragment(), Injectable, ActivityItemNavigator {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ActivitiesViewModel

    private lateinit var binding: FragmentActivitiesBinding

    private lateinit var adapter: ActivityAdapter

    companion object {

        fun newInstance() = ActivitiesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<FragmentActivitiesBinding>(inflater, R.layout.fragment_activities, container, false).also {
                binding = it
            }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.activities.observe(this, Observer {
            if(it != null) {
                adapter.activities = it
            }
        })

        setupRecyclerView()

        viewModel.onStart()
    }

    private fun setupRecyclerView() {
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm

        adapter = ActivityAdapter(viewModel.createActivityItemViewModel(this))
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : EndlessScrollListener(llm) {
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        })
    }

    override fun onItemClick(work: Work) {
        val intent = Intent(activity, WorkInfoActivity::class.java)
        intent.putExtra(WORK, work)
        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateWorkStatusEvent(event: UpdateWorkStatusEvent) {
        viewModel.updateWorkStatus(event.id, event.kind)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}