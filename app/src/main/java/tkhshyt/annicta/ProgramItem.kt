package tkhshyt.annicta

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_program.view.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.json.Program
import tkhshyt.annicta.event.ShowRecordDialogEvent
import tkhshyt.annicta.utils.Utils

class ProgramItem(val program: Program, val context: Context?) : AbstractItem<ProgramItem, ProgramItem.ViewHolder>() {

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v, context)
    }

    override fun getType(): Int {
        return R.id.item_program
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_program
    }

    class ViewHolder(itemView: View, private val context: Context?) : FastAdapter.ViewHolder<ProgramItem>(itemView) {

        override fun bindView(programItem: ProgramItem?, payloads: MutableList<Any>?) {
            if (programItem != null) {
                val program = programItem.program
                itemView.setOnClickListener {
                    val episode = program.episode.copy(work = program.work)
                    EventBus.getDefault().post(ShowRecordDialogEvent(episode))
                }

                var imageUrl: String? = null
                if (context != null) {
                    if (program.work.images?.twitter?.image_url != null && program.work.images.twitter.image_url.isNotBlank()) {
                        imageUrl = program.work.images.twitter.image_url
                    } else if (program.work.images?.recommended_url != null && program.work.images.recommended_url.isNotBlank()) {
                        imageUrl = program.work.images.recommended_url
                    }
                    if (imageUrl != null) {
                        Glide.with(context)
                            .load(imageUrl)
                            .into(itemView.title_icon)
                    }
                }
                itemView.start_at.text = Utils.textDateFormat.format(program.started_at)
                itemView.channel.text = program.channel.name
                itemView.title.text = program.work.title
                val title = "${program.episode.number_text} ${program.episode.title.orEmpty()}"
                itemView.episode.text = title
                if (program.is_rebroadcast) {
                    itemView.rebroadcast.visibility = View.VISIBLE
                } else {
                    itemView.rebroadcast.visibility = View.GONE
                }
            }
        }

        override fun unbindView(item: ProgramItem?) {
            itemView.title_icon.setImageResource(R.drawable.ic_broken_image)
        }
    }
}
