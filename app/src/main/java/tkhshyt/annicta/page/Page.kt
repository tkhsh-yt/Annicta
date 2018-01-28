package tkhshyt.annicta.page

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import tkhshyt.annicta.*

enum class Page(val nameId: Int, val page: Class<out Activity>) {
    TOP(R.string.page_top, TopActivity::class.java) {
        override fun intent(context: Context): Intent {
            return super.intent(context).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
        }
    },
    AUTH(R.string.page_auth, AuthActivity::class.java) {
        override fun intent(context: Context): Intent {
            return super.intent(context).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
        }
    },
    MAIN(R.string.page_main, MainActivity::class.java),
    RECORD(R.string.page_record, RecordActivity::class.java);

    open fun intent(context: Context) = Intent(context, page)
    fun name(resources: Resources): String = resources.getString(nameId)
}