package tkhshyt.annicta

import android.app.Activity
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_record.view.*
import tkhshyt.annict.json.Record

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
        }

        override fun unbindView(item: RecordItem?) {
        }
    }
}

