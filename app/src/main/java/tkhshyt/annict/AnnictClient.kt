package tkhshyt.annict

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object AnnictClient {

    val baseUrl = "https://api.annict.com"

    val service = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(AnnictService::class.java)

    fun authorizeUrl(
            client_id: String,
            response_type: String = "code",
            redirect_uri: String = "urn:ietf:wg:oauth:2.0:oob",
            scope: String = "read write"
    ): String = "${baseUrl}/oauth/authorize?client_id=${client_id}&response_type=${response_type}&redirect_uri=${redirect_uri}&scope=${scope}"
}
