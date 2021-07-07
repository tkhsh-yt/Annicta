package tkhshyt.annicta.main.works

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import io.reactivex.Single
import tkhshyt.annict.AnnictService
import tkhshyt.annict.Season
import tkhshyt.annict.Sort
import tkhshyt.annict.json.Status
import tkhshyt.annict.json.Work
import tkhshyt.annict.json.Works
import tkhshyt.annict.param.WorksParam
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

    var season: Season? = Season.season(Calendar.getInstance())

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
        val params = seasonSelectSpinner.genParam()
        params.filter_season = season
        params.access_token = accessToken
        params.page = page
        return worksRepository.worksWithStatus(params)
    }

    fun updateWorkStatus(id: Long?, kind: String) {
        val index = works.indexOfFirst { it.id == id }
        if (index > -1) {
            val work = works[index]
            works[index] = work.copy(status = Status(kind))
        }
    }
}