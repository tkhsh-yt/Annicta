package tkhshyt.annicta.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import tkhshyt.annicta.auth.AuthActivity

@Module
interface ActivityModule {

    @ContributesAndroidInjector(modules = [AuthModule::class])
    fun contributeAuthActivity(): AuthActivity
}