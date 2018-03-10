package tkhshyt.annicta.auth

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.AccessToken
import tkhshyt.annicta.BuildConfig
import javax.inject.Inject

class AuthRepository @Inject constructor(
        val annict: AnnictService
) {

    fun authorizeUrl(): String {
        return AnnictService.authorizeUrl(
                baseUrl = "https://ja.annict.com",
                client_id = BuildConfig.CLIENT_ID
        )
    }

    fun authorize(clientId: String, clientSecret: String, code: String): Single<AccessToken> {
        return annict.authorize(
                client_id = clientId,
                client_secret = clientSecret,
                code = code
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.body() }
    }

}