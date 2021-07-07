package tkhshyt.annicta.di.module

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tkhshyt.annict.AnnictService
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import java.io.IOException
import java.net.ProtocolException


@Module
class AppModule {

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.addNetworkInterceptor { chain ->
            try {
                chain.proceed(chain.request())
            } catch (e: ProtocolException) {
                Response.Builder()
                    .request(chain.request())
                    .code(204)
                    .protocol(Protocol.HTTP_1_1)
                    .build()
            }
        }.build()
    }

    @Singleton
    @Provides
    fun provideAnnictService(client: OkHttpClient): AnnictService {
        return Retrofit.Builder()
            .baseUrl("https://api.annict.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AnnictService::class.java)
    }
}