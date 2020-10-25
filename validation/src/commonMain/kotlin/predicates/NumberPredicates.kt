@file:Suppress("unused", "NOTHING_TO_INLINE")

package tech.ketc.ktil.validation.predicates

import tech.ketc.ktil.validation.*

inline val V.positiveNumber get() = Predicate<Number> { it.toDouble() > 0.0 }
inline val V.negativeNumber get() = Predicate<Number> { it.toDouble() < 0.0 }
inline val V.zero get() = Predicate<Number> { it.toDouble() == 0.0 }
inline val V.lessThanZero get() = Predicate<Number> { it.toDouble() <= 0.0 }
inline val V.moreThanZero get() = Predicate<Number> { it.toDouble() >= 0.0 }

inline fun V.between(range: IntRange) = Predicate<Number> { it.toInt() in range }
inline fun V.between(range: CharRange) = Predicate<Number> { it.toChar() in range }
inline fun V.between(range: LongRange) = Predicate<Number> { it.toLong() in range }

inline fun V.eq(value: Number) = Predicate<Number> { it.toDouble() == value.toDouble() }
inline fun V.lessThan(value: Number) = Predicate<Number> { it.toDouble() <= value.toDouble() }
inline fun V.moreThan(value: Number) = Predicate<Number> { it.toDouble() >= value.toDouble() }
inline fun V.smallerThan(value: Number) = Predicate<Number> { it.toDouble() < value.toDouble() }
inline fun V.biggerThan(value: Number) = Predicate<Number> { it.toDouble() > value.toDouble() }
