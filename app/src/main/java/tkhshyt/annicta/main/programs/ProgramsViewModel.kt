package tkhshyt.annicta.main.programs

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import tkhshyt.annict.json.Program
import tkhshyt.annicta.MutableListLiveData
import tkhshyt.annicta.data.ProgramsRepository
import tkhshyt.annicta.data.UserInfoRepository
import javax.inject.Inject

class ProgramsViewModel @Inject constructor(
        context: Application,
        val userInfoRepository: UserInfoRepository,
        val programsRepository: ProgramsRepository
): AndroidViewModel(context) {

    val programs = MutableListLiveData<Program>()
    val isLoading = MutableLiveData<Boolean>()

    var page = 1

    init {
        isLoading.value = false
    }

    fun onStart() {
        onRefresh()
    }

    fun onRefresh() {
        programs.clear()
        page = 1

        loadMore()
    }

    fun loadMore() {
        if(page > 0 && isLoading.value == false) {
            isLoading.value = true
            val accessToken = userInfoRepository.getAccessToken()
            if (accessToken != null) {
                programsRepository.programs(
                        access_token = accessToken,
                        page = page
                ).doFinally {
                    isLoading.postValue(false)
                }.subscribe({
                        programs.addAll(it.programs)
                        page = it.next_page ?: -1
                }, {
                })
            }
        }
    }
}