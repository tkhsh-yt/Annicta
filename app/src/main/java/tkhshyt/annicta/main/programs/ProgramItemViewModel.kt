package tkhshyt.annicta.main.programs

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import tkhshyt.annict.json.Program
import java.util.*

class ProgramItemViewModel : ViewModel() {

    lateinit var navigator: ProgramItemNavigator

    lateinit var program: Program

    fun onClick() {
        navigator.onItemClick(program)
    }

    fun isBroadcast(): Boolean {
        val now = Calendar.getInstance().timeInMillis
        return now - program.started_at.time > 0
    }
}