package tkhshyt.annicta.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import tkhshyt.annicta.di.ViewModelKey
import tkhshyt.annicta.main.MainViewModel
import tkhshyt.annicta.main.activities.ActivitiesFragment
import tkhshyt.annicta.main.programs.ProgramsFragment
import tkhshyt.annicta.main.works.WorksFragment

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindViewModel(viewModel: MainViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeProgramsFragment(): ProgramsFragment

    @ContributesAndroidInjector
    fun contributeWorksFragment(): WorksFragment

    @ContributesAndroidInjector
    fun contributeActivitiesFragment(): ActivitiesFragment
}