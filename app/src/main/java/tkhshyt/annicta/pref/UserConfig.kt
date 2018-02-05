package tkhshyt.annicta.pref

import com.chibatching.kotpref.KotprefModel

object UserConfig : KotprefModel() {
    var shareTwitter by booleanPref(false)
    var shareFacebook by booleanPref(false)
    var startedAtLt by intPref(2)
}