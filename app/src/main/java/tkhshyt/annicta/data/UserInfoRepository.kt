package tkhshyt.annicta.data

import android.app.Application
import android.content.Context
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.bulk
import tkhshyt.annicta.pref.UserInfo
import javax.inject.Inject

class UserInfoRepository @Inject constructor(
        val context: Application
) {

    init {
        Kotpref.init(context)
    }

    fun isAuthorize(): Boolean {
        return UserInfo.accessToken != null
    }

    fun getAccessToken(): String? {
        return UserInfo.accessToken
    }

    fun setAccessToken(accessToken: String) {
        UserInfo.accessToken = accessToken
    }
}