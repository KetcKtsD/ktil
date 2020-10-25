package tech.ketc.ktil.validation

internal actual class LinkedList<E> private constructor(
    private val list: java.util.LinkedList<E>
) : MutableList<E> by list {
    actual constructor() : this(java.util.LinkedList())
    actual constructor(elements: Collection<E>) : this(java.util.LinkedList(elements))
}
