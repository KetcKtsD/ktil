package tech.ketc.util.validator

import tech.ketc.util.*
import kotlin.reflect.*

typealias ValidationResult<T> = Either<List<ValidationError>, out T>

/**
 * ValidationError
 *
 * @property property [KProperty1]
 * @property value Error value
 * @property message Error message
 */
data class ValidationError(
    val property: KProperty1<Any, Any?>?,
    val value: Any?,
    val message: String
)
