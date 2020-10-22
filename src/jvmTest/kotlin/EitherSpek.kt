package tech.ketc.util

import org.junit.jupiter.api.*
import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*
import kotlin.test.*

class EitherSpek : Spek({
    class Left; class Right
    describe("Either Spek") {
        context("EitherがLeftを持つ場合") {
            val left by memoized { Left() }
            val either: Either<Left, Right> by memoized { Either.left(left) }

            it("isLeftはtrueを返す") { assertTrue(either.isLeft) }

            it("isRightはfalseを返す") { assertFalse(either.isRight) }

            it("leftはLeftを返す") { assertSame(left, either.left) }

            it("rightはNoSuchElementExceptionを投げる") {
                assertThrows<NoSuchElementException> { either.right }
            }

            it("leftOrElseは与えられた関数を実行せずにLeftを返す") {
                assertSame(left, either.leftOrElse { Left() })
            }

            it("rightOrElseは与えられた関数を実行してRightを返す") {
                val right = Right()
                assertSame(right, either.rightOrElse { right })
            }

            it("leftは与えられた関数を実行する") {
                var called = false
                either.left { called = true }
                assertTrue(called)
            }

            it("rightは与えられた関数を実行しない") {
                var called = false
                either.right { called = true }
                assertFalse(called)
            }

            it("foldはonLeftを実行しonRightを実行しない") {
                var callOnLeft = false
                val onLeft = Any()
                var callOnRight = false
                val onRight = Any()

                val result = either.fold(
                    onLeft = { callOnLeft = true; onLeft },
                    onRight = { callOnRight = true; onRight }
                )
                assertTrue(callOnLeft)
                assertFalse(callOnRight)
                assertSame(onLeft, result)
                assertNotSame(onRight, result)
            }
        }

        context("EitherがRightを持つ場合") {
            val right by memoized { Right() }
            val either: Either<Left, Right> by memoized { Either.right(right) }

            it("isLeftはfalseを返す") { assertFalse(either.isLeft) }

            it("isRightはtrueを返す") { assertTrue(either.isRight) }

            it("leftはNoSuchElementExceptionを投げる") {
                assertThrows<NoSuchElementException> { either.left }
            }

            it("rightはRightを返す") { assertSame(right, either.right) }

            it("leftOrElseは与えられた関数を実行してLeftを返す") {
                val left = Left()
                assertSame(left, either.leftOrElse { left })
            }

            it("rightOrElseは与えられた関数を実行せずにLeftを返す") {
                assertSame(right, either.rightOrElse { Right() })
            }

            it("leftは与えられた関数を実行しない") {
                var called = false
                either.left { called = true }
                assertFalse(called)
            }

            it("rightは与えられた関数を実行する") {
                var called = false
                either.right { called = true }
                assertTrue(called)
            }

            it("foldはonLeftを実行せず､onRightを実行する") {
                var callOnLeft = false
                val onLeft = Any()
                var callOnRight = false
                val onRight = Any()

                val result = either.fold(
                    onLeft = { callOnLeft = true; onLeft },
                    onRight = { callOnRight = true; onRight }
                )
                assertFalse(callOnLeft)
                assertTrue(callOnRight)
                assertNotSame(onLeft, result)
                assertSame(onRight, result)
            }
        }
    }
})