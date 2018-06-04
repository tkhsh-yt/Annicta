package tkhshyt.annicta.work_info

import android.app.Application
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ItemEpisodeBinding
import tkhshyt.annicta.databinding.ItemWorkInfoBinding

class WorkInfoAdapter(val context: Application): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var infoes = listOf<WorkInfoItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    enum class ViewType {
        WORK {
            override fun createViewHolder(inflater: LayoutInflater, viewGroup: ViewGroup?): WorkInfoViewHolder {
                val binding = DataBindingUtil.inflate<ItemWorkInfoBinding>(inflater, R.layout.item_work_info, viewGroup, false)
                return WorkInfoViewHolder(binding)
            }

            override fun bindViewHolder(context: Application, holder: RecyclerView.ViewHolder, item: WorkInfoItem) {
                val work = (item as WorkItem).work
                 val viewModel = WorkInfoItemViewModel(context)
                viewModel.work.value = work
                (holder as WorkInfoViewHolder).bind(viewModel)
            }
        },
        EPISODE {

            override fun createViewHolder(inflater: LayoutInflater, viewGroup: ViewGroup?): WorkInfoAdapter.Companion.EpisodeViewHolder {
                val binding = DataBindingUtil.inflate<ItemEpisodeBinding>(inflater, R.layout.item_episode, viewGroup, false)
                return EpisodeViewHolder(binding)
            }

            override fun bindViewHolder(context: Application, holder: RecyclerView.ViewHolder, item: WorkInfoItem) {
                val episode = (item as EpisodeItem).episode
                val viewModel = EpisodeItemViewModel(context)
                viewModel.episode.value = episode
                (holder as EpisodeViewHolder).bind(viewModel)
            }
        };

        companion object {
            fun forId(id: Int): ViewType {
                for (viewType: ViewType in values()) {
                    if(viewType.ordinal == id) {
                        return viewType
                    }
                }
                throw AssertionError("no enum for the id. you forgot to implement?")
            }
        }

        abstract fun createViewHolder(inflater: LayoutInflater, viewGroup: ViewGroup?): RecyclerView.ViewHolder

        abstract fun bindViewHolder(context: Application, holder: RecyclerView.ViewHolder, item: WorkInfoItem)
    }

    companion object {

        class WorkInfoViewHolder(val binding: ItemWorkInfoBinding): RecyclerView.ViewHolder(binding.root) {

            fun bind(viewModel: WorkInfoItemViewModel) {
                binding.viewModel = viewModel
                binding.executePendingBindings()
            }
        }

        class EpisodeViewHolder(val binding: ItemEpisodeBinding): RecyclerView.ViewHolder(binding.root) {

            fun bind(viewModel: EpisodeItemViewModel) {
                binding.viewModel = viewModel
                binding.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return ViewType.forId(viewType).createViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return infoes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder ?: return
        val item = infoes[position]
        ViewType.forId(holder.itemViewType).bindViewHolder(context, holder, item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = infoes[position]
        return when(item) {
            is WorkItem -> {
                ViewType.WORK.ordinal
            }
            is EpisodeItem -> {
                ViewType.EPISODE.ordinal
            }
            else -> {
                throw AssertionError("no enum found for the id. you forgot to implement?")
            }
        }
    }
}