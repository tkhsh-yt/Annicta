package tkhshyt.annicta.auth

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri

class AuthViewModel (
        val context: Context,
        val authRepository: AuthRepository
) : ViewModel() {

    val visibleOpenBrowser = ObservableBoolean(true)

    val code = ObservableField<String>()

    fun authorize() {
    }

    fun openAuthUrl() {
        val uri = Uri.parse(authRepository.authorizeUrl())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
        showAuthorizeView()
    }

    fun showAuthorizeView() {
        visibleOpenBrowser.set(false)
    }
}