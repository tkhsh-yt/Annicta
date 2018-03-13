package tkhshyt.annicta

import android.support.v4.app.Fragment
import tkhshyt.annicta.di.AppComponent

abstract class BaseFragment : Fragment() {

    fun getInjector(): AppComponent {
        return (context as AnnictApplication).getInjector()
    }
}