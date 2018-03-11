package tkhshyt.annicta.auth

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Intent
import android.net.Uri
import tkhshyt.annicta.BuildConfig

class AuthViewModel (
        context: Application,
        private val authRepository: AuthRepository
) : AndroidViewModel(context) {

    lateinit var authNavigator: AuthNavigator

    fun authorize(code: String) {
        authRepository.authorize(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret= BuildConfig.CLIENT_SECRET,
                code = code
        ).subscribe({
            val accessToken = it.body()
            if (accessToken != null) {
                 authNavigator.onAuthorize(accessToken)
            }
        }, {
            authNavigator.onFailToAuthorize()
        })
    }

    fun openAuthUrl() {
        val uri = Uri.parse(authRepository.authorizeUrl())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        super.getApplication<Application>().startActivity(intent)
    }
}