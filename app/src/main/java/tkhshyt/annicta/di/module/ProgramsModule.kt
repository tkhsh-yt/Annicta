package tkhshyt.annicta.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tkhshyt.annicta.di.ViewModelKey
import tkhshyt.annicta.main.programs.ProgramsViewModel

@Module
interface ProgramsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProgramsViewModel::class)
    fun bindViewModel(viewModel: ProgramsViewModel): ViewModel
}