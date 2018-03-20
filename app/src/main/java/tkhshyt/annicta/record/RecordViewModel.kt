package tkhshyt.annicta.record

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import tkhshyt.annict.json.Episode
import tkhshyt.annict.json.Program
import tkhshyt.annicta.data.UserConfigRepository
import javax.inject.Inject

class RecordViewModel @Inject constructor(
        context: Application,
        val userConfigRepository: UserConfigRepository
) : AndroidViewModel(context) {

    lateinit var navigator: RecordNavigator

    lateinit var program: Program

    val prevEpisode = MutableLiveData<Episode?>()

    val nextEpisode = MutableLiveData<Episode?>()

    val titleVisibility = MutableLiveData<Boolean>()

    val shareTwitter = MutableLiveData<Boolean>()
    val shareFacebook = MutableLiveData<Boolean>()

    fun onStart() {
        shareTwitter.postValue(userConfigRepository.shareTwitter())
        shareFacebook.postValue(userConfigRepository.shareFacebook())
    }

    fun toggleShareTwitter() {
        userConfigRepository.setTwitter(shareTwitter.value == true)
    }

    fun toggleShareFacebook() {
        userConfigRepository.setFacebook(shareFacebook.value == true)
    }

    fun onClickBackArrow() {
        navigator.onClickBackArrow()
    }
}