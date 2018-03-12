package tkhshyt.annicta.di

import android.content.Context
import dagger.Module
import dagger.Provides
import tkhshyt.annict.AnnictService
import tkhshyt.annicta.data.AuthRepository
import tkhshyt.annicta.data.UserInfoRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserInfoRepository(context: Context): UserInfoRepository {
        return UserInfoRepository(context)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(annict: AnnictService): AuthRepository {
        return AuthRepository(annict)
    }
}
