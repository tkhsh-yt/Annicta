package tkhshyt.annicta.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tkhshyt.annict.AnnictService
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAnnictService(): AnnictService {
        return Retrofit.Builder()
            .baseUrl("https://api.annict.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AnnictService::class.java)
    }
}