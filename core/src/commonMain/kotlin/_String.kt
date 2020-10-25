package tech.ketc.util

val String.strictLength get() = strictLength(this)

internal external fun strictLength(str: String): Int
