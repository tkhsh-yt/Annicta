package tkhshyt.annicta.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tkhshyt.annicta.di.ViewModelKey
import tkhshyt.annicta.top.TopViewModel

@Module
interface TopModule {

    @Binds
    @IntoMap
    @ViewModelKey(TopViewModel::class)
    fun bindViewModel(viewModel: TopViewModel): ViewModel
}