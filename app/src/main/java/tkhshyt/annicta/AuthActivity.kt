package tkhshyt.annicta

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_auth.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annicta.event.FailToAuthorizeEvent
import tkhshyt.annicta.event.OpenAnnictEvent
import tkhshyt.annicta.layout.message.MessageCreator
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var message: MessageCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // アクションバーを非表示
        supportActionBar?.hide()

        // フォントの設定
        val typeface = Typeface.createFromAsset(assets, "Offside-Regular.ttf")
        logoText.typeface = typeface
        appNameText.typeface = typeface

        // 認証画面を開くためのフラグメントをセット
        setupOpenAnnictFragment()
    }

    // 認証URLを開くためのフラグメントを設定
    private fun setupOpenAnnictFragment() {
        val openAnnictFragment = OpenAnnictFragment()
        val bundle = Bundle()
        bundle.putString("client_id", intent.getStringExtra("client_id"))
        openAnnictFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, openAnnictFragment)
        transaction.commit()
    }

    // 認証コードを入力するためのフラグメントを設定
    private fun setupAuthorizeFragment() {
        val authFragment = AuthFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, authFragment)
        transaction.commit()
    }

    // 認証URLを開いた後の処理
    // tkhshyt.annicta.OpeanAnnictFragment からイベントが投げられてくる．
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOpenAnnictEvent(event: OpenAnnictEvent) {
        setupAuthorizeFragment()
    }

    // 認証に失敗したときの処理
    // tkhshyt.annicta.AuthFragment からイベントが投げられてくる．
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFailToAuthorizeEvent(event: FailToAuthorizeEvent) {
        message.create()
            .context(this)
            .message(getString(R.string.fail_to_authorize))
            .build().show()
        setupOpenAnnictFragment()
    }

    override fun onStart() {
        super.onStart()

        // Pub/Sub
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()

        // Pub/Sub
        EventBus.getDefault().unregister(this)
    }
}
