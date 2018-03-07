package tkhshyt.annicta

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import kotlinx.android.synthetic.main.item_work_info.view.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.Kind
import tkhshyt.annict.json.Work
import tkhshyt.annicta.event.UpdateStatusEvent

class WorkInfoItem(val work: Work, val activity: Activity?) : AbstractItem<WorkInfoItem, WorkInfoItem.ViewHolder>() {

    override fun getType(): Int {
        return R.id.item_work_info
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v, activity)
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_work_info
    }

    class ViewHolder(itemView: View, private val activity: Activity?) : FastAdapter.ViewHolder<WorkInfoItem>(itemView) {

        override fun bindView(workInfoItem: WorkInfoItem?, payloads: MutableList<Any>?) {
            if (workInfoItem != null) {
                val work = workInfoItem.work
                itemView.media.text = work.media_text

                if (work.images?.twitter?.original_avatar_url != null && work.images.twitter.original_avatar_url.isNotBlank()) {
                    if (activity != null) {
                        Glide.with(activity)
                            .load(work.images.twitter.original_avatar_url)
                            .apply(RequestOptions()
                                .circleCrop()
                                .placeholder(R.drawable.ic_account_circle)
                            ).into(itemView.twitterIcon)
                    }
                } else {
                    itemView.twitterIcon.setImageResource(R.drawable.ic_image_black_24dp)
                }

                itemView.workTitle.text = work.title
                itemView.watcherCount.text = work.watchers_count.toString()
                itemView.seasonName.text = work.season_name.orEmpty()
                itemView.releasedOn.text = work.released_on.orEmpty()

                if (work.twitter_username != null && work.twitter_username.isNotBlank()) {
                    itemView.twitterButton.setOnClickListener {
                        val url = "https://twitter.com/${work.twitter_username}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        activity?.startActivity(intent)
                    }
                    itemView.twitterButton.visibility = View.VISIBLE
                } else {
                    itemView.twitterButton.visibility = View.GONE
                }

                if (work.twitter_hashtag != null && work.twitter_hashtag.isNotBlank()) {
                    val buttonText = "#${work.twitter_hashtag}"
                    itemView.twitterHashtagButton.text = buttonText
                    itemView.twitterHashtagButton.setOnClickListener {
                        val url = "https://twitter.com/search?q=${work.twitter_hashtag}&src=hash"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        activity?.startActivity(intent)
                    }
                    itemView.twitterHashtagButton.visibility = View.VISIBLE
                } else {
                    itemView.twitterHashtagButton.visibility = View.GONE
                }

                if (work.wikipedia_url != null) {
                    itemView.wikipediaButton.setOnClickListener {
                        val url = work.wikipedia_url
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        activity?.startActivity(intent)
                    }
                    itemView.wikipediaButton.visibility = View.VISIBLE
                } else {
                    itemView.wikipediaButton.visibility = View.GONE
                }

                if (work.mal_anime_id != null) {
                    itemView.myAnimeListButton.setOnClickListener {
                        val url = "https://myanimelist.net/anime/${work.mal_anime_id}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        activity?.startActivity(intent)
                    }
                    itemView.myAnimeListButton.visibility = View.VISIBLE
                } else {
                    itemView.myAnimeListButton.visibility = View.GONE
                }

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

                if (work.episodes_count != null) {
                    itemView.episodeCount.text = work.episodes_count.toString()

                    itemView.episodeCountText.visibility = View.VISIBLE
                    itemView.episodeCountText.visibility = View.VISIBLE

                    if (work.episodes_count == 0) {
                        itemView.episodeCountText.visibility = View.GONE
                        itemView.episodeCount.visibility = View.GONE
                    }
                } else {
                    itemView.episodeCountText.visibility = View.GONE
                    itemView.episodeCount.visibility = View.GONE
                }
            }
        }

        override fun unbindView(item: WorkInfoItem?) {
        }
    }
}