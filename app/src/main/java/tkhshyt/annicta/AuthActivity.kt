package tkhshyt.annicta

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_auth.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annicta.event.FailToAuthorizeEvent
import tkhshyt.annicta.event.OpenAnnictEvent

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        supportActionBar?.hide()

        val typeface = Typeface.createFromAsset(assets, "Offside-Regular.ttf")
        logoTextView.typeface = typeface
        appNameTextView.typeface = typeface

        commitOpenAnnictFragment()
    }

    private fun commitOpenAnnictFragment() {
        val openAnnictFragment = OpenAnnictFragment()
        val bundle = Bundle()
        bundle.putString("client_id", intent.getStringExtra("client_id"))
        openAnnictFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, openAnnictFragment)

        transaction.commit()
    }

    private fun commitAuthorizeFragment() {
        val authFragment = AuthFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, authFragment)

        transaction.commit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOpenAnnictEvent(event: OpenAnnictEvent) {
        commitAuthorizeFragment()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFailToAuthorizeEvent(event: FailToAuthorizeEvent) {
        Toast.makeText(this, "認証に失敗しました", Toast.LENGTH_LONG).show()
        commitOpenAnnictFragment()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}
