package tkhshyt.annicta.work_info

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import tkhshyt.annict.json.Episode
import tkhshyt.annict.json.Work
import tkhshyt.annicta.MutableListLiveData
import tkhshyt.annicta.data.EpisodesRepository
import tkhshyt.annicta.data.RecordsRepository
import tkhshyt.annicta.data.UserInfoRepository
import javax.inject.Inject

class WorkInfoesViewModel @Inject constructor(
        private val userInfoRepository: UserInfoRepository,
        private val episodesRepository: EpisodesRepository
): ViewModel() {

    lateinit var work: Work

    val infoes = MutableListLiveData<WorkInfoItem>()

    val isLoading = MutableLiveData<Boolean>()

    var page = 1

    init {
        isLoading.value = false
    }

    fun onRefresh() {
        infoes.clear()
        page = 1

        infoes.add(WorkItem(work))

        loadMore()
    }

    fun loadMore() {
        if(page > 0 && isLoading.value == false) {
            isLoading.value = true
            val accessToken = userInfoRepository.getAccessToken()
            if(accessToken != null) {
                episodesRepository.episodes(
                        access_token = accessToken,
                        filter_work_id = work.id.toString(),
                        sort_id = "asc",
                        page = page
                ).doFinally {
                    isLoading.postValue(false)
                }.subscribe({
                    infoes.addAll(it.episodes.map { EpisodeItem(it) })
                    page = it.next_page ?: -1
                }, {
                })
            }
        }
    }
}