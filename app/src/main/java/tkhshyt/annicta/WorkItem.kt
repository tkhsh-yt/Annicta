package tkhshyt.annicta

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_work.view.*
import tkhshyt.annict.Kind
import tkhshyt.annict.json.Work

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

                var selectedItem = -1
                val adapter = object : ArrayAdapter<String>(context, R.layout.item_status, context?.resources?.getStringArray(R.array.work_status_array)) {

                    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
                        val view = super.getDropDownView(position, convertView, parent)
                        if(position == selectedItem) {
                            view.setBackgroundColor(ContextCompat.getColor(context, R.color.spinner_selected_color))
                        } else {
                            view.setBackgroundColor(ContextCompat.getColor(context, R.color.status_dropdown_color))
                        }
                        return view
                    }
                }
                adapter.setDropDownViewResource(R.layout.item_status_dropdown)

                itemView.statusSpinner.adapter = adapter
                itemView.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedItem = position
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
                itemView.statusSpinner.setSelection(Kind.getIndex(work.status?.kind))
            }
        }

        override fun unbindView(item: WorkItem?) {
            itemView.title_icon.setImageResource(R.drawable.ic_broken_image)
        }
    }
}
