package tkhshyt.annicta.auth

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Intent
import android.databinding.ObservableBoolean
import android.net.Uri
import tkhshyt.annicta.BuildConfig
import tkhshyt.annicta.R
import tkhshyt.annicta.SingleLiveEvent

class AuthViewModel (
        context: Application,
        private val authRepository: AuthRepository
) : AndroidViewModel(context) {

    lateinit var authNavigator: AuthNavigator

    val toastMessage = SingleLiveEvent<Int>()
    val authorizing = ObservableBoolean(false)

    fun authorize(code: String) {
        authorizing.set(true)
        authRepository.authorize(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret= BuildConfig.CLIENT_SECRET,
                code = code
        ).doFinally {
            authorizing.set(false)
        }.subscribe({
            val accessToken = it.body()
            if (accessToken != null) {
                authNavigator.onAuthorize(accessToken)
            }
        }, {
            showToastMessage(R.string.fail_to_authorize)
        })
    }

    fun openAuthUrl() {
        val uri = Uri.parse(authRepository.authorizeUrl())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        super.getApplication<Application>().startActivity(intent)
    }

    fun showToastMessage(message: Int) {
        toastMessage.value = message
    }
}