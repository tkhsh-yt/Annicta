package tkhshyt.annicta.di

import android.app.Application
import dagger.Module
import dagger.Provides
import tkhshyt.annicta.data.AuthRepository
import tkhshyt.annicta.auth.AuthViewModel
import tkhshyt.annicta.data.UserInfoRepository
import tkhshyt.annicta.main.MainViewModel
import tkhshyt.annicta.top.TopViewModel
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    fun provideAuthViewModel(application: Application, authRepository: AuthRepository): AuthViewModel {
        return AuthViewModel(application, authRepository)
    }

    @Provides
    fun provideTopViewModel(application: Application, userInfoRepository: UserInfoRepository): TopViewModel {
        return TopViewModel(application, userInfoRepository)
    }

    @Provides
    fun provideMainViewModel(): MainViewModel {
        return MainViewModel()
    }
}