package tkhshyt.annicta.record

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import tkhshyt.annict.json.Record
import tkhshyt.annicta.MutableListLiveData
import tkhshyt.annicta.data.RecordsRepository
import tkhshyt.annicta.data.UserInfoRepository
import javax.inject.Inject

class RecordsViewModel @Inject constructor(
        context: Application,
        private val userInfoRepository: UserInfoRepository,
        private val recordsRepository: RecordsRepository
) : AndroidViewModel(context) {

    var episodeId: Long = -1
    set(value) {
        field = value
        records.clear()
        onRefresh()
    }
    val records = MutableListLiveData<Record>()
    val isLoading = MutableLiveData<Boolean>()

    var page = 1

    init {
        isLoading.value = false
    }

    fun onRefresh() {
        records.clear()
        page = 1

        loadMore()
    }

    fun loadMore() {
        if(page > 0 && isLoading.value == false) {
            isLoading.value = true
            val accessToken = userInfoRepository.getAccessToken()
            if (accessToken != null) {
                recordsRepository.records(
                        access_token = accessToken,
                        filter_episode_id = episodeId,
                        filter_has_record_comment = true,
                        sort_id = "desc",
                        page = page
                ).doFinally {
                    isLoading.postValue(false)
                }.subscribe({
                        records.addAll(it.records)
                        page = it.next_page ?: -1
                    }, {
                    })
            }
        }
    }
}