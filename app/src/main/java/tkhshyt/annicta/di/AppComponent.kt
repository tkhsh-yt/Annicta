package tkhshyt.annicta.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import tkhshyt.annicta.AnnictApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun repositoryComponent(repositoryModule: RepositoryModule): RepositoryComponent

    fun viewModelComponent(viewModelModule: ViewModelModule): ViewModelComponent
}
