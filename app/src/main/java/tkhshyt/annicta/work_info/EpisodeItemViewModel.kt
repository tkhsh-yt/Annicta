package tkhshyt.annicta.work_info

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import tkhshyt.annict.json.Episode
import tkhshyt.annicta.AnnictApplication
import tkhshyt.annicta.record.RecordActivity
import tkhshyt.annicta.record.RecordActivity.Companion.EPISODE
import tkhshyt.annicta.record.RecordActivity.Companion.EPISODE_ID

class EpisodeItemViewModel(
        context: Application
): AndroidViewModel(context) {

    val episode = MutableLiveData<Episode>()

    fun onClick() {
        val context = getApplication<AnnictApplication>()
        val intent = Intent(context, RecordActivity::class.java)
        intent.putExtra(EPISODE, episode.value)
        context.startActivity(intent)
    }
}