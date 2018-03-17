package tkhshyt.annicta.main.programs

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.content.Context
import tkhshyt.annict.json.Program
import javax.inject.Inject

class ProgramItemViewModel (
): ViewModel() {

    lateinit var program: Program

    fun onClick() {
    }
}