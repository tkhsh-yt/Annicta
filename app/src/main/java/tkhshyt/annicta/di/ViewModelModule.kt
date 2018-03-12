package tkhshyt.annicta.di

import android.app.Application
import dagger.Module
import dagger.Provides
import tkhshyt.annicta.auth.AuthRepository
import tkhshyt.annicta.auth.AuthViewModel
import tkhshyt.annicta.top.TopViewModel

@Module
class ViewModelModule {

    @Provides
    fun provideAuthViewModel(application: Application, authRepository: AuthRepository): AuthViewModel {
        return AuthViewModel(application, authRepository)
    }

    @Provides
    fun provideTopViewModel(application: Application): TopViewModel {
        return TopViewModel(application)
    }
}