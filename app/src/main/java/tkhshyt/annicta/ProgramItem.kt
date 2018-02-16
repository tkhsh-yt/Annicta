package tkhshyt.annicta

import android.app.Activity
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_program.view.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.json.Program
import tkhshyt.annicta.event.StartRecordActivityEvent
import tkhshyt.annicta.page.Page
import tkhshyt.annicta.page.go
import tkhshyt.annicta.utils.AnnictUtil

class ProgramItem(val program: Program, val activity: Activity?) : AbstractItem<ProgramItem, ProgramItem.ViewHolder>() {

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v, activity)
    }

    override fun getType(): Int {
        return R.id.item_program
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_program
    }

    class ViewHolder(itemView: View, private val activity: Activity?) : FastAdapter.ViewHolder<ProgramItem>(itemView) {

        override fun bindView(programItem: ProgramItem?, payloads: MutableList<Any>?) {
            if (programItem != null) {
                val program = programItem.program
                itemView.setOnClickListener {
                    val episode = program.episode.copy(work = program.work)
                    EventBus.getDefault().post(StartRecordActivityEvent(episode))
                    if (activity != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            activity.go(
                                    Page.RECORD,
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, itemView.workIcon, itemView.workIcon.transitionName).toBundle(),
                                    { it.putExtra("episode", episode) }
                            )
                        } else {
                            activity.go(
                                    Page.RECORD,
                                    { it.putExtra("episode", episode) }
                            )
                        }
                    }
                }

                var imageUrl: String? = null
                if (activity != null) {
                    if (program.work.images?.twitter?.image_url != null && program.work.images.twitter.image_url.isNotBlank()) {
                        imageUrl = program.work.images.twitter.image_url
                    } else if (program.work.images?.recommended_url != null && program.work.images.recommended_url.isNotBlank()) {
                        imageUrl = program.work.images.recommended_url
                    }
                    if (imageUrl != null) {
                        Glide.with(activity)
                            .load(imageUrl)
                            .into(itemView.workIcon)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    itemView.workIcon.transitionName = "work_icon"
                }
                itemView.start_at.text = AnnictUtil.textDateFormat.format(program.started_at)
                itemView.channel.text = program.channel.name
                itemView.workTitle.text = program.work.title
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
            itemView.workIcon.setImageResource(R.drawable.ic_image_black_24dp)
        }
    }
}
