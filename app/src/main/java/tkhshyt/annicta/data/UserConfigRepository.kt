package tkhshyt.annicta.data

import android.app.Application
import com.chibatching.kotpref.Kotpref
import tkhshyt.annicta.pref.UserConfig
import javax.inject.Inject

class UserConfigRepository @Inject constructor(
        val context: Application
) {

    init {
        Kotpref.init(context)
    }

    fun shareTwitter(): Boolean {
        return UserConfig.shareTwitter
    }

    fun setTwitter(share: Boolean) {
        UserConfig.shareTwitter = share
    }

    fun shareFacebook(): Boolean {
        return UserConfig.shareFacebook
    }

    fun setFacebook(share: Boolean) {
        UserConfig.shareFacebook = share
    }
}