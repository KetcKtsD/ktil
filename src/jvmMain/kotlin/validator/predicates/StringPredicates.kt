@file:Suppress("unused", "NOTHING_TO_INLINE")

package tech.ketc.util.validator.predicates

import tech.ketc.util.*
import tech.ketc.util.validator.*


class EmailPredicate : Predicate<String> {
    companion object {
        private val emailRegex =
            "^[a-zA-Z0-9.!#\$%&'*+\\\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$".toRegex()
    }

    override fun validate(value: String): Boolean = value.matches(emailRegex)
}

inline val V.emptyString get() = Predicate<String> { it.strictLength == 0 }
inline val V.notEmptyString get() = Predicate<String> { it.strictLength > 0 }
inline val V.blankString get() = Predicate<String> { it.isBlank() }
inline val V.notBlankString get() = Predicate<String> { it.isNotBlank() }

inline val V.email get() = EmailPredicate()

inline fun V.lengthIsBetween(range: IntRange) = Predicate<String> { it.strictLength in range }

inline fun V.lengthIs(length: Int) = Predicate<String> { it.strictLength == length }
inline fun V.lengthISLessThan(length: Int) = Predicate<String> { it.strictLength <= length }
inline fun V.lengthISMoreThan(length: Int) = Predicate<String> { it.strictLength >= length }
inline fun V.lengthIsSmallerThan(length: Int) = Predicate<String> { it.strictLength < length }
inline fun V.lengthIsBiggerThan(length: Int) = Predicate<String> { it.strictLength > length }

inline fun V.matches(regex: Regex) = Predicate<String> { it.matches(regex) }
inline fun V.contains(regex: Regex) = Predicate<String> { it.contains(regex) }
