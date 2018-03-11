package tkhshyt.annicta.di

import dagger.Subcomponent
import tkhshyt.annicta.auth.AuthActivity

@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {

    fun inject(activity: AuthActivity)
}