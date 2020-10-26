package tech.ketc.ktil.validation

import tech.ketc.ktil.*
import kotlin.reflect.*
import kotlin.collections.*

/**
 * Validator
 */
abstract class Validator<T : Any>(initializer: ValidationScope<T>.() -> Unit) {
    private val _scope = ValidationScopeImpl<T>().apply(initializer)
    private val scope get() = _scope as ValidationScopeImpl<*>

    /**
     * Returns right on successful validation
     */
    fun validate(target: T): ValidationResult<T> = _scope.validate(target)

    private class ValidationScopeImpl<T : Any> : ValidationScope<T> {
        private val validations = LinkedList<Validation>()

        @Suppress("UNCHECKED_CAST")
        override fun <P : Any> KProperty1<T, P?>.shouldBe(predicate: Predicate<P>): PredicateHolder {
            val item = Validation.ItemValidation(
                this as KProperty1<Any, Any?>,
                predicate as Predicate<Any>
            )
            validations.add(item)
            return item
        }

        @Suppress("UNCHECKED_CAST")
        override fun <P : Any> KProperty1<T, P?>.validateBy(validator: Validator<in P>) {
            val composite = Validation.CompositeValidation(
                this as KProperty1<Any, Any?>,
                validator.scope as ValidationScopeImpl<Any>
            )
            validations.add(composite)
        }

        @Suppress("UNCHECKED_CAST")
        override fun KClass<T>.shouldBe(predicate: Predicate<T>): PredicateHolder {
            val item = Validation.ClassItemValidation(predicate as Predicate<Any>)
            validations.add(item)
            return item
        }

        @Suppress("UNCHECKED_CAST")
        override fun KClass<T>.validateBy(validator: Validator<in T>) {
            val composite = Validation.ClassCompositeValidation(
                validator.scope as ValidationScopeImpl<Any>
            )
            validations.add(composite)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <P : Any> KProperty1<T, Collection<P?>?>.shouldBeEach(predicate: Predicate<P>): PredicateHolder {
            val item = Validation.CollectionItemValidation(
                this as KProperty1<Any, Collection<Any?>?>,
                predicate as Predicate<Any>
            )
            validations.add(item)
            return item
        }

        @Suppress("UNCHECKED_CAST")
        override fun <P : Any> KProperty1<T, Collection<P?>?>.validateByEach(validator: Validator<in P>) {
            val composite = Validation.CollectionCompositeValidation(
                this as KProperty1<Any, Collection<Any?>?>,
                validator.scope as ValidationScopeImpl<Any>
            )
            validations.add(composite)
        }

        fun validate(target: T): ValidationResult<T> {
            val errors = LinkedList<ValidationError>()
            for (validation in validations) {
                when (validation) {
                    is Validation.ItemValidation -> {
                        val value = validation.property.get(target as Any) ?: continue
                        if (validation.predicate.validate(value)) continue
                        val info = validation.run { ValidationError(property, value, errorMessage) }
                        errors.add(info)
                    }
                    is Validation.ClassItemValidation -> {
                        if (validation.predicate.validate(target)) continue
                        val info = validation.run { ValidationError(null, target, errorMessage) }
                        errors.add(info)
                    }
                    is Validation.CollectionItemValidation -> {
                        val collection = validation.property.get(target as Any)
                        collection ?: continue
                        for (item in collection) {
                            item ?: continue
                            if (!validation.predicate.validate(item)) {
                                val error = validation.run {
                                    ValidationError(property, collection, errorMessage)
                                }
                                errors.add(error)
                                break
                            }
                        }
                    }
                    is Validation.CompositeValidation -> {
                        val value = validation.property.get(target as Any) ?: continue
                        val result = validation.scope.validate(value)
                        if (result.isRight) continue
                        errors.addAll(result.left)
                    }
                    is Validation.ClassCompositeValidation -> {
                        val result = validation.scope.validate(target)
                        if (result.isRight) continue
                        errors.addAll(result.left)
                    }
                    is Validation.CollectionCompositeValidation -> {
                        val collection = validation.property.get(target as Any)
                        collection ?: continue
                        for (item in collection) {
                            item ?: continue
                            val result = validation.scope.validate(item)
                            if (result.isLeft) {
                                errors.addAll(result.left)
                                break
                            }
                        }
                    }
                }
            }
            return if (errors.isEmpty()) target.asRight() else errors.asLeft()
        }
    }

    private sealed class Validation {
        companion object {
            private const val DEFAULT_ERROR_MESSAGE = "validation error"
        }

        class ItemValidation(
            val property: KProperty1<Any, Any?>,
            val predicate: Predicate<Any>
        ) : Validation(), PredicateHolder {
            var errorMessage: String = DEFAULT_ERROR_MESSAGE

            override fun otherwise(errorMessage: String) {
                this.errorMessage = errorMessage
            }
        }

        class ClassItemValidation(
            val predicate: Predicate<Any>
        ) : Validation(), PredicateHolder {
            var errorMessage: String = DEFAULT_ERROR_MESSAGE
            override fun otherwise(errorMessage: String) {
                this.errorMessage = errorMessage
            }
        }

        class CollectionItemValidation(
            val property: KProperty1<Any, Collection<Any?>?>,
            val predicate: Predicate<Any>,
        ) : Validation(), PredicateHolder {
            var errorMessage: String = DEFAULT_ERROR_MESSAGE

            override fun otherwise(errorMessage: String) {
                this.errorMessage = errorMessage
            }
        }

        class CompositeValidation(
            val property: KProperty1<Any, Any?>,
            val scope: ValidationScopeImpl<Any>,
        ) : Validation()

        class ClassCompositeValidation(
            val scope: ValidationScopeImpl<Any>,
        ) : Validation()

        class CollectionCompositeValidation(
            val property: KProperty1<Any, Collection<Any?>?>,
            val scope: ValidationScopeImpl<Any>,
        ) : Validation()
    }
}

private class ValidatorInternal<T : Any>(
    initializer: ValidationScope<T>.() -> Unit
) : Validator<T>(initializer)

fun <T : Any> validator(
    initializer: ValidationScope<T>.() -> Unit
): Validator<T> = ValidatorInternal(initializer)
