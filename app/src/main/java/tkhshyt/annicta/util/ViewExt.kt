package tkhshyt.annicta.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import android.widget.Toast
import tkhshyt.annicta.SingleLiveEvent

fun Context.showToast(toastText: String, timeLength: Int) {
    Toast.makeText(this, toastText, timeLength).show()
}

fun Context.setupToast(lifecycleOwner: LifecycleOwner,
                    toastMessageLiveEvent: SingleLiveEvent<Int>, timeLength: Int) {
    toastMessageLiveEvent.observe(lifecycleOwner, Observer {
        it?.let { showToast(this.getString(it), timeLength) }
    })
}