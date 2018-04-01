package tkhshyt.annicta.main.works

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import tkhshyt.annict.json.Status
import tkhshyt.annict.json.Work
import tkhshyt.annicta.MutableListLiveData
import tkhshyt.annicta.data.UserInfoRepository
import tkhshyt.annicta.data.WorkRepository
import tkhshyt.annicta.data.WorksRepository
import javax.inject.Inject

class WorksViewModel @Inject constructor(
    context: Application,
    workRepository: WorkRepository,
    private val userInfoRepository: UserInfoRepository,
    private val worksRepository: WorksRepository
): AndroidViewModel(context) {

    val works = MutableListLiveData<Work>()
    val isLoading = MutableLiveData<Boolean>()

    val createWorkItemViewModel = {
        WorkItemViewModel(context, userInfoRepository, workRepository)
    }

    var page = 1

    init {
        isLoading.value = false
    }

    fun onStart() {
        onRefresh()
    }

    fun onRefresh() {
        works.clear()
        page = 1

        loadMore()
    }

    fun loadMore() {
        if(page > 0 && isLoading.value == false) {
            val accessToken = userInfoRepository.getAccessToken()
            if(accessToken != null) {
                isLoading.value = true
                worksRepository.worksWithStatus(
                        access_token = accessToken,
                        filter_season = "2018-spring",
                        sort_watchers_count = "desc",
                        page = page
                ).doFinally {
                    isLoading.postValue(false)
                }.subscribe({
                    works.addAll(it.works)
                    page = it.next_page ?: -1
                }, {
                })
            }
        }
    }

    fun updateWorkStatus(id: Long?, kind: String) {
        val index = works.indexOfFirst { it.id == id }
        if (index > -1) {
            val work = works[index]
            works[index] = work.copy(status = Status(kind))
        }
    }
}