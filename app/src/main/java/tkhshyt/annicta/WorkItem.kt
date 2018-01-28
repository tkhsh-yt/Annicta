package tkhshyt.annicta

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_work.view.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.Kind
import tkhshyt.annict.json.Work
import tkhshyt.annicta.event.UpdateStatusEvent

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

                    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                        var view = convertView
                        if (convertView == null) {
                            view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.item_status, null)
                            view.findViewById<TextView>(R.id.status)?.text = getItem(position)
                            return view
                        }
                        convertView.findViewById<TextView>(R.id.status)?.text = getItem(position)

                        return convertView
                    }

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

                val selection = Math.max(Kind.stringToIndex(work.status?.kind), 0)
                itemView.statusSpinner.adapter = adapter
                itemView.statusSpinner.setSelection(selection)

                itemView.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (selectedItem != -1 && selectedItem != position) {
                            EventBus.getDefault().post(UpdateStatusEvent(workItem.work.id ?: -1, Kind.values()[position].kind))
                        }
                        selectedItem = position
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }

        override fun unbindView(item: WorkItem?) {
            itemView.workIcon.setImageResource(R.drawable.ic_image_black_24dp)
        }
    }
}
