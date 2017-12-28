package tkhshyt.annicta.pref

import com.chibatching.kotpref.KotprefModel

object UserInfo : KotprefModel() {
    var accessToken by nullableStringPref()
}