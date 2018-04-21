package tkhshyt.annicta.main.works

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import tkhshyt.annict.json.Work
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ItemWorkBinding
import tkhshyt.annicta.layout.spinner.StatusAdapter

class WorkAdapter constructor(
        private val createViewModel: () -> WorkItemViewModel
) : RecyclerView.Adapter<WorkAdapter.ViewHolder>() {

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
        val viewModel = createViewModel()
        viewModel.work.value = work
        holder?.bind(viewModel)
    }

    class ViewHolder(val binding: ItemWorkBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            val statuses = binding.root.context.resources.getStringArray(R.array.work_status_array)
            val adapter = StatusAdapter(binding.root.context, statuses)
            binding.statusSpinner.adapter = adapter
        }

        fun bind(viewModel: WorkItemViewModel) {
            viewModel.status.observeForever {
                (binding.statusSpinner.adapter as StatusAdapter).selectedItem = it ?: -1
            }
            binding.statusSpinner.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.onStatusSelected(position)
            }
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}