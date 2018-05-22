package tkhshyt.annicta.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import tkhshyt.annicta.di.ViewModelKey
import tkhshyt.annicta.work_info.WorkInfoFragment
import tkhshyt.annicta.work_info.WorkInfoViewModel

@Module
interface WorkInfoModule {

    @Binds
    @IntoMap
    @ViewModelKey(WorkInfoViewModel::class)
    fun bindViewModel(viewModel: WorkInfoViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeWorkInfoFragment(): WorkInfoFragment
}