package tech.ketc.ktil

internal actual object StrExt {
    actual fun strictLength(str: String): Int = str.codePointCount(0, str.length)
}
