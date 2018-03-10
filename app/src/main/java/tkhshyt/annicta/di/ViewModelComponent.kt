package tkhshyt.annicta.di

import dagger.Subcomponent
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.auth.AuthFragment

@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {

    fun inject(activity: AuthActivity)
    fun inject(fragment: AuthFragment)
}