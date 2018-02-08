package tkhshyt.annicta.layout.recycler

import android.content.Context


object Util {

    fun calculateNoOfColumns(context: Context?): Int {
        if (context != null) {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            return (dpWidth / 160).toInt()
        }
        return 1
    }
}