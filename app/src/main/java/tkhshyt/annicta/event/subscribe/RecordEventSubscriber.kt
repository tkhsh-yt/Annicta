package tkhshyt.annicta.event.subscribe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tkhshyt.annict.AnnictService
import tkhshyt.annicta.RecordDialogFragment
import tkhshyt.annicta.event.CreateRecordEvent
import tkhshyt.annicta.event.RecordedEvent
import tkhshyt.annicta.event.ShowRecordDialogEvent
import tkhshyt.annicta.pref.UserInfo

interface RecordEventSubscriber {

    var annict: AnnictService

    val baseActivity: AppCompatActivity

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRecordEvent(dialogEventShow: ShowRecordDialogEvent) {
        val args = Bundle()
        args.putSerializable("episode", dialogEventShow.episode)

        val dialog = RecordDialogFragment()
        dialog.arguments = args
        dialog.show(baseActivity.supportFragmentManager, "record")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCreateRecordEvent(event: CreateRecordEvent) {
        val createRecord = event.createRecord
        val accessToken = UserInfo.accessToken
        if (accessToken != null) {
            annict.createRecord(
                    access_token = accessToken,
                    episode_id = createRecord.episode.id!!,
                    rating_state = createRecord.rating_state,
                    comment = createRecord.comment,
                    share_twitter = createRecord.share_twitter,
                    share_facebook = createRecord.share_facebook
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val record = response.body()
                    Toast.makeText(baseActivity, "記録しました", Toast.LENGTH_LONG).show()
                    EventBus.getDefault().post(RecordedEvent(record))
                }, { _ ->
                    Toast.makeText(baseActivity, "記録に失敗しました", Toast.LENGTH_LONG).show()
                })
        }
    }
}
