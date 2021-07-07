package tkhshyt.annicta.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tkhshyt.annicta.auth.AuthViewModel
import tkhshyt.annicta.di.ViewModelKey

@Module
interface AuthModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun bindViewModel(viewModel: AuthViewModel): ViewModel
}