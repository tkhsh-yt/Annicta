package tkhshyt.annicta.di

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.di.module.AuthModule
import tkhshyt.annicta.di.module.MainModule
import tkhshyt.annicta.di.module.ProgramsModule
import tkhshyt.annicta.di.module.TopModule
import tkhshyt.annicta.main.MainActivity
import tkhshyt.annicta.main.programs.ProgramsFragment
import tkhshyt.annicta.top.TopActivity

@Module
internal abstract class UiModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [AuthModule::class])
    internal abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector(modules = [TopModule::class])
    internal abstract fun contributeTopActivity(): TopActivity

    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [ProgramsModule::class])
    internal abstract fun contributeProgramsFragment(): ProgramsFragment

}