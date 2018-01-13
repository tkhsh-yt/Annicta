package tkhshyt.annicta.page

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun Context.go(page: Page, f: (intent: Intent) -> Intent) = startActivity(f(page.intent(applicationContext)))

fun Context.go(page: Page) = startActivity(page.intent(applicationContext))

inline fun Activity.go(page: Page, f: (intent: Intent) -> Intent, requestCode: Int) = startActivityForResult(f(page.intent(applicationContext)), requestCode)

fun Activity.go(page: Page, requestCode: Int) = startActivityForResult(page.intent(applicationContext), requestCode)
