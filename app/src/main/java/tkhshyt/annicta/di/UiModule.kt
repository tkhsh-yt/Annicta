package tkhshyt.annicta.di

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.di.module.*
import tkhshyt.annicta.main.MainActivity
import tkhshyt.annicta.main.programs.ProgramsFragment
import tkhshyt.annicta.record.RecordActivity
import tkhshyt.annicta.record.RecordsFragment
import tkhshyt.annicta.top.TopActivity
import tkhshyt.annicta.work_info.WorkInfoActivity

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

    @ContributesAndroidInjector(modules = [RecordModule::class])
    internal abstract fun contributeRecordActivity(): RecordActivity

    @ContributesAndroidInjector(modules = [WorkInfoModule::class])
    internal abstract fun contributeWorkInfoActivity(): WorkInfoActivity
}