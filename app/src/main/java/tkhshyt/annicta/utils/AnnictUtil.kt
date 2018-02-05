package tkhshyt.annicta.utils

import tkhshyt.annicta.R
import java.text.SimpleDateFormat
import java.util.*

object AnnictUtil {

    val textDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN)

    val apiDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN)

    const val Sec = 1000
    const val Min = 60 * Sec
    const val Hour = 60 * Min
    const val Day = 24 * Hour

    fun prettyDate(date: Date?): String {
        if (date == null) {
            return ""
        }
        val now = Calendar.getInstance()

        val diff = now.time.time - date.time

        // TODO 「日前」の計算が正確でないので直す
        return when {
            diff <= Hour -> "${diff / Min}分前"
            diff <= Day -> "${diff / Hour}時間前"
            diff <= 4 * Day -> "${diff / Day}日前"
            else -> textDateFormat.format(date)
        }
    }

    fun ratingBadge(rating: String?): Int {
        return when(rating) {
            "bad" -> R.drawable.badge_bad
            "average" -> R.drawable.badge_average
            "good" -> R.drawable.badge_good
            "great" -> R.drawable.badge_great
            else -> -1
        }
    }

    fun ratingText(rating: String?): String? {
        return when(rating) {
            "bad" -> "良くない"
            "average" -> "普通"
            "good" -> "良い"
            "great" -> "とても良い"
            else -> null
        }
    }
}
