package tkhshyt.annicta

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.AnnictClient
import tkhshyt.annicta.event.FailToAuthorizeEvent

class AuthFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 認証ボタンが押されたとき
        authorizeButton.setOnClickListener {
            AnnictClient.service.authorize(
                    client_id = BuildConfig.CLIENT_ID,
                    client_secret = BuildConfig.CLIENT_SECRET,
                    code = codeEditText.text.toString()
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ accessToken ->
                    val intent = Intent()
                    intent.putExtra("access_token", accessToken.access_token)
                    activity.setResult(Activity.RESULT_OK, intent)
                    activity.finish()
                }, { throwable ->
                    EventBus.getDefault().post(FailToAuthorizeEvent(throwable))
                })
        }
    }
}