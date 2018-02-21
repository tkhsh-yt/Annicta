package tkhshyt.annicta.utils

fun <A, B> A?.notNull(f: (A) -> B?): B? =
        if (this != null) {
            f(this)
        } else {
            null
        }

fun <A, B> A?.notNullIf(p: (A) -> Boolean, f: (A) -> B?): B? =
        if (this != null && p(this)) {
            this.notNull(f)
        } else {
            null
        }