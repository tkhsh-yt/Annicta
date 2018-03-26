package tkhshyt.annicta.record

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import tkhshyt.annict.json.Episode
import tkhshyt.annict.json.Program
import tkhshyt.annicta.R
import tkhshyt.annicta.SingleLiveEvent
import tkhshyt.annicta.data.RecordRepository
import tkhshyt.annicta.data.UserConfigRepository
import tkhshyt.annicta.data.UserInfoRepository
import javax.inject.Inject

class RecordViewModel @Inject constructor(
        context: Application,
        val userInfoRepository: UserInfoRepository,
        val recordRepository: RecordRepository,
        val userConfigRepository: UserConfigRepository
) : AndroidViewModel(context) {

    lateinit var navigator: RecordNavigator

    lateinit var program: Program

    val titleVisibility = MutableLiveData<Boolean>()

    val prevEpisode = MutableLiveData<Episode?>()
    val nextEpisode = MutableLiveData<Episode?>()

    val comment = MutableLiveData<String>()
    val rating = MutableLiveData<Int>()
    val shareTwitter = MutableLiveData<Boolean>()
    val shareFacebook = MutableLiveData<Boolean>()
    val enabled = MutableLiveData<Boolean>()

    val toastMessage = SingleLiveEvent<Int>()

    val ratingStates = arrayOf(null, "bad", "average", "good", "great")

    fun onStart() {
        shareTwitter.postValue(userConfigRepository.shareTwitter())
        shareFacebook.postValue(userConfigRepository.shareFacebook())
        enabled.postValue(true)
    }

    fun onClickBackArrow() {
        navigator.onClickBackArrow()
    }

    fun toggleShareTwitter() {
        userConfigRepository.setTwitter(shareTwitter.value == true)
    }

    fun toggleShareFacebook() {
        userConfigRepository.setFacebook(shareFacebook.value == true)
    }

    fun onRecord() {
        if(userInfoRepository.isAuthorize()) {
            val accessToken = userInfoRepository.getAccessToken()
            if(accessToken != null) {
                enabled.postValue(false)
                recordRepository.createRecord(
                        access_token = accessToken,
                        episode_id = program.episode.id ?: 0,
                        comment = comment.value,
                        rating_state = ratingStates[rating.value ?: 0],
                        share_twitter = shareTwitter.value == true,
                        share_facebook = shareFacebook.value == true
                ).doFinally {
                    enabled.postValue(true)
                }.subscribe({
                    showToastMessage(R.string.success_to_record)
                    navigator.onRecorded()
                }, {
                    showToastMessage(R.string.fail_to_record)
                })
            }
        }
    }

    fun showToastMessage(message: Int) {
        toastMessage.value = message
    }
}