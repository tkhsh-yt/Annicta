package tkhshyt.annicta

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.AnnictService
import tkhshyt.annicta.event.FailToAuthorizeEvent
import javax.inject.Inject

class AuthFragment : Fragment() {

    @Inject
    lateinit var annict: AnnictService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as? DaggerApplication)?.getComponent()?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 認証ボタンが押されたとき
        // 成功した場合はアクティビティを終了して，失敗した場合はイベント飛ばす．
        authorizeButton.setOnClickListener {
            annict.authorize(
                    client_id = BuildConfig.CLIENT_ID,
                    client_secret = BuildConfig.CLIENT_SECRET,
                    code = codeEditText.text.toString()
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val accessToken = response.body()
                    val intent = Intent()
                    intent.putExtra("access_token", accessToken.access_token)
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }, { throwable ->
                    EventBus.getDefault().post(FailToAuthorizeEvent(throwable))
                })
        }
    }
}