package tkhshyt.annicta.work_info

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import tkhshyt.annict.json.Episode

class EpisodeItemViewModel(): ViewModel() {

    val episode = MutableLiveData<Episode>()
}