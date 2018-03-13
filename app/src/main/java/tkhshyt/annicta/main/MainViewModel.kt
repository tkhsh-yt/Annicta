package tkhshyt.annicta.main

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log

class MainViewModel(

): ViewModel() {

    val title = ObservableField("")

    val seasonTab = ObservableBoolean(false)

    fun onStart() {
    }
}