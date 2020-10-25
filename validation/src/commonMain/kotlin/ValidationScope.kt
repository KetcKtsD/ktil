package tech.ketc.ktil.validation

import kotlin.reflect.*

/**
 * ValidationScope
 */
interface ValidationScope<T : Any> {
    /**
     * Set conditions for receiver validation
     */
    infix fun <P : Any> KProperty1<T, P?>.shouldBe(predicate: Predicate<P>): PredicateHolder

    /**
     * Set the [Validator] to validate the receiver
     */
    infix fun <P : Any> KProperty1<T, P?>.validateBy(validator: Validator<in P>)

    /**
     * Set conditions for receiver validation
     */
    infix fun KClass<T>.shouldBe(predicate: Predicate<T>): PredicateHolder

    /**
     * Set the [Validator] to validate the receiver
     */
    infix fun KClass<T>.validateBy(validator: Validator<in T>)

    /**
     *  Set conditions for receiver item validation
     */
    infix fun <P : Any> KProperty1<T, Collection<P?>?>.shouldBeEach(predicate: Predicate<P>): PredicateHolder

    /**
     * Set the [Validator] to validate the receiver item
     */
    infix fun <P : Any> KProperty1<T, Collection<P?>?>.validateByEach(validator: Validator<in P>)
}
