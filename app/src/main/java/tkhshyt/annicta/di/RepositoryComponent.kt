package tkhshyt.annicta.di

import dagger.Subcomponent
import tkhshyt.annicta.auth.AuthViewModel

@Subcomponent(modules = [RepositoryModule::class])
interface RepositoryComponent {

    fun inject(viewModel: AuthViewModel)
}