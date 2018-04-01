package tkhshyt.annicta.main.works

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import tkhshyt.annict.json.Work
import tkhshyt.annicta.R
import tkhshyt.annicta.data.UserInfoRepository
import tkhshyt.annicta.data.WorkRepository
import tkhshyt.annicta.event.UpdateWorkStatusEvent

class WorkItemViewModel(
        context: Application,
        private val userInfoRepository: UserInfoRepository,
        private val workRepository: WorkRepository
) : AndroidViewModel(context) {

//    lateinit var navigator: WorkItemNavigator

    val work = MutableLiveData<Work>()

    val status = MutableLiveData<Int>()

    private val statuses = arrayOf("no_select", "wanna_watch", "watching", "watched", "on_hold", "stop_watching")

    init {
        work.observeForever {
            val index = statuses.indexOf(work.value?.status?.kind)
            val position = if(index > -1) { index } else { 0 }
            status.value = position
        }
    }

    fun onStatusSelected(position: Int) {
        userInfoRepository.getAccessToken()?.let {
            workRepository.updateState(
                    access_token = it,
                    work_id = work.value?.id ?: -1,
                    kind = statuses[position]
            ).subscribe({
                EventBus.getDefault().post(UpdateWorkStatusEvent(work.value?.id, statuses[position]))
                showToastMessage(R.string.update_status)
            }, {
                it.printStackTrace()
                showToastMessage(R.string.fail_to_update_status)
            })
        }
    }

    fun onClick() {
    }

    private fun showToastMessage(message: Int) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}