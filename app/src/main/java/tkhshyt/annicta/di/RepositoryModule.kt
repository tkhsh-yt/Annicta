package tkhshyt.annicta.di

import dagger.Module
import dagger.Provides
import tkhshyt.annict.AnnictService
import tkhshyt.annicta.auth.AuthRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(annict: AnnictService): AuthRepository {
        return AuthRepository(annict)
    }
}
