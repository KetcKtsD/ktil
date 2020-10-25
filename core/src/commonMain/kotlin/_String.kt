package tech.ketc.ktil

val String.strictLength get() = StrExt.strictLength(this)

internal expect object StrExt {
    fun strictLength(str: String): Int
}
