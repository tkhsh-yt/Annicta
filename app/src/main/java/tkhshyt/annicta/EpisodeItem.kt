package tkhshyt.annicta

import android.app.Activity
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_episode.view.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.json.Episode
import tkhshyt.annicta.event.StartRecordActivityEvent

class EpisodeItem(val episode: Episode, val activity: Activity?) : AbstractItem<EpisodeItem, EpisodeItem.ViewHolder>() {

    override fun getType(): Int {
        return R.id.item_episode
    }

    override fun getViewHolder(v: View?): ViewHolder {
        return ViewHolder(v, activity)
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_episode
    }

    class ViewHolder(itemView: View?, private val activity: Activity?) : FastAdapter.ViewHolder<EpisodeItem>(itemView) {

        override fun bindView(episodeItem: EpisodeItem?, payloads: MutableList<Any>?) {
            if (episodeItem != null) {
                val episode = episodeItem.episode
                itemView.setOnClickListener {
                    if (activity != null) {
                        EventBus.getDefault().post(StartRecordActivityEvent(episode))
                    }
                }

                itemView.episodeNumber.text = episode.number_text
                itemView.episodeTitle.text = episode.title
                itemView.episodeCount.text = episode.records_count.toString()
            }
        }

        override fun unbindView(item: EpisodeItem?) {
        }
    }
}