package tkhshyt.annicta.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tkhshyt.annicta.di.ViewModelKey
import tkhshyt.annicta.main.works.WorkItemViewModel

@Module
interface WorkItemModule {

    @Binds
    @IntoMap
    @ViewModelKey(WorkItemViewModel::class)
    fun bindViewModel(viewModel: WorkItemViewModel): ViewModel
}