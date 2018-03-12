package tkhshyt.annicta.top

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Intent
import android.databinding.ObservableBoolean
import tkhshyt.annicta.BuildConfig
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.pref.UserInfo

class TopViewModel(
        context: Application
) : AndroidViewModel(context) {

    companion object {
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
    }

    val authorized = ObservableBoolean(UserInfo.accessToken != null)

    fun launchAuthActivity() {
        val intent = Intent(getApplication(), AuthActivity::class.java)
        intent.putExtra(CLIENT_ID, BuildConfig.CLIENT_ID)
        intent.putExtra(CLIENT_SECRET, BuildConfig.CLIENT_SECRET)
        getApplication<Application>().startActivity(intent)
    }

    fun launchMainActivity() {
    }
}