package tkhshyt.annicta

import android.app.Activity
import android.view.View
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_record.view.*
import tkhshyt.annict.json.Record
import tkhshyt.annicta.utils.AnnictUtil

class RecordItem(val record: Record, val activity: Activity?) : AbstractItem<RecordItem, RecordItem.ViewHolder>() {

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v, activity)
    }

    override fun getType(): Int {
        return R.id.item_record
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_record
    }

    class ViewHolder(itemView: View, private val activity: Activity?) : FastAdapter.ViewHolder<RecordItem>(itemView) {

        override fun bindView(recordItem: RecordItem?, payloads: MutableList<Any>?) {
            itemView.userName.text = recordItem?.record?.user?.name.orEmpty()
            itemView.comment.text = recordItem?.record?.comment.orEmpty()

            Glide.with(activity)
                .load(recordItem?.record?.user?.avatar_url)
                .into(itemView.userIcon)

            itemView.createdAt.text = AnnictUtil.prettyDate(recordItem?.record?.created_at)

            val rating = recordItem?.record?.rating_state
            if (rating != null) {
                itemView.rating.text = AnnictUtil.ratingText(rating)
                itemView.rating.setBackgroundResource(AnnictUtil.ratingBadge(rating))
                itemView.rating.visibility = View.VISIBLE
            } else {
                itemView.rating.visibility = View.GONE
            }

            itemView.likesCount.text = recordItem?.record?.likes_count?.toString().orEmpty()
        }

        override fun unbindView(item: RecordItem?) {
        }
    }
}

