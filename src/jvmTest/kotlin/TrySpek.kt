package tech.ketc.util

import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*
import kotlin.test.*

class TrySpek : Spek({
    describe("doTry(relaxed,block)") {
        context("RelaxedErrorMapperがLeftを返す状態") {
            val relaxed = RelaxedErrorMapper { it.asLeft<Throwable, Int>() }

            it("blockが例外を投げなければRightが返る") {
                val either = doTry(relaxed) { 1 + 1 }
                assertTrue(either.isRight)
                assertEquals(2, either.right)
            }

            it("blockが例外を投げるとLeft(Left)が返る") {
                val e = RuntimeException()
                val either = doTry(relaxed, e::raise)
                assertTrue(either.isLeft)
                assertTrue(either.left.isLeft)
                assertSame(e, either.left.left)
            }
        }

        context("RelaxedErrorMapperがRightを返すとき") {
            val error by memoized { Any() }
            val relaxed = RelaxedErrorMapper { error.asRight() }

            it("blockが例外を投げなければRightが返る") {
                val either = doTry(relaxed) { 1 + 1 }
                assertTrue(either.isRight)
                assertEquals(2, either.right)
            }

            it("blockが例外を投げるとLeft(Right)が返る") {
                val either = doTry(relaxed, RuntimeException()::raise)
                assertTrue(either.isLeft)
                assertTrue(either.left.isRight)
                assertSame(error, either.left.right)
            }
        }
    }

    describe("doTry(strict,block)") {
        val error by memoized { Any() }
        val strict = StrictThrowableMapper { error }

        it("blockが例外を投げなければRightが返る") {
            val either = doTry(strict) { 1 + 1 }
            assertTrue(either.isRight)
            assertEquals(2, either.right)
        }

        it("blockが例外を投げるとLeftが返る") {
            val either = doTry(strict, RuntimeException()::raise)
            assertTrue(either.isLeft)
            assertSame(error, either.left)
        }
    }

    describe("doTry(block)") {
        it("blockが例外を投げなければRightが返る") {
            val either = doTry { 1 + 1 }
            assertTrue(either.isRight)
            assertEquals(2, either.right)
        }

        it("blockが例外を投げるとLeftが返る") {
            val e = RuntimeException()
            val either = doTry(e::raise)
            assertTrue(either.isLeft)
            assertSame(e, either.left)
        }
    }
})
