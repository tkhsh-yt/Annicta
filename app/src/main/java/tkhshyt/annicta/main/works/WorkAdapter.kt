package tkhshyt.annicta.main.works

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tkhshyt.annict.json.Work
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ItemWorkBinding
import tkhshyt.annicta.layout.spinner.RatingAdapter

class WorkAdapter() : RecyclerView.Adapter<WorkAdapter.ViewHolder>() {

    var works = listOf<Work>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = DataBindingUtil.inflate<ItemWorkBinding>(inflater, R.layout.item_work, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return works.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val work = works[position]
        // 直接 new するのは避けたい
        val viewModel = WorkItemViewModel()
        viewModel.work.value = work
        // viewModel.navigator = navigator
        holder?.bind(viewModel)
    }

    class ViewHolder(val binding: ItemWorkBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            val statuses = binding.root.context.resources.getStringArray(R.array.work_status_array)
            binding.statusSpinner.adapter = RatingAdapter(binding.root.context, statuses)
        }

        fun bind(viewModel: WorkItemViewModel) {
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}