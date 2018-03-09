package tkhshyt.annicta.util

fun <A, B> A?.notNullIf(p: (A) -> Boolean, f: (A) -> B?): B? =
        if (this != null && p(this)) {
            f(this)
        } else {
            null
        }