package tkhshyt.annicta.top

import android.databinding.Observable
import android.os.Bundle
import com.chibatching.kotpref.Kotpref
import tkhshyt.annicta.BaseActivity
import tkhshyt.annicta.R
import tkhshyt.annicta.di.ViewModelModule
import javax.inject.Inject

class TopActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: TopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        Kotpref.init(this)

        getInjector().viewModelComponent(ViewModelModule())
            .inject(this)

        viewModel.authorized.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(p0: Observable?, p1: Int) {
                if(viewModel.authorized.get()) {
                    viewModel.launchAuthActivity()
                } else {
                    viewModel.launchMainActivity()
                }
            }
        })
    }
}
