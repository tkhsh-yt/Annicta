package tkhshyt.annicta.record

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tkhshyt.annict.json.Record
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ItemRecordBinding

class RecordAdapter : RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    var records = listOf<Record>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = DataBindingUtil.inflate<ItemRecordBinding>(inflater, R.layout.item_record, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val record = records[position]

        val viewModel = RecordItemViewModel()
        viewModel.record = record
        holder?.bind(viewModel)
    }

    class ViewHolder(val binding: ItemRecordBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: RecordItemViewModel) {
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}