package tkhshyt.annicta.di

import dagger.Subcomponent
import tkhshyt.annicta.auth.AuthViewModel
import tkhshyt.annicta.main.MainActivity
import tkhshyt.annicta.main.MainViewModel
import tkhshyt.annicta.top.TopViewModel

@Subcomponent(modules = [RepositoryModule::class])
interface RepositoryComponent {

    fun inject(viewModel: TopViewModel)

    fun inject(viewModel: AuthViewModel)

    fun inject(viewModel: MainViewModel)
}