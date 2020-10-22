package tech.ketc.util.validator

/**
 * PredicateHolder
 */
interface PredicateHolder {
    /**
     * Set an error message if you choose to validate
     */
    infix fun otherwise(errorMessage: String)
}
