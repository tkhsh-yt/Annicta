package tkhshyt.annicta.di

import android.app.Application
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, EventBusModule::class])
interface AppComponent {

    fun repositoryComponent(repositoryModule: RepositoryModule): RepositoryComponent

    fun viewModelComponent(viewModelModule: ViewModelModule): ViewModelComponent
}
