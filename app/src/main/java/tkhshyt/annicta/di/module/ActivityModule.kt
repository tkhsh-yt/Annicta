package tkhshyt.annicta.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.top.TopActivity

@Module
interface ActivityModule {

    @ContributesAndroidInjector(modules = [AuthModule::class])
    fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector(modules = [TopModule::class])
    fun contributeTopActivity(): TopActivity
}