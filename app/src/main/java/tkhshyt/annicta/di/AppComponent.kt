package tkhshyt.annicta.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import tkhshyt.annicta.AnnictApplication
import tkhshyt.annicta.di.module.AppModule
import tkhshyt.annicta.di.module.AuthModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    AndroidSupportInjectionModule::class,
    UiModule::class,
    AuthModule::class
])
interface AppComponent : AndroidInjector<AnnictApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: AnnictApplication)
}
