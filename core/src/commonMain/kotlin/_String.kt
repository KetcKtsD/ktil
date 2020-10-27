package tech.ketc.ktil

/**
 * Surrogate-pairs also count correctly
 */
val String.strictLength get() = StrExt.strictLength(this)

internal expect object StrExt {
    fun strictLength(str: String): Int
}
