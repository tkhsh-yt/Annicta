package tkhshyt.annicta.top

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import tkhshyt.annicta.BuildConfig
import tkhshyt.annicta.R
import tkhshyt.annicta.auth.AuthActivity

class TopActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_AUTH = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        val intent = Intent(this, AuthActivity::class.java)
        intent.putExtra(TopViewModel.CLIENT_ID, BuildConfig.CLIENT_ID)
        intent.putExtra(TopViewModel.CLIENT_SECRET, BuildConfig.CLIENT_SECRET)
        startActivityForResult(intent, REQUEST_AUTH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_AUTH -> {
                if (resultCode == Activity.RESULT_OK) {
                    val accessToken = data?.getStringExtra(AuthActivity.ACCESS_TOKEN)
                    if (accessToken != null) {
                        // viewModel.onAuthorized(accessToken)
                    }
                }
            }
        }
    }

//    override fun launchAuthActivity() {
//        val intent = Intent(this, AuthActivity::class.java)
//        intent.putExtra(TopViewModel.CLIENT_ID, BuildConfig.CLIENT_ID)
//        intent.putExtra(TopViewModel.CLIENT_SECRET, BuildConfig.CLIENT_SECRET)
//        startActivityForResult(intent, REQUEST_AUTH)
//    }
}
