package tkhshyt.annicta.work_info

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_work_info.*
import tkhshyt.annict.json.Application
import tkhshyt.annict.json.Work
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.FragmentWorkInfoBinding
import tkhshyt.annicta.di.Injectable
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.work_info.WorkInfoActivity.Companion.WORK
import javax.inject.Inject

class WorkInfoFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: WorkInfoesViewModel

    private lateinit var binding: FragmentWorkInfoBinding

    private lateinit var adapter: WorkInfoAdapter

    companion object {

        fun newInstance() = WorkInfoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<FragmentWorkInfoBinding>(inflater, R.layout.fragment_work_info, container, false).also {
                binding = it
            }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(arguments?.containsKey(WORK) == true) {
            arguments?.getSerializable(WORK)?.let {
                viewModel.work = it as Work
            }
        }

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.infoes.observe(this, Observer {
            if(it != null) {
                adapter.infoes = it
            }
        })

        setupRecyclerView()

        viewModel.onRefresh()
    }

    private fun setupRecyclerView() {
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm

        activity?.let {
            adapter = WorkInfoAdapter(it.application)
        }
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : EndlessScrollListener(llm) {
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        })
    }
}

