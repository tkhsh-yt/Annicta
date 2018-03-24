package tkhshyt.annicta.record

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_programs.*
import tkhshyt.annict.json.Record
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.FragmentRecordsBinding
import tkhshyt.annicta.di.Injectable
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.main.programs.ProgramAdapter
import javax.inject.Inject

class RecordsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: RecordsViewModel

    private lateinit var binding: FragmentRecordsBinding

    lateinit var adapter: RecordAdapter

    companion object {

        fun newInstance() = RecordsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<FragmentRecordsBinding>(inflater, R.layout.fragment_records, container, false).also {
                binding = it
            }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.records.observe(this, Observer<List<Record>> {
            if(it != null) {
                adapter.records = it
            }
        })

        setupSwipeRefreshView()
        setupRecyclerView()

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

        adapter = RecordAdapter()
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : EndlessScrollListener(llm) {
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        })
    }
}