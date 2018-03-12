package tkhshyt.annicta.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import tkhshyt.annict.AnnictService
import tkhshyt.annict.json.AccessToken
import tkhshyt.annicta.BuildConfig
import javax.inject.Inject

class AuthRepository @Inject constructor(
        val annict: AnnictService
) {

    companion object {
        const val REDIRECT_URI = "annicta://callback"
    }

    fun authorizeUrl(): String {
        return AnnictService.authorizeUrl(
                baseUrl = "https://ja.annict.com",
                client_id = BuildConfig.CLIENT_ID,
                redirect_uri = REDIRECT_URI
        )
    }

    fun authorize(clientId: String, clientSecret: String, code: String): Single<Response<AccessToken>> {
        return annict.authorize(
                client_id = clientId,
                client_secret = clientSecret,
                code = code,
                redirect_uri = REDIRECT_URI
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}