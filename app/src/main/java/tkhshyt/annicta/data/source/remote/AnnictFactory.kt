package tkhshyt.annicta.data.source.remote

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tkhshyt.annict.AnnictService

object AnnictFactory {

    private var INSTANCE: AnnictService? = null

    fun create(): AnnictService {
        if (INSTANCE == null) {
            val baseUrl = "https://api.annict.com"
            INSTANCE = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AnnictService::class.java)
        }
        return INSTANCE!!
    }

}