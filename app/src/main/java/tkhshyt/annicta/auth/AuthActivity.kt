package tkhshyt.annicta.auth

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_auth.*
import tkhshyt.annict.json.AccessToken
import tkhshyt.annicta.BaseActivity
import tkhshyt.annicta.R
import tkhshyt.annicta.databinding.ActivityAuthBinding
import tkhshyt.annicta.di.ViewModelModule
import tkhshyt.annicta.util.setupToast
import javax.inject.Inject

class AuthActivity : BaseActivity(), AuthNavigator {

    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val FONT_PATH = "Offside-Regular.ttf"
    }

    @Inject
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getInjector().viewModelComponent(ViewModelModule())
            .inject(this)

        val binding = DataBindingUtil
            .setContentView<ActivityAuthBinding>(this, R.layout.activity_auth)
        viewModel.authNavigator = this
        binding.viewModel = viewModel
        binding.executePendingBindings()

        supportActionBar?.hide()

        val typeface = Typeface.createFromAsset(assets, FONT_PATH)
        logoText.typeface = typeface
        appNameText.typeface = typeface

        viewModel.let {
            setupToast(this, it.toastMessage, Toast.LENGTH_SHORT)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        val uri = intent.data
        if (uri != null) {
            val code = intent.data.getQueryParameter("code")
            viewModel.authorize(code)
        }
    }

    override fun onAuthorize(accessToken: AccessToken) {
        val data = Intent()
        data.putExtra(ACCESS_TOKEN, accessToken.access_token)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
