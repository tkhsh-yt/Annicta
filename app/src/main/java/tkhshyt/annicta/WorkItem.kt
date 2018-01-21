package tkhshyt.annicta

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_program.view.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.json.Work
import tkhshyt.annicta.event.ShowRecordDialogEvent
import tkhshyt.annicta.utils.Utils

class WorkItem(val work: Work, val context: Context?) : AbstractItem<WorkItem, WorkItem.ViewHolder>() {

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v, context)
    }

    override fun getType(): Int {
        return R.id.item_work
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_work
    }

    class ViewHolder(itemView: View, private val context: Context?) : FastAdapter.ViewHolder<WorkItem>(itemView) {

        override fun bindView(workItem: WorkItem?, payloads: MutableList<Any>?) {
            if (workItem != null) {
                val work = workItem.work
                itemView.setOnClickListener {
                    // TODO タイトルの詳細画面を開く
                }

                var imageUrl: String? = null
                if (context != null) {
                    if (work.images?.twitter?.image_url != null && work.images.twitter.image_url.isNotBlank()) {
                        imageUrl = work.images.twitter.image_url
                    } else if (work.images?.recommended_url != null && work.images.recommended_url.isNotBlank()) {
                        imageUrl = work.images.recommended_url
                    }
                    if (imageUrl != null) {
                        Glide.with(context)
                            .load(imageUrl)
                            .into(itemView.title_icon)
                    }
                }
                itemView.title.text = work.title
            }
        }

        override fun unbindView(item: WorkItem?) {
            itemView.title_icon.setImageResource(R.drawable.ic_broken_image)
        }
    }
}
