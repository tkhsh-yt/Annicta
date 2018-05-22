package tkhshyt.annicta.work_info

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import tkhshyt.annict.json.Work

class WorkInfoItemViewModel: ViewModel() {

    val work = MutableLiveData<Work>()
}