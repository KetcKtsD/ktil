@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ExperimentalContracts::class)

package tech.ketc.ktil

import kotlin.contracts.*

/**
 * Simple Either type
 */
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
     * Return this value of Left, or result of [onRight] if this is a Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <L1 : L> getLeftOrElse(onRight: (R) -> L1): L {
        contract { callsInPlace(onRight, InvocationKind.AT_MOST_ONCE) }
        if (leftOrRight.isLeft) return leftOrRight.value as L
        return onRight(leftOrRight.value as R)
    }

    /**
     * Return this value of Right or result of [onLeft] if this is a Left
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <R1 : R> getRightOrElse(onLeft: (L) -> R1): R {
        contract { callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE) }
        if (leftOrRight.isRight) return leftOrRight.value as R
        return onLeft(leftOrRight.value as L)
    }

    /**
     * Return this Left or result of [onRight] if this is a Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <L1 : L, R1 : R> leftOrElse(onRight: (R) -> Either<L1, R1>): Either<L, R> {
        contract { callsInPlace(onRight, InvocationKind.AT_MOST_ONCE) }
        return if (leftOrRight.isRight) onRight(leftOrRight.value as R) as Either<L, R> else this
    }

    /**
     * Return this Right or result of [onLeft] if this is a Left
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <L1 : L, R1 : R> rightOrElse(onLeft: (L) -> Either<L1, R1>): Either<L, R> {
        contract { callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE) }
        return if (leftOrRight.isLeft) onLeft(leftOrRight.value as L) as Either<L, R> else this
    }

    /**
     * Run a [onLeft] if this is a Left
     */
    @Suppress("UNCHECKED_CAST")
    inline fun left(onLeft: (L) -> Unit) {
        contract { callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE) }
        if (leftOrRight.isLeft) onLeft(leftOrRight.value as L)
    }

    /**
     * Run a [onRight] if this is a Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun right(onRight: (R) -> Unit) {
        contract { callsInPlace(onRight, InvocationKind.AT_MOST_ONCE) }
        if (leftOrRight.isRight) onRight(leftOrRight.value as R)
    }

    /**
     * Maps the result of [onLeft] to Left
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <L1> leftMap(onLeft: (L) -> L1): Either<L1, R> {
        contract { callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE) }
        return if (leftOrRight.isLeft) left(onLeft(leftOrRight.value as L)) else right(leftOrRight.value as R)
    }

    /**
     * Maps the result of [onRight] to Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <R1> rightMap(onRight: (R) -> R1): Either<L, R1> {
        contract { callsInPlace(onRight, InvocationKind.AT_MOST_ONCE) }
        return if (leftOrRight.isRight) right(onRight(leftOrRight.value as R)) else left(leftOrRight.value as L)
    }

    /**
     * Binds the result of [onLeft] Left
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <L1, R1 : R> leftFlatMap(onLeft: (L) -> Either<L1, R1>): Either<L1, R> {
        contract { callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE) }
        return if (leftOrRight.isLeft) return onLeft(leftOrRight.value as L) as Either<L1, R> else right(leftOrRight.value as R)
    }

    /**
     * Binds the result of [onRight] Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <L1 : L, R1> rightFlatMap(onRight: (R) -> Either<L1, R1>): Either<L, R1> {
        contract { callsInPlace(onRight, InvocationKind.AT_MOST_ONCE) }
        return if (leftOrRight.isRight) return onRight(leftOrRight.value as R) as Either<L, R1> else left(leftOrRight.value as L)
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

    /**
     * Swap Left and Right
     */
    @Suppress("UNCHECKED_CAST")
    inline fun swap(): Either<R, L> {
        return if (leftOrRight.isLeft) right(leftOrRight.value as L) else left(leftOrRight.value as R)
    }

    override fun toString(): String = leftOrRight.toString()

    @PublishedApi
    internal class LeftOrRight(val value: Any?, val isLeft: Boolean) {
        inline val isRight get() = !isLeft

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (this === other) return true
            if (this::class != other::class) return false

            other as LeftOrRight

            if (isLeft != other.isLeft) return false
            if (value != other.value) return false
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

//ext functions

@Suppress("UNCHECKED_CAST")
inline fun <L, R : L> Either<L, R>.mergeLeft(): L = leftOrRight.value as L

@Suppress("UNCHECKED_CAST")
inline fun <L : R, R> Either<L, R>.mergeRight(): R = leftOrRight.value as R

@Suppress("UNCHECKED_CAST")
inline fun <L : Either<L1, R1>, R, L1, R1 : R> Either<L, R>.joinLeft(): Either<L1, R> {
    return if (leftOrRight.isRight) Either.right(leftOrRight.value as R) else leftOrRight.value as Either<L1, R>
}

@Suppress("UNCHECKED_CAST")
inline fun <L, R : Either<L1, R1>, L1 : L, R1> Either<L, R>.joinRight(): Either<L, R1> {
    return if (leftOrRight.isLeft) Either.left(leftOrRight.value as L) else leftOrRight.value as Either<L, R1>
}

//default right functions

/**
 * Return this value of Right or result of [onLeft] if this is a Left
 *
 * @see Either.getRightOrElse
 */
inline fun <L, R, R1 : R> Either<L, R>.getOrElse(onLeft: (L) -> R1): R {
    contract { callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE) }
    return getRightOrElse(onLeft)
}

/**
 * Return this Right or result of [onLeft] if this is a Left
 *
 * @see Either.rightOrElse
 */
inline fun <L, R, L1 : L, R1 : R> Either<L, R>.orElse(onLeft: (L) -> Either<L1, R1>): Either<L, R> {
    contract { callsInPlace(onLeft, InvocationKind.AT_MOST_ONCE) }
    return rightOrElse(onLeft)
}

/**
 * Maps the result of [onRight] to Right
 *
 * @see Either.rightMap
 */
inline fun <L, R, R1> Either<L, R>.map(onRight: (R) -> R1): Either<L, R1> {
    contract { callsInPlace(onRight, InvocationKind.AT_MOST_ONCE) }
    return rightMap(onRight)
}

/**
 * Binds the result of [onRight] Right
 *
 * @see Either.rightFlatMap
 */
inline fun <L, R, L1 : L, R1> Either<L, R>.flatMap(onRight: (R) -> Either<L1, R1>): Either<L, R1> {
    contract { callsInPlace(onRight, InvocationKind.AT_MOST_ONCE) }
    return rightFlatMap(onRight)
}

/**
 * @see mergeRight
 */
inline fun <L : R, R> Either<L, R>.merge(): R = mergeRight()
