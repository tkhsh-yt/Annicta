package tkhshyt.annicta.main.activities

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.Spanned
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.Action
import tkhshyt.annict.json.Activity
import tkhshyt.annicta.AnnictApplication
import tkhshyt.annicta.R
import tkhshyt.annicta.data.UserInfoRepository
import tkhshyt.annicta.data.WorkRepository
import tkhshyt.annicta.event.UpdateWorkStatusEvent
import tkhshyt.annicta.util.AndroidUtil
import tkhshyt.annicta.util.AnnictUtil
import tkhshyt.annicta.work_info.WorkInfoActivity
import tkhshyt.annicta.work_info.WorkInfoActivity.Companion.WORK

class ActivityItemViewModel(
        context: Application,
        private val workRepository: WorkRepository,
        private val userInfoRepository: UserInfoRepository
) : AndroidViewModel(context) {

    lateinit var navigator: ActivityItemNavigator

    val activity = MutableLiveData<Activity>()

    val status = MutableLiveData<Int>()

    val activityText = MutableLiveData<Spanned>()

    // FIXME
    private val statuses = arrayOf("no_select", "wanna_watch", "watching", "watched", "on_hold", "stop_watching")

    init {
        activity.observeForever {
            val index = statuses.indexOf(activity.value?.work?.status?.kind)
            val position = if(index > -1) { index } else { 0 }
            status.value = position
        }

        activity.observeForever { activity ->
            val work = activity?.work
            if(activity != null && work != null) {
                val linkColor = ContextCompat.getColor(getApplication(), R.color.light_blue_800)
                val text = when (activity.action) {
                    Action.CreateRecord -> {
                        String.format("%s が %s %s を見ました",
                                AndroidUtil.colorHtml(activity.user?.name.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(activity.work.title.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(activity.episode?.number_text.orEmpty(), linkColor))
                    }
                    Action.CreateReview -> {
                        String.format("%s が %s のレビューを書きました ",
                                AndroidUtil.colorHtml(activity.user?.name.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(work.title.orEmpty(), linkColor))
                    }
                    Action.CreateMultipleRecords -> {
                        var text = String.format("%s が %s ",
                                activity.user?.name.orEmpty(),
                                work.title.orEmpty())
                        val records = activity.multiple_record
                        text += if (records?.size == 1) {
                            records.first().episode.number_text.orEmpty()
                        } else {
                            String.format("%s から %s",
                                    AndroidUtil.colorHtml(records?.last()?.episode?.number_text.orEmpty(), linkColor),
                                    AndroidUtil.colorHtml(records?.first()?.episode?.number_text.orEmpty(), linkColor))
                        }
                        text + "を見ました"
                    }
                    Action.CreateStatus -> {
                        String.format("%s が %s を「%s」に変更しました",
                                AndroidUtil.colorHtml(activity.user?.name.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(work.title.orEmpty(), linkColor),
                                AndroidUtil.colorHtml(AnnictUtil.kindText(activity.status?.kind), linkColor))
                    }
                    else -> { "" }
                }
                activityText.value = AndroidUtil.fromHtml(text)
            }
        }
    }

    fun onStatusSelected(position: Int) {
        userInfoRepository.getAccessToken()?.let {
            workRepository.updateState(
                    access_token = it,
                    work_id = activity.value?.work?.id ?: -1,
                    kind = statuses[position]
            ).subscribe({
                EventBus.getDefault().post(UpdateWorkStatusEvent(activity.value?.work?.id, statuses[position]))
                showToastMessage(R.string.update_status)
            }, {
                it.printStackTrace()
                showToastMessage(R.string.fail_to_update_status)
            })
        }
    }

    fun onClick() {
        activity.value?.work?.let {
            navigator.onItemClick(it)
        }
    }

    private fun showToastMessage(message: Int) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}