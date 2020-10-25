@file:Suppress("NOTHING_TO_INLINE")

package tech.ketc.util

/**
 * @param V Mapped Type
 */
interface RelaxedErrorMapper<V> {
    /**
     * Returns a [Throwable] or mapped value
     */
    fun map(e: Throwable): Either<Throwable, V>

    companion object {
        operator fun <V> invoke(map: (Throwable) -> Either<Throwable, V>): RelaxedErrorMapper<V> = Impl(map)
    }

    private class Impl<V>(private val map: (Throwable) -> Either<Throwable, V>) : RelaxedErrorMapper<V> {
        override fun map(e: Throwable): Either<Throwable, V> = map.invoke(e)
    }
}

interface StrictThrowableMapper<V> {
    fun map(e: Throwable): V

    companion object {
        operator fun <V> invoke(map: (Throwable) -> V): StrictThrowableMapper<V> = Impl(map)
    }

    private class Impl<V>(private val map: (Throwable) -> V) : StrictThrowableMapper<V> {
        override fun map(e: Throwable): V = map.invoke(e)
    }
}

inline fun <V, R> doTry(relaxed: RelaxedErrorMapper<V>, block: () -> R): Either<Either<Throwable, V>, R> = try {
    Either.right(block())
} catch (e: Throwable) {
    val value: Either<Throwable, V> = relaxed.map(e)
    Either.left(value)
}

inline fun <V, R> doTry(strict: StrictThrowableMapper<V>, block: () -> R): Either<V, R> = try {
    Either.right(block())
} catch (e: Throwable) {
    Either.left(strict.map(e))
}


inline fun <R> doTry(block: () -> R): Either<Throwable, R> = try {
    Either.right(block())
} catch (e: Throwable) {
    Either.left(e)
}
