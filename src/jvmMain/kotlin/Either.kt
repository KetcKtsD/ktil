package tech.ketc.util

import kotlin.contracts.*

/**
 * Simple Either type
 */
@OptIn(ExperimentalContracts::class)
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class Either<L, R> @PublishedApi internal constructor(
    @PublishedApi
    internal val leftOrRight: LeftOrRight
) {
    companion object {
        /**
         * Create a Left
         */
        fun <L, R> left(value: L): Either<L, R> = Either(LeftOrRight(value, true))

        /**
         * Create a Right
         */
        fun <L, R> right(value: R): Either<L, R> = Either(LeftOrRight(value, false))
    }

    /**
     * Returns the true, if this is a Left
     */
    inline val isLeft get() = leftOrRight.isLeft

    /**
     * Returns the true, if this is a Right
     */
    inline val isRight get() = leftOrRight.isRight

    /**
     * Returns the value from this Left
     *
     * @throws NoSuchElementException if this is a Right
     */
    @Suppress("UNCHECKED_CAST")
    inline val left: L
        get() = if (leftOrRight.isLeft) leftOrRight.value as L else
            throw NoSuchElementException("projection is Right")

    /**
     * Returns the value from this Right
     *
     * @throws NoSuchElementException if this is a Left
     */
    @Suppress("UNCHECKED_CAST")
    inline val right: R
        get() = if (leftOrRight.isRight) leftOrRight.value as R else
            throw NoSuchElementException("projection is Left")

    /**
     * Returns the value from this Left or the given argument if this is a Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun leftOrElse(onRight: (R) -> L): L {
        contract { callsInPlace(onRight, InvocationKind.AT_MOST_ONCE) }
        if (leftOrRight.isLeft) return leftOrRight.value as L
        return onRight(leftOrRight.value as R)
    }

    /**
     * Returns the value from this Right or the given argument if this is a Left
     */
    @Suppress("UNCHECKED_CAST")
    inline fun rightOrElse(onLeft: (L) -> R): R {
        contract { callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE) }
        if (leftOrRight.isRight) return leftOrRight.value as R
        return onLeft(leftOrRight.value as L)
    }

    /**
     * Run a [block] if this is a Left
     */
    @Suppress("UNCHECKED_CAST")
    inline fun left(block: (L) -> Unit) {
        contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
        if (leftOrRight.isLeft) block(leftOrRight.value as L)
    }

    /**
     * Run a [block] if this is a Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun right(block: (R) -> Unit) {
        contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
        if (leftOrRight.isRight) block(leftOrRight.value as R)
    }

    /**
     * Applies [onLeft] if this is a Left or [onRight] if this is a Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <T> fold(onLeft: (L) -> T, onRight: (R) -> T): T {
        contract {
            callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE)
            callsInPlace(onRight, InvocationKind.AT_MOST_ONCE)
        }
        return if (leftOrRight.isLeft) onLeft(leftOrRight.value as L) else
            onRight(leftOrRight.value as R)
    }

    override fun toString(): String = leftOrRight.toString()

    @PublishedApi
    internal class LeftOrRight(val value: Any?, val isLeft: Boolean) {
        inline val isRight get() = !isLeft

        override fun equals(other: Any?): Boolean {
            if (other == null) return false

            if (this === other) return true

            if (other !is LeftOrRight) return false

            if (value != other) return false
            if (isLeft != other.isLeft) return false
            return true
        }

        override fun hashCode(): Int {
            var result = value?.hashCode() ?: 0
            result = 31 * result + isLeft.hashCode()
            return result
        }

        override fun toString(): String = if (isLeft) "Left(l=$value)" else "Right(r=$value)"
    }
}

/**
 * @see Either.Companion.left
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <L, R> L.asLeft(): Either<L, R> = Either.left(this)

/**
 * @see Either.Companion.right
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <L, R> R.asRight(): Either<L, R> = Either.right(this)
