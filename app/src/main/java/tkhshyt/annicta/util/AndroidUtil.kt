package tkhshyt.annicta.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils


object AndroidUtil {

    @SuppressWarnings("deprecation")
    fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    fun colorHtml(text: String, color: Int): String {
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        return String.format("<font color=\"%s\">%s</font>", hexColor, TextUtils.htmlEncode(text))
    }
}