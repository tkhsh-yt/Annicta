package tkhshyt.annicta.main.works

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import tkhshyt.annict.json.Work

class WorkItemViewModel : ViewModel() {

    val work = MutableLiveData<Work>()

    val status = MutableLiveData<Int>()

    private val statuses = arrayOf(null, "wanna_watch", "watching", "watched", "on_hold", "stop_watching")

    init {
        work.observeForever {
            val index = statuses.indexOf(work.value?.status?.kind)
            status.value = if(index > -1) {
                index
            } else {
                0
            }
        }
    }

    fun onClick() {
    }
}