package tkhshyt.annicta.top

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chibatching.kotpref.Kotpref
import tkhshyt.annicta.BuildConfig
import tkhshyt.annicta.R
import tkhshyt.annicta.RequestCode
import tkhshyt.annicta.auth.AuthActivity
import tkhshyt.annicta.pref.UserInfo

class TopActivity : AppCompatActivity() {

    companion object {
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        Kotpref.init(this)

        if (UserInfo.accessToken != null) {
            // finish()
        } else {
            launchAuthorizeActivity()
        }
    }

    // 認証用アクティビティを起動する
    private fun launchAuthorizeActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.putExtra(CLIENT_ID, BuildConfig.CLIENT_ID)
        intent.putExtra(CLIENT_SECRET, BuildConfig.CLIENT_SECRET)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RequestCode.Auth -> { // 認証
                if (resultCode == Activity.RESULT_OK) {
                    // Success
                    finish()
                } else {
                    launchAuthorizeActivity()
                }
            }
        }
    }
}
