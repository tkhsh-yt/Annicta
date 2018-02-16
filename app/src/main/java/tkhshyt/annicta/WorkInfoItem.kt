package tkhshyt.annicta

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_work_info.view.*
import tkhshyt.annict.json.Work

class WorkInfoItem(val work: Work, val activity: Activity?) : AbstractItem<WorkInfoItem, WorkInfoItem.ViewHolder>() {

    override fun getType(): Int {
        return R.id.item_work_info
    }

    override fun getViewHolder(v: View?): ViewHolder {
        return ViewHolder(v, activity)
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_work_info
    }

    class ViewHolder(itemView: View?, private val activity: Activity?) : FastAdapter.ViewHolder<WorkInfoItem>(itemView) {

        override fun bindView(workInfoItem: WorkInfoItem?, payloads: MutableList<Any>?) {
            if (workInfoItem != null) {
                val work = workInfoItem.work
                itemView.media.text = work.media_text


                if (work.images?.twitter?.original_avatar_url != null) {
                    Glide.with(activity)
                        .load(work.images.twitter.original_avatar_url)
                        .into(itemView.twitterIcon)
                } else {
                    itemView.twitterIcon.setImageResource(R.drawable.ic_image_black_24dp)
                }

                itemView.workTitle.text = work.title
                itemView.watcherCount.text = work.watchers_count.toString()
                itemView.seasonName.text = work.season_name.orEmpty()
                itemView.releasedOn.text = work.released_on.orEmpty()

                if (work.twitter_username != null) {
                    itemView.twitterButton.setOnClickListener {
                        val url = "https://twitter.com/${work.twitter_username}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        activity?.startActivity(intent)
                    }
                    itemView.twitterButton.visibility = View.VISIBLE
                } else {
                    itemView.twitterButton.visibility = View.GONE
                }

                if (work.twitter_hashtag != null) {
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