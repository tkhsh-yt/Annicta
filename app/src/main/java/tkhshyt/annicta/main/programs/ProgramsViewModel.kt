package tkhshyt.annicta.main.programs

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Build
import android.widget.Toast
import tkhshyt.annict.json.Program
import tkhshyt.annict.json.Record
import tkhshyt.annicta.AnnictApplication
import tkhshyt.annicta.MutableListLiveData
import tkhshyt.annicta.data.ProgramsRepository
import tkhshyt.annicta.data.UserInfoRepository
import javax.inject.Inject

class ProgramsViewModel @Inject constructor(
        context: Application,
        private val userInfoRepository: UserInfoRepository,
        private val programsRepository: ProgramsRepository
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
            val accessToken = userInfoRepository.getAccessToken()
            if (accessToken != null) {
                isLoading.value = true
                programsRepository.programs(
                        access_token = accessToken,
                        page = page
                ).doFinally {
                    isLoading.postValue(false)
                }.subscribe({
                        programs.addAll(it.programs)
                        page = it.next_page ?: -1
                }, {
                    // TODO
                })
            }
        }
    }

    fun removeRecord(record: Record) {
        val index = programs.indexOfFirst { it.episode.id == record.episode?.id }
        programs.removeAt(index)
    }
}