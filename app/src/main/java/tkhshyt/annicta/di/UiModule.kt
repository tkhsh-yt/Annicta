package tkhshyt.annicta.di

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.di.module.AuthModule

@Module
internal abstract class UiModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [AuthModule::class])
    internal abstract fun contributeAuthActivity(): AuthActivity

}