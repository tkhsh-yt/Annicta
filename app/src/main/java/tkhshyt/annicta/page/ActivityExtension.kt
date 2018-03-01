package tkhshyt.annicta.page

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle

inline fun Context.go(page: Page, f: (intent: Intent) -> Intent) = startActivity(f(page.intent(applicationContext)))

fun Context.go(page: Page) = startActivity(page.intent(applicationContext))

inline fun Activity.go(page: Page, f: (intent: Intent) -> Intent, requestCode: Int) = startActivityForResult(f(page.intent(applicationContext)), requestCode)

fun Activity.go(page: Page, requestCode: Int) = startActivityForResult(page.intent(applicationContext), requestCode)

fun Context.go(page: Page, bundle: Bundle?) = {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(page.intent(applicationContext), bundle)
    }
}

fun Context.go(page: Page, bundle: Bundle?, f: (intent: Intent) -> Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(f(page.intent(applicationContext)), bundle)
    }
}
