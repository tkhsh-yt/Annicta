package tkhshyt.annicta

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import tkhshyt.annicta.di.AppComponent

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    fun getInjector(): AppComponent {
        return (application as AnnictApplication).getInjector()
    }
}