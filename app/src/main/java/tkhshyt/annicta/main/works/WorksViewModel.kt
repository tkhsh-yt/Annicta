package tkhshyt.annicta.main.works

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import io.reactivex.Single
import tkhshyt.annict.Season
import tkhshyt.annict.json.Status
import tkhshyt.annict.json.Work
import tkhshyt.annict.json.Works
import tkhshyt.annicta.MutableListLiveData
import tkhshyt.annicta.data.UserInfoRepository
import tkhshyt.annicta.data.WorkRepository
import tkhshyt.annicta.data.WorksRepository
import tkhshyt.annicta.main.works.SeasonSelectSpinner.*
import java.util.*
import javax.inject.Inject

class WorksViewModel @Inject constructor(
    context: Application,
    workRepository: WorkRepository,
    private val userInfoRepository: UserInfoRepository,
    private val worksRepository: WorksRepository
): AndroidViewModel(context) {

    lateinit var navigator: WorksNavigator

    val works = MutableListLiveData<Work>()
    val isLoading = MutableLiveData<Boolean>()

    var season: Season? = Season.Companion.season(Calendar.getInstance())

    val createWorkItemViewModel = {
        WorkItemViewModel(context, userInfoRepository, workRepository)
    }

    var page = 1

    var seasonSelectSpinner = CURRENT_SEASON

    init {
        isLoading.value = false
    }

    fun onStart() {
        onRefresh()
    }

    fun onRefresh() {
        if(seasonSelectSpinner == SELECT && season == null) {
            navigator.launchSeasonSelectDialog()
        } else {
            works.clear()
            page = 1

            loadMore()
        }
    }

    fun loadMore() {
        if(page > 0 && isLoading.value == false) {
            val accessToken = userInfoRepository.getAccessToken()
            if(accessToken != null) {
                val request = request(accessToken)
                if(request != null) {
                    isLoading.value = true
                    request.doFinally {
                        isLoading.postValue(false)
                    }.subscribe({
                        works.addAll(it.works)
                        page = it.next_page ?: -1
                    }, {
                        // TODO
                    })
                }
            }
        }
    }

    private fun request(accessToken: String): Single<Works>? {
        return when(seasonSelectSpinner) {
            NEXT_SEASON -> {
                worksRepository.worksWithStatus(
                        access_token = accessToken,
                        filter_season = "2018-summer", // FIXME
                        sort_watchers_count = "desc",
                        page = page
                )
            }
            CURRENT_SEASON -> {
                worksRepository.worksWithStatus(
                        access_token = accessToken,
                        filter_season = "2018-spring", // FIXME
                        sort_watchers_count = "desc",
                        page = page
                )
            }
            PREV_SEASON -> {
                worksRepository.worksWithStatus(
                        access_token = accessToken,
                        filter_season = "2018-winter", // FIXME
                        sort_watchers_count = "desc",
                        page = page
                )
            }
            FAVORITE -> {
                worksRepository.worksWithStatus(
                        access_token = accessToken,
                        sort_watchers_count = "desc",
                        page = page
                )
            }
            NEW -> {
                worksRepository.worksWithStatus(
                        access_token = accessToken,
                        sort_id = "desc",
                        page = page
                )
            }
            SELECT -> {
                worksRepository.worksWithStatus(
                        access_token = accessToken,
                        filter_season = season?.param(),
                        sort_watchers_count = "desc",
                        page = page
                )
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