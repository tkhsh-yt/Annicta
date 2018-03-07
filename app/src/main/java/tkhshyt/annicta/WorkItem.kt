package tkhshyt.annicta

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
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
import tkhshyt.annicta.event.UpdateStatusEvent
import tkhshyt.annicta.page.Page
import tkhshyt.annicta.page.go
import tkhshyt.annicta.pool.WorkPool
import javax.inject.Inject

class WorkItem(val id: Long, val activity: Activity) : AbstractItem<WorkItem, WorkItem.ViewHolder>() {

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v, activity)
    }

    override fun getType(): Int {
        return R.id.item_work
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_work
    }

    class ViewHolder(itemView: View, private val activity: Activity) : FastAdapter.ViewHolder<WorkItem>(itemView) {

        @Inject
        lateinit var workPool: WorkPool

        init {
            (activity.application as? MyApplication)?.getComponent()?.inject(this)
        }

        override fun bindView(workItem: WorkItem?, payloads: MutableList<Any>?) {
            if (workItem != null && workPool.containsWork(workItem.id)) {
                val work = workPool.getWork(workItem.id)!!
                itemView.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.go(
                                Page.WORK,
                                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, itemView.workIcon, itemView.workIcon.transitionName).toBundle(),
                                { it.putExtra("work_id", workItem.id) }
                        )
                    } else {
                        activity.go(Page.WORK, { it.putExtra("work_id", workItem.id) })
                    }
                }

                var imageUrl: String? = null
                if (work.images?.recommended_url != null && work.images.recommended_url.isNotBlank()) {
                    imageUrl = work.images.recommended_url
                } else if (work.images?.twitter?.image_url != null && work.images.twitter.image_url.isNotBlank()) {
                    imageUrl = work.images.twitter.image_url
                }
                if (imageUrl != null) {
                    Glide.with(activity)
                        .load(imageUrl)
                        .into(itemView.workIcon)
                }
                itemView.workTitle.text = work.title

                var selectedItem = -1
                val adapter = object : ArrayAdapter<String>(activity, R.layout.item_status, activity?.resources?.getStringArray(R.array.work_status_array)) {

                    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                        val view: View?
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
                        if (position == selectedItem) {
                            view.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_700))
                        } else {
                            view.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_800))
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
                            EventBus.getDefault().post(UpdateStatusEvent(work.id
                                    ?: -1, Kind.values()[position].kind))
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
