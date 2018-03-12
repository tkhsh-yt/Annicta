package tkhshyt.annicta.top

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import tkhshyt.annicta.data.UserInfoRepository

class TopViewModel(
        context: Application,
        private val userInfoRepository: UserInfoRepository
) : AndroidViewModel(context) {

    companion object {
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
    }

    lateinit var topActivityNavigator: TopActivityNavigator

    fun onStart() {
        if (userInfoRepository.isAuthorize()) {
            launchMainActivity()
        } else {
            launchAuthActivity()
        }
    }

    fun onAuthorized(accessToken: String) {
        userInfoRepository.setAccessToken(accessToken)
        launchMainActivity()
    }

    fun launchAuthActivity() {
        topActivityNavigator.launchAuthActivity()
    }

    fun launchMainActivity() {
    }
}