package tkhshyt.annicta

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tkhshyt.annict.AnnictService
import tkhshyt.annicta.layout.message.MessageCreator
import tkhshyt.annicta.layout.message.ToastMessageCreator
import javax.inject.Singleton

@Module
class ReleaseModule {

    @Singleton
    @Provides
    fun provideAnnictService(): AnnictService {
        val baseUrl = "https://api.annict.com"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AnnictService::class.java)
    }

    @Singleton
    @Provides
    fun provideMessageCreator(): MessageCreator {
        return ToastMessageCreator()
    }
}