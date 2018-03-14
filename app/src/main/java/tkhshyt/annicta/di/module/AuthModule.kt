package tkhshyt.annicta.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.auth.AuthViewModel
import tkhshyt.annicta.di.ViewModelFactory
import tkhshyt.annicta.di.ViewModelKey

@Module
interface AuthModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun bindViewModel(viewModel: AuthViewModel): ViewModel
}