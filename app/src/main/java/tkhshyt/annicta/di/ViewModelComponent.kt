package tkhshyt.annicta.di

import dagger.Subcomponent
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.main.MainActivity
import tkhshyt.annicta.top.TopActivity

@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {

    fun inject(activity: AuthActivity)

    fun inject(activity: TopActivity)

    fun inject(activity: MainActivity)
}