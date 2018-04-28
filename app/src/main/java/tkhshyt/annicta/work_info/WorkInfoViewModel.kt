package tkhshyt.annicta.work_info

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import tkhshyt.annict.json.Work
import javax.inject.Inject

class WorkInfoViewModel @Inject constructor(): ViewModel() {

    val work = MutableLiveData<Work>()

    lateinit var navigator: WorkInfoNavigator

    val titleVisibility = MutableLiveData<Boolean>()

    fun onClickBackArrow() {
        navigator.onClickBackArrow()
    }
}