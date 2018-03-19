package tkhshyt.annicta.main.programs

import android.arch.lifecycle.ViewModel
import tkhshyt.annict.json.Program

class ProgramItemViewModel : ViewModel() {

    lateinit var navigator: ProgramItemNavigator

    lateinit var program: Program

    fun onClick() {
        navigator.onItemClick(program)
    }
}