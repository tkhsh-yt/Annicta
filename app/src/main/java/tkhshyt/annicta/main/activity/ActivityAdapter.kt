package tkhshyt.annicta.main.activity

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import tkhshyt.annict.json.Activity
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ItemActivityBinding
import tkhshyt.annicta.layout.spinner.StatusAdapter

class ActivityAdapter(
        private val createViewModel: () -> ActivityItemViewModel
): RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    var activities = listOf<Activity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = DataBindingUtil.inflate<ItemActivityBinding>(inflater, R.layout.item_activity, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val activity = activities[position]
        val viewModel = createViewModel()
        viewModel.activity.value = activity
        holder?.bind(viewModel)
    }

    class ViewHolder(val binding: ItemActivityBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            val statuses = binding.root.context.resources.getStringArray(R.array.work_status_array)
            val adapter = StatusAdapter(binding.root.context, statuses)
            binding.statusSpinner.adapter = adapter
        }

        fun bind(viewModel: ActivityItemViewModel) {
            binding.statusSpinner.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.onStatusSelected(position)
            }
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}