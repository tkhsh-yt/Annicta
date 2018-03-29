package tkhshyt.annicta.main.works

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_works.*
import tkhshyt.annict.json.Work
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.FragmentWorksBinding
import tkhshyt.annicta.di.Injectable
import tkhshyt.annicta.layout.recycler.EndlessScrollListener
import tkhshyt.annicta.layout.recycler.Util
import javax.inject.Inject

class WorksFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: WorksViewModel

    private lateinit var binding: FragmentWorksBinding

    private lateinit var adapter: WorkAdapter

    companion object {
        fun newInstance() = WorksFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<FragmentWorksBinding>(inflater, R.layout.fragment_works, container, false).also {
                binding = it
            }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.works.observe(this, Observer<List<Work>> {
            if(it != null) {
                adapter.works = it
            }
        })

        setupRecyclerView()

        viewModel.onStart()
    }

    private fun setupRecyclerView() {
        val columns = Util.calculateNoOfColumns(context)
        val glm = GridLayoutManager(context, columns)
        recyclerView.layoutManager = glm

        adapter = WorkAdapter()
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : EndlessScrollListener(glm) {
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        })
    }
}