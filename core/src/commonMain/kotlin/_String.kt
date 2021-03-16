package tech.ketc.ktil

import kotlin.jvm.*

/**
 * Surrogate-pairs also count correctly
 */
val String.strictLength get() = StrExt.strictLength(this)

internal expect object StrExt {
    @JvmStatic
    fun strictLength(str: String): Int
}
