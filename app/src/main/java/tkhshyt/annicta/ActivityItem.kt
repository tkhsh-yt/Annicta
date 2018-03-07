package tkhshyt.annicta

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
import com.bumptech.glide.request.RequestOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_activity.view.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.Action
import tkhshyt.annict.Kind
import tkhshyt.annict.json.Activity
import tkhshyt.annicta.event.UpdateStatusEvent
import tkhshyt.annicta.page.Page
import tkhshyt.annicta.page.go
import tkhshyt.annicta.utils.AndroidUtil
import tkhshyt.annicta.utils.AnnictUtil

class ActivityItem(val activity: Activity, val act: android.app.Activity?) : AbstractItem<ActivityItem, ActivityItem.ViewHolder>() {

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v, act)
    }

    override fun getType(): Int {
        return R.id.item_activity
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_activity
    }

    class ViewHolder(itemView: View, val act: android.app.Activity?) : FastAdapter.ViewHolder<ActivityItem>(itemView) {

        override fun bindView(item: ActivityItem?, payloads: MutableList<Any>?) {
            val activity = item?.activity
            val work = activity?.work

            if (activity != null && act != null && work != null) {
                Glide.with(act)
                    .load(item.activity.user?.avatar_url)
                    .apply(RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_account_circle)
                    ).into(itemView.userIcon)

                val linkColor = ContextCompat.getColor(act, R.color.light_blue_800)
                when (activity.action) {
                    Action.CreateRecord -> {
                        val text = String.format("%s が %s %s を見ました",
                                AndroidUtil.colorHtml(activity.user?.name.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(activity.work.title.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(activity.episode?.number_text.orEmpty(), linkColor))
                        itemView.activity.text = AndroidUtil.fromHtml(text)
                    }
                    Action.CreateReview -> {
                        val text = String.format("%s が %s のレビューを書きました ",
                                AndroidUtil.colorHtml(activity.user?.name.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(work.title.orEmpty(), linkColor))
                        itemView.activity.text = AndroidUtil.fromHtml(text)
                    }
                    Action.CreateMultipleRecords -> {
                        var text = String.format("%s が %s ",
                                activity.user?.name.orEmpty(),
                                work.title.orEmpty())
                        val records = activity.multiple_record
                        text += if (records?.size == 1) {
                            records.first().episode.number_text.orEmpty()
                        } else {
                            String.format("%s から %s",
                                    AndroidUtil.colorHtml(records?.last()?.episode?.number_text.orEmpty(), linkColor),
                                    AndroidUtil.colorHtml(records?.first()?.episode?.number_text.orEmpty(), linkColor))
                        }
                        text += "を見ました"
                        itemView.activity.text = AndroidUtil.fromHtml(text)
                    }
                    Action.CreateStatus -> {
                        val text = String.format("%s が %s を「%s」に変更しました",
                                AndroidUtil.colorHtml(activity.user?.name.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(work.title.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(AnnictUtil.kindText(activity.status?.kind), linkColor))
                        itemView.activity.text = AndroidUtil.fromHtml(text)
                    }
                }

                itemView.workCard.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        act.go(
                                Page.WORK,
                                ActivityOptionsCompat.makeSceneTransitionAnimation(act, itemView.workIcon, itemView.workIcon.transitionName).toBundle(),
                                { it.putExtra("work_id", work.id) }
                        )
                    } else {
                        act.go(Page.WORK, { it.putExtra("work_id", work.id) })
                    }
                }

                var imageUrl: String? = null
                if (work.images?.recommended_url != null && work.images.recommended_url.isNotBlank()) {
                    imageUrl = work.images.recommended_url
                } else if (work.images?.twitter?.image_url != null && work.images.twitter.image_url.isNotBlank()) {
                    imageUrl = work.images.twitter.image_url
                }
                if (imageUrl != null) {
                    Glide.with(this.act)
                        .load(imageUrl)
                        .into(itemView.workIcon)
                }

                itemView.workTitle.text = work.title
                var selectedItem = -1
                val adapter = object : ArrayAdapter<String>(this.act, R.layout.item_status, this.act.resources?.getStringArray(R.array.work_status_array)) {

                    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                        val view: View?
                        if (convertView == null) {
                            view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                                .inflate(R.layout.item_status, null)
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

        override fun unbindView(item: ActivityItem?) {
            itemView.userIcon.setImageResource(R.drawable.ic_account_circle)
            itemView.workIcon.setImageResource(R.drawable.ic_image_black_24dp)
        }
    }
}