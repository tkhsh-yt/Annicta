package tkhshyt.annicta

import android.app.Application
import org.greenrobot.eventbus.EventBus
import tkhshyt.annicta.di.AppComponent
import tkhshyt.annicta.di.AppModule
import tkhshyt.annicta.di.DaggerAppComponent
import tkhshyt.annicta.di.EventBusModule

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
            .eventBusModule(EventBusModule(EventBus.getDefault()))
            .build()
    }
}
