package tkhshyt.annicta.main.programs

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tkhshyt.annict.json.Program
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ItemProgramBinding

class ProgramAdapter(private val navigator: ProgramItemNavigator) : RecyclerView.Adapter<ProgramAdapter.ViewHolder>() {

    var programs = listOf<Program>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val binding = DataBindingUtil.inflate<ItemProgramBinding>(inflater, R.layout.item_program, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return programs.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val program = programs[position]
        // 直接 new するのは避けたい
        val viewModel = ProgramItemViewModel()
        viewModel.program = program
        viewModel.navigator = navigator
        holder?.bind(viewModel)
    }

    class ViewHolder(val binding: ItemProgramBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ProgramItemViewModel) {
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}