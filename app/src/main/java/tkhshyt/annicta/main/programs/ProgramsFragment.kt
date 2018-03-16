package tkhshyt.annicta.main.programs

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_programs.*
import tkhshyt.annict.json.Program
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.FragmentProgramsBinding
import tkhshyt.annicta.di.Injectable
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import javax.inject.Inject

class ProgramsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ProgramsViewModel

    private lateinit var binding: FragmentProgramsBinding

    private lateinit var adapter: ProgramAdapter

    companion object {
        fun newInstance() = ProgramsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<FragmentProgramsBinding>(inflater, R.layout.fragment_programs, container, false).also {
                binding = it
            }.root


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.programs.observe(this, Observer<List<Program>> {
            if(it != null) {
                adapter.programs = it
            }
        })

        setupRecyclerView()
        setupSwipeRefreshView()

        viewModel.onStart()
    }

    private fun setupSwipeRefreshView() {
        swipeRefreshView.setColorSchemeResources(R.color.green_500, R.color.red_500, R.color.indigo_500, R.color.yellow_500)
        swipeRefreshView.setOnRefreshListener {
            viewModel.onRefresh()
        }
    }

    private fun setupRecyclerView() {
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm

        adapter = ProgramAdapter()
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : EndlessScrollListener(llm) {
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        })
    }
}