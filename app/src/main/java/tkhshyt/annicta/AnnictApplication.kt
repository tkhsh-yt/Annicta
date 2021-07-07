package tkhshyt.annicta

import dagger.android.support.DaggerApplication
import tkhshyt.annicta.di.DaggerAppComponent
import tkhshyt.annicta.di.applyAutoInjector

class AnnictApplication : DaggerApplication() {

    override fun applicationInjector() = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()

        applyAutoInjector()
    }
}
