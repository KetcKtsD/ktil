@file:Suppress("NOTHING_TO_INLINE")

package tech.ketc.ktil

/**
 * RelaxedErrorMapper
 */
interface RelaxedErrorMapper<V> {
    /**
     * Returns the [Throwable] or mapped value.
     */
    fun map(e: Throwable): Either<Throwable, V>

    companion object {
        operator fun <V> invoke(map: (Throwable) -> Either<Throwable, V>): RelaxedErrorMapper<V> = Impl(map)
    }

    private class Impl<V>(private val map: (Throwable) -> Either<Throwable, V>) : RelaxedErrorMapper<V> {
        override fun map(e: Throwable): Either<Throwable, V> = map.invoke(e)
    }
}

/**
 * StrictThrowableMapper
 */
interface StrictThrowableMapper<V> {
    /**
     * Returns the mapped value.
     */
    fun map(e: Throwable): V

    companion object {
        operator fun <V> invoke(map: (Throwable) -> V): StrictThrowableMapper<V> = Impl(map)
    }

    private class Impl<V>(private val map: (Throwable) -> V) : StrictThrowableMapper<V> {
        override fun map(e: Throwable): V = map.invoke(e)
    }
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] that was thrown from the [block] function execution and mapped by [relaxed] it as a failure.
 *
 * In case of successful, the result will be assigned to the Right.
 */
inline fun <V, R> doTry(relaxed: RelaxedErrorMapper<V>, block: () -> R): Either<Either<Throwable, V>, R> = try {
    Either.right(block())
} catch (e: Throwable) {
    val value: Either<Throwable, V> = relaxed.map(e)
    Either.left(value)
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] that was thrown from the [block] function execution and mapped by [strict] it as a failure.
 *
 * In case of successful, the result will be assigned to the Right.
 */
inline fun <V, R> doTry(strict: StrictThrowableMapper<V>, block: () -> R): Either<V, R> = try {
    Either.right(block())
} catch (e: Throwable) {
    Either.left(strict.map(e))
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] that was thrown from the [block] function execution and encapsulating it as a failure.
 *
 * In case of successful, the result will be assigned to the Right.
 */
inline fun <R> doTry(block: () -> R): Either<Throwable, R> = try {
    runCatching(block)
    Either.right(block())
} catch (e: Throwable) {
    Either.left(e)
}
