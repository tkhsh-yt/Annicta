package tkhshyt.annicta

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import tkhshyt.annicta.di.AppComponent
import tkhshyt.annicta.di.AppModule
import tkhshyt.annicta.di.DaggerAppComponent

class AnnictApplication : Application() {

    private lateinit var injector: AppComponent

    fun getInjector(): AppComponent {
        return injector
    }

    override fun onCreate() {
        super.onCreate()

        injector = buildAppComponent()
    }

    protected fun buildAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
