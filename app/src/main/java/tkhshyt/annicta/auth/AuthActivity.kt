package tkhshyt.annicta.auth

import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_auth.*
import tkhshyt.annicta.AnnictApplication
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ActivityAuthBinding
import tkhshyt.annicta.di.ViewModelModule
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as AnnictApplication).getInjector()
            .viewModelComponent(ViewModelModule()).inject(this)

        val binding = DataBindingUtil.setContentView<ActivityAuthBinding>(this, R.layout.activity_auth)
        binding.openAnnict.viewmodel = viewModel
        binding.authorize.viewmodel = viewModel
        binding.viewmodel = viewModel
        binding.executePendingBindings()

        supportActionBar?.hide()

        val typeface = Typeface.createFromAsset(assets, "Offside-Regular.ttf")
        logoText.typeface = typeface
        appNameText.typeface = typeface
    }
}
