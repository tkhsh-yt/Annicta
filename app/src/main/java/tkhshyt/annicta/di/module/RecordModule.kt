package tkhshyt.annicta.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tkhshyt.annicta.di.ViewModelKey
import tkhshyt.annicta.record.RecordViewModel

@Module
interface RecordModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecordViewModel::class)
    fun bindViewModel(viewModel: RecordViewModel): ViewModel
}