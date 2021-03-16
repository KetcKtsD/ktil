package tech.ketc.ktil

/**
 * Throw the Receiver
 */
fun Throwable.raise(): Nothing = throw this
