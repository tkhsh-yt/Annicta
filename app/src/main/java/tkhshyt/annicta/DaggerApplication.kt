package tkhshyt.annicta

import android.app.Application

class DaggerApplication: Application() {

    private lateinit var component: AppComponent

    fun getComponent(): AppComponent {
        return component
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
            .releaseModule(ReleaseModule())
            .build()
    }
}
