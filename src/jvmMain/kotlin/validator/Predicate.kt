package tech.ketc.util.validator

/**
 * The actual Validation process
 */
fun interface Predicate<in P : Any> {
    /**
     * Returns True if the verification is successful
     *
     * @param value Value to be verified
     */
    fun validate(value: P): Boolean
}
