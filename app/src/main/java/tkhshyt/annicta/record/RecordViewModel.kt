package tkhshyt.annicta.record

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.json.Episode
import tkhshyt.annicta.R
import tkhshyt.annicta.SingleLiveEvent
import tkhshyt.annicta.data.EpisodesRepository
import tkhshyt.annicta.data.RecordRepository
import tkhshyt.annicta.data.UserConfigRepository
import tkhshyt.annicta.data.UserInfoRepository
import tkhshyt.annicta.event.CreateRecordEvent
import javax.inject.Inject

class RecordViewModel @Inject constructor(
        context: Application,
        private val userInfoRepository: UserInfoRepository,
        private val episodesRepository: EpisodesRepository,
        private val recordRepository: RecordRepository,
        private val userConfigRepository: UserConfigRepository
) : AndroidViewModel(context) {

    lateinit var navigator: RecordNavigator

    val episode = MutableLiveData<Episode>()

    val titleVisibility = MutableLiveData<Boolean>()

    val prevEpisode = MutableLiveData<Episode?>()
    val nextEpisode = MutableLiveData<Episode?>()

    val comment = MutableLiveData<String>()
    val rating = MutableLiveData<Int>()
    val shareTwitter = MutableLiveData<Boolean>()
    val shareFacebook = MutableLiveData<Boolean>()
    val enabled = MutableLiveData<Boolean>()

    val toastMessage = SingleLiveEvent<Int>()

    private val ratingStates = arrayOf(null, "bad", "average", "good", "great")

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

    fun setPrevNextEpisode() {
        userInfoRepository.getAccessToken()?.let {
            episodesRepository.episodes(
                    access_token = it,
                    filter_ids = episode.value?.id.toString()
            ).subscribe({
                val episode = it.episodes.first()
                prevEpisode.value = episode.prev_episode
                nextEpisode.value = episode.next_episode
            }, {
                showToastMessage(R.string.fail_to_get_episode)
            })
        }
    }

    fun onPrevEpisode() {
        episode.value?.let {
            episode.value = prevEpisode.value?.copy(work = it.work)
        }
    }

    fun onNextEpisode() {
        episode.value?.let {
            episode.value = nextEpisode.value?.copy(work = it.work)
        }
    }

    fun onRecord() {
        if(userInfoRepository.isAuthorize()) {
            val accessToken = userInfoRepository.getAccessToken()
            if(accessToken != null) {
                enabled.postValue(false)
                recordRepository.createRecord(
                        access_token = accessToken,
                        episode_id = episode?.value?.id ?: 0,
                        comment = comment.value,
                        rating_state = ratingStates[rating.value ?: 0],
                        share_twitter = shareTwitter.value == true,
                        share_facebook = shareFacebook.value == true
                ).doFinally {
                    enabled.postValue(true)
                }.subscribe({
                    EventBus.getDefault().post(CreateRecordEvent(it))
                    showToastMessage(R.string.success_to_record)
                    navigator.onRecorded()
                }, {
                    showToastMessage(R.string.fail_to_record)
                })
            }
        }
    }

    private fun showToastMessage(message: Int) {
        toastMessage.value = message
    }
}