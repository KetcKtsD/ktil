package tech.ketc.ktil

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
                assertTrue(assertFails { either.right } is NoSuchElementException)
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
                assertTrue(assertFails { either.left } is NoSuchElementException)
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

        describe("equals") {
            context("LeftとRightの比較") {
                it("同じ値を格納していてもfalseを返す") {
                    val any = Any()
                    val either1: Either<Any, Any> = any.asLeft()
                    val either2: Either<Any, Any> = any.asRight()
                    assertNotEquals(either1, either2)
                }

                it("違う値を格納していればfalseを返す") {
                    val either1: Either<Any, Any> = Any().asLeft()
                    val either2: Either<Any, Any> = Any().asRight()
                    assertNotEquals(either1, either2)
                }
            }

            context("Left同士の比較") {
                it("同じ値を格納していればtrueを返す") {
                    val any = Any()
                    val either1: Either<Any, Any> = any.asLeft()
                    val either2: Either<Any, Any> = any.asLeft()
                    assertEquals(either1, either2)
                }

                it("違う値を格納していればfalseを返す") {
                    val either1: Either<Any, Any> = Any().asLeft()
                    val either2: Either<Any, Any> = Any().asLeft()
                    assertNotEquals(either1, either2)
                }
            }

            context("Right同士の比較") {
                it("同じ値を格納していればtrueを返す") {
                    val any = Any()
                    val either1: Either<Any, Any> = any.asRight()
                    val either2: Either<Any, Any> = any.asRight()
                    assertEquals(either1, either2)
                }

                it("違う値を格納していればfalseを返す") {
                    val either1: Either<Any, Any> = Any().asRight()
                    val either2: Either<Any, Any> = Any().asRight()
                    assertNotEquals(either1, either2)
                }
            }
        }
    }

    describe("ext functions") {
        it("mergeLeftは値を変更しない") {
            val left: Either<Left, ExtLeft> = Left().asLeft()
            assertSame(left.left, left.mergeLeft())
            val right: Either<Left, ExtLeft> = ExtLeft().asRight()
            assertSame(right.right, right.mergeLeft())
        }

        it("mergeRightは値を変更しない") {
            val left: Either<ExtRight, Right> = Right().asRight()
            assertSame(left.right, left.mergeRight())
            val right: Either<ExtRight, Right> = ExtRight().asLeft()
            assertSame(right.left, right.mergeRight())
        }

        it("joinLeftはLeftに値をJoinする") {
            val leftLeft: Either<Int, String> = 1.asLeft()
            val leftRight: Either<Int, String> = "leftRight".asRight()

            val eitherLeftLeft: Either<Either<Int, String>, String> = leftLeft.asLeft()
            assertSame(leftLeft.left, eitherLeftLeft.joinLeft().left)

            val eitherLeftRight: Either<Either<Int, String>, String> = leftRight.asLeft()
            assertSame(leftRight.right, eitherLeftRight.joinLeft().right)

            val eitherRight: Either<Either<Int, String>, String> = "eitherRight".asRight()
            assertSame(eitherRight.right, eitherRight.joinLeft().right)
        }

        it("joinRightはRightに値をJoinする") {
            val rightLeft: Either<Int, String> = 1.asLeft()
            val rightRight: Either<Int, String> = "rightRight".asRight()

            val eitherRightLeft: Either<Int, Either<Int, String>> = rightLeft.asRight()
            assertSame(rightLeft.left, eitherRightLeft.joinRight().left)

            val eitherRightRight: Either<Int, Either<Int, String>> = rightRight.asRight()
            assertSame(rightRight.right, eitherRightRight.joinRight().right)

            val eitherLeft: Either<Int, Either<Int, String>> = 2.asLeft()
            assertSame(eitherLeft.left, eitherLeft.joinRight().left)
        }
    }

    describe("default right functions") {
        it("mergeは値を変更しない") {
            val left: Either<ExtRight, Right> = Right().asRight()
            assertSame(left.right, left.merge())
            val right: Either<ExtRight, Right> = ExtRight().asLeft()
            assertSame(right.left, right.merge())
        }

        context("EitherがLeftを持つ場合") {
            val left by memoized { Left() }
            val either: Either<Left, Right> by memoized { Either.left(left) }

            it("getはNoSuchElementExceptionを投げる") {
                assertTrue(assertFails { either.get() } is NoSuchElementException)
            }

            it("getOrElseは与えられた関数を実行してRightの値を返す") {
                val right = Right()
                assertSame(right, either.getOrElse { right })
            }

            it("orElseは与えられた関数を実行して新たなEitherを返す") {
                val right = Right()
                assertSame(right, either.orElse { right.asRight() }.right)
                val newLeft = object : Left() {}
                assertSame(newLeft, either.orElse { newLeft.asLeft() }.left)
            }

            it("mapは与えられた関数を実行せずLeftを返す") {
                var called = false
                val result = either.map { called = true; Any() }
                assertFalse(called)
                assertTrue(result.isLeft)
                assertSame(left, result.left)
            }

            it("flatMapは与えられた関数を実行せずLeftを返す") {
                val newLeft: Either<ExtLeft, Right> = ExtLeft().asLeft()
                assertSame(left, either.flatMap { newLeft }.left)
            }
        }

        context("EitherがRightを持つ場合") {
            val right by memoized { Right() }
            val either: Either<Left, Right> by memoized { Either.right(right) }

            it("getはRightを返す") { assertSame(right, either.get()) }

            it("getOrElseは与えられた関数を実行せずにLeftを返す") {
                assertSame(right, either.getOrElse { Right() })
            }

            it("orElseは与えられた関数を実行せずにRightを返す") {
                assertSame(right, either.orElse { Right().asRight() }.right)
            }

            it("mapは与えられた関数を実行しRightを返す") {
                val any = Any()
                val result = either.map { any }
                assertTrue(result.isRight)
                assertSame(any, result.right)
            }

            it("flatMapは与えられた関数を実行して新たなEitherを返す") {
                val newRight = 1
                assertSame(newRight, either.flatMap { newRight.asRight() }.right)
                val newEither: Either<ExtLeft, Int> = ExtLeft().asLeft()
                assertSame(newEither.left, either.flatMap { newEither }.left)
            }
        }
    }

    describe("Either.Companion") {
        it("rightはRightを返す") { assertTrue(Either.right<Int, Int>(1).isRight) }

        it("leftはLeftを返す") { assertTrue(Either.left<Int, Int>(1).isLeft) }

        describe("cond") {
            it("conditionがtrueの場合はRightを返す") { assertTrue(Either.cond(true, 1, 1).isRight) }

            it("conditionがfalseの場合はLeftを返す") { assertTrue(Either.cond(false, 1, 1).isLeft) }
        }
    }
})
