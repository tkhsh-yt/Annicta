package tkhshyt.annicta.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import tkhshyt.annicta.auth.AuthRepository
import tkhshyt.annicta.auth.AuthViewModel

@Module
class ViewModelModule {

    @Provides
    fun provideAuthViewModel(context: Context, authRepository: AuthRepository): AuthViewModel {
        return AuthViewModel(context, authRepository)
    }
}