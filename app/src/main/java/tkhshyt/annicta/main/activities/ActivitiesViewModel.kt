package tkhshyt.annicta.main.activities

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import tkhshyt.annict.json.Activity
import tkhshyt.annict.json.Status
import tkhshyt.annicta.MutableListLiveData
import tkhshyt.annicta.data.ActivitiesRepository
import tkhshyt.annicta.data.UserInfoRepository
import tkhshyt.annicta.data.WorkRepository
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(
        context: Application,
        workRepository: WorkRepository,
        private val userInfoRepository: UserInfoRepository,
        private val activitiesRepository: ActivitiesRepository
) : AndroidViewModel(context) {

    val activities = MutableListLiveData<Activity>()
    val isLoading = MutableLiveData<Boolean>()

    val createActivityItemViewModel = {
        ActivityItemViewModel(context, workRepository, userInfoRepository)
    }

    var page = 1

    init {
        isLoading.value = false
    }

    fun onStart() {
        onRefresh()
    }

    fun onRefresh() {
        activities.clear()
        page = 1

        loadMore()
    }

    fun loadMore() {
        if(page > 0 && isLoading.value == false) {
            userInfoRepository.getAccessToken()?.let {
                isLoading.value = true
                activitiesRepository.followingActivitiesWithStatus(
                        access_token = it,
                        page = page
                ).doFinally {
                    isLoading.postValue(false)
                }.subscribe({
                    activities.addAll(it.activities)
                    page = it.next_page ?: -1
                }, {
                    // TODO
                })
            }
        }
    }

    fun updateWorkStatus(id: Long?, kind: String) {
        val updates = activities.mapIndexed { index, activity ->
            Pair(index, activity)
        }.filter {
            it.second.work?.id == id
        }
        updates.forEach {
            val index = it.first
            val activity = it.second
            if(activity.work?.id == id) {
                activities[index] = activity.copy(work = activity.work?.copy(status = Status(kind)))
            }
        }
    }
}