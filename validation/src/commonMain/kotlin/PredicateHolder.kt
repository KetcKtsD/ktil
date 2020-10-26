package tech.ketc.ktil.validation

/**
 * PredicateHolder
 */
interface PredicateHolder {
    /**
     * Set an error message if you choose to validate
     */
    infix fun otherwise(errorMessage: String)
}
