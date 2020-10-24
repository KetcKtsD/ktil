package tech.ketc.util

import org.junit.jupiter.api.*
import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*
import kotlin.test.*

class EitherSpek : Spek({
    open class Left; open class Right; class ExtLeft : Left(); class ExtRight : Right()
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

            it("getLeftOrElseは与えられた関数を実行せずにLeftの値を返す") {
                assertSame(left, either.getLeftOrElse { Left() })
            }

            it("getRightOrElseは与えられた関数を実行してRightの値を返す") {
                val right = Right()
                assertSame(right, either.getRightOrElse { right })
            }

            it("leftOrElseは与えられた関数を実行せずにLeftを返す") {
                assertSame(left, either.leftOrElse { Left().asLeft() }.left)
            }

            it("rightOrElseは与えられた関数を実行して新たなEitherを返す") {
                val right = Right()
                assertSame(right, either.rightOrElse { right.asRight() }.right)
                val newLeft = object : Left() {}
                assertSame(newLeft, either.rightOrElse { newLeft.asLeft() }.left)
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

            it("leftMapは与えられた関数を実行しLeftを返す") {
                val any = Any()
                val result = either.leftMap { any }
                assertTrue(result.isLeft)
                assertSame(any, result.left)
            }

            it("rightMapは与えられた関数を実行せずLeftを返す") {
                var called = false
                val result = either.rightMap { called = true; Any() }
                assertFalse(called)
                assertTrue(result.isLeft)
                assertSame(left, result.left)
            }

            it("leftFlatMapは与えられた関数を実行して新たなEitherを返す") {
                val newLeft = 1
                assertSame(newLeft, either.leftFlatMap { newLeft.asLeft() }.left)
                val newEither: Either<Int, ExtRight> = ExtRight().asRight()
                assertSame(newEither.right, either.leftFlatMap { newEither }.right)
            }

            it("rightFlatMapは与えられた関数を実行せずLeftを返す") {
                val newLeft: Either<ExtLeft, Right> = ExtLeft().asLeft()
                assertSame(left, either.rightFlatMap { newLeft }.left)
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

            it("swapはLeftをRightにもってくる") {
                val result = either.swap()
                assertTrue(result.isRight)
                assertSame(left, result.right)
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

            it("getLeftOrElseは与えられた関数を実行してLeftを返す") {
                val left = Left()
                assertSame(left, either.getLeftOrElse { left })
            }

            it("getRightOrElseは与えられた関数を実行せずにLeftを返す") {
                assertSame(right, either.getRightOrElse { Right() })
            }

            it("leftOrElseは与えられた関数を実行して新たなEitherを返す") {
                val left = Left()
                assertSame(left, either.leftOrElse { left.asLeft() }.left)
                val newRight = object : Right() {}
                assertSame(newRight, either.leftOrElse { newRight.asRight() }.right)
            }

            it("rightOrElseは与えられた関数を実行せずにRightを返す") {
                assertSame(right, either.rightOrElse { Right().asRight() }.right)
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

            it("leftMapは与えられた関数を実行せずRightを返す") {
                var called = false
                val result = either.leftMap { called = true; Any() }
                assertFalse(called)
                assertTrue(result.isRight)
                assertSame(right, result.right)
            }

            it("rightMapは与えられた関数を実行しRightを返す") {
                val any = Any()
                val result = either.rightMap { any }
                assertTrue(result.isRight)
                assertSame(any, result.right)
            }

            it("leftFlatMapは与えられた関数を実行せずRightを返す") {
                val newRight: Either<Left, ExtRight> = ExtRight().asRight()
                assertSame(right, either.leftFlatMap { newRight }.right)
            }

            it("rightFlatMapは与えられた関数を実行して新たなEitherを返す") {
                val newRight = 1
                assertSame(newRight, either.rightFlatMap { newRight.asRight() }.right)
                val newEither: Either<ExtLeft, Int> = ExtLeft().asLeft()
                assertSame(newEither.left, either.rightFlatMap { newEither }.left)
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

            it("swapはRightをLeftにもってくる") {
                val result = either.swap()
                assertTrue(result.isLeft)
                assertSame(right, result.left)
            }
        }
    }
})
