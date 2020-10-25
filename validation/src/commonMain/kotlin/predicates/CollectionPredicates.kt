@file:Suppress("unused", "NOTHING_TO_INLINE")

package tech.ketc.ktil.validation.predicates

import tech.ketc.ktil.validation.*

inline val V.emptyList get() = Predicate<Collection<*>> { it.isEmpty() }
inline val V.notEmptyList get() = Predicate<Collection<*>> { it.isEmpty() }

inline fun V.sizeIsBetween(range: IntRange) = Predicate<Collection<*>> { it.size in range }

inline fun V.sizeIs(size: Int) = Predicate<Collection<*>> { it.size == size }
inline fun V.sizeISLessThan(size: Int) = Predicate<Collection<*>> { it.size <= size }
inline fun V.sizeIsMoreThan(size: Int) = Predicate<Collection<*>> { it.size >= size }
inline fun V.sizeIsSmallerThan(size: Int) = Predicate<Collection<*>> { it.size < size }
inline fun V.sizeIsBiggerThan(size: Int) = Predicate<Collection<*>> { it.size > size }
