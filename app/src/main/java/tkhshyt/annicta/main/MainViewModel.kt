package tkhshyt.annicta.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import tkhshyt.annicta.AnnictApplication
import tkhshyt.annicta.R
import tkhshyt.annicta.data.ProgramsRepository
import tkhshyt.annicta.data.UserInfoRepository
import tkhshyt.annicta.main.programs.MainNavigator
import javax.inject.Inject

class MainViewModel @Inject constructor(
        context: Application,
        private val userInfoRepository: UserInfoRepository
) : AndroidViewModel(context) {

    lateinit var navigator: MainNavigator

    val selectedTabPosition = MutableLiveData<Int>()

    private val tabTitle = getApplication<AnnictApplication>().resources.getStringArray(R.array.tab)
    val title = Transformations.map(selectedTabPosition, {
        tabTitle[it]
    })

    val seasonTab = Transformations.map(selectedTabPosition, {
        it == 1
    })

    init {
        selectedTabPosition.postValue(0)
    }

    fun logout() {
        userInfoRepository.setAccessToken(null)
        navigator.restart()
    }
}