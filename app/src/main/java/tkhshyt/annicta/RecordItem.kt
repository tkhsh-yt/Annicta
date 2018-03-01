package tkhshyt.annicta

import android.app.Activity
import android.graphics.BlurMaskFilter
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_record.view.*
import tkhshyt.annict.json.Record
import tkhshyt.annicta.utils.AnnictUtil

class RecordItem(val record: Record, val activity: Activity?) : AbstractItem<RecordItem, RecordItem.ViewHolder>() {

    var blur = true

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
            itemView.activity.text = recordItem?.record?.user?.name.orEmpty()
            itemView.comment.text = recordItem?.record?.comment.orEmpty()

            // ぼかし（エピソードが視聴済みかどうか API で取得できないため保留）
//            if (recordItem?.blur == true) {
//                applyBlurMaskFilter(itemView.comment, BlurMaskFilter.Blur.NORMAL)
//            } else {
//                itemView.comment?.paint?.maskFilter = null
//            }
//
//            itemView.comment?.setOnClickListener {
//                recordItem?.blur = false
//                itemView.comment?.paint?.maskFilter = null
//                itemView.comment?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//            }

            Glide.with(activity)
                .load(recordItem?.record?.user?.avatar_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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

            itemView.likesCount.text = recordItem?.record?.likes_count?.toString()
        }

        override fun unbindView(item: RecordItem?) {
            itemView.userIcon.setImageResource(R.drawable.ic_account_circle)
        }

        fun applyBlurMaskFilter(tv: TextView, style: BlurMaskFilter.Blur) {
            val radius = 20.0f
            val filter = BlurMaskFilter(radius, style)

            tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            tv.paint.maskFilter = filter
        }
    }
}

