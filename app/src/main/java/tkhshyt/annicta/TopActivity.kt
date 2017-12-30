package tkhshyt.annicta

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.chibatching.kotpref.Kotpref
import tkhshyt.annicta.page.Page
import tkhshyt.annicta.page.go
import tkhshyt.annicta.pref.UserInfo

class TopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        Kotpref.init(this)

        if(UserInfo.accessToken != null) {
        } else {
            launchAuthorizeActivity()
        }
    }

    // 認証用アクティビティを起動する
    private fun launchAuthorizeActivity() {
        val intent = Intent()
        intent.putExtra("client_id", BuildConfig.CLIENT_ID)
        intent.putExtra("client_secret", BuildConfig.CLIENT_SECRET)

        go(Page.AUTH, { it.putExtras(intent) }, RequestCode.Auth)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            RequestCode.Auth -> { // 認証
                if(resultCode == Activity.RESULT_OK) {
                    UserInfo.accessToken = data?.getStringExtra("access_token")
                    Toast.makeText(this, "認証に成功しました", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
