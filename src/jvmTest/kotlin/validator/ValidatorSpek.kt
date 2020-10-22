package tech.ketc.util.validator

import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*
import kotlin.test.*
import kotlin.text.*

class ValidatorSpek : Spek({
    class President(val name: String = "name")
    class Organization(val id: Int = 0, val president: President = President())
    class User(
        val id: Int = 0,
        val name: String = "name",
        val age: Int = 0,
        val organization: Organization = Organization()
    )

    describe("Validator Spek") {
        describe("validate()") {
            context("単体のバリデーション") {
                it("条件を満たしていればValidationOkが返る") {
                    val validator = validator<User> { User::id shouldBe { it > 0 } }
                    val result = validator.validate(User(1, "name", 1))
                    assertTrue(result.isRight)
                }

                it("条件を満たしていなければValidationErrorのリストが返る") {
                    val validator = validator<User> { User::id shouldBe { it < 0 } }
                    val result = validator.validate(User(0, "name", 1))

                    assertTrue(result.isLeft)
                    val errors = result.left
                    assertEquals(1, errors.size)

                    val error = errors.first()
                    assertEquals("validation error", error.message)
                    assertTrue(User::id == error.property)
                    assertEquals(0, error.value)
                }
            }

            context("複数のバリデーション") {
                it("条件を満たしていればValidationOkが返る") {
                    val validator = validator<User> {
                        User::id shouldBe { it > 2 }
                        User::name shouldBe { it == "name1" }
                        User::age shouldBe { it == 13 }
                    }
                    val result = validator.validate(User(3, "name1", 13))
                    assertTrue(result.isRight)
                }

                it("条件を満たしていなければValidationErrorのリストが返る") {
                    val validator = validator<User> {
                        User::id shouldBe { it < 0 }
                        User::name shouldBe { it == "name1" }
                        User::age shouldBe { it == 0 }
                    }
                    val result = validator.validate(User(3, "name2", 0))

                    assertTrue(result.isLeft)
                    val errors = result.left
                    assertEquals(2, errors.size)

                    val (first, second) = errors

                    assertEquals("validation error", first.message)
                    assertTrue(User::id == first.property)
                    assertEquals(3, first.value)

                    assertEquals("validation error", second.message)
                    assertTrue(User::name == second.property)
                    assertEquals("name2", second.value)
                }
            }

            context("ネストしたバリデーション") {
                it("条件を満たしていればValidationOkが返る") {
                    val validator = validator<User> {
                        User::id shouldBe { it >= 0 }
                        User::name shouldBe { it == "name" }
                        User::age shouldBe { it >= 0 }

                        User::organization validateBy validator {
                            Organization::id shouldBe { it >= 0 }

                            Organization::president validateBy validator {
                                President::name shouldBe { it == "name" }
                            }
                        }
                    }
                    val result = validator.validate(User())
                    assertTrue(result.isRight)
                }

                it("条件を満たしていなければValidationErrorのリストが返る") {
                    val validator = validator<User> {
                        User::id shouldBe { it >= 0 }
                        User::name shouldBe { it == "name" }
                        User::age shouldBe { it >= 0 }

                        User::organization validateBy validator {
                            Organization::id shouldBe { it >= 0 }

                            Organization::president validateBy validator {
                                President::name shouldBe { it == "name" }
                            }
                        }
                    }
                    val target = User(
                        id = -1,
                        name = "name1",
                        organization = Organization(
                            id = -1,
                            President(
                                name = "name1"
                            )
                        )
                    )
                    val result = validator.validate(target)

                    assertTrue(result.isLeft)
                    val errors = result.left
                    assertEquals(4, errors.size)

                    assertEquals("validation error", errors[0].message)
                    assertTrue(User::id == errors[0].property)
                    assertEquals(-1, errors[0].value)

                    assertEquals("validation error", errors[1].message)
                    assertTrue(User::name == errors[1].property)
                    assertEquals("name1", errors[1].value)

                    assertEquals("validation error", errors[2].message)
                    assertTrue(Organization::id == errors[2].property)
                    assertEquals(-1, errors[2].value)

                    assertEquals("validation error", errors[3].message)
                    assertTrue(President::name == errors[3].property)
                    assertEquals("name1", errors[3].value)
                }
            }
        }

        describe("otherwise()") {
            it("検証に失敗した場合､指定したエラーメッセージをセットする") {
                val validator = validator<User> {
                    User::id shouldBe { it > 0 } otherwise "user id must be a positive number"
                }

                val result = validator.validate(User(id = -1))
                assertTrue(result.isLeft)
                assertEquals("user id must be a positive number", result.left.first().message)
            }
        }

        describe("collection validation") {
            class Room(
                val numbers: List<Int> = (0..3).toList(),
                val users: List<User> = listOf(
                    User(id = 0),
                    User(id = 1),
                    User(id = 2),
                    User(id = 3),
                )
            )

            describe("shouldBeEach") {
                it("条件を満たしていればValidationOkが返る") {
                    val validator = validator<Room> { Room::numbers shouldBeEach { it >= 0 } }
                    val result = validator.validate(Room())
                    assertTrue(result.isRight)
                }

                it("条件を満たしていなければValidationErrorのリストが返る") {
                    val validator = validator<Room> { Room::numbers shouldBeEach { it != 2 } }
                    val result = validator.validate(Room())

                    assertTrue(result.isLeft)

                    val errors = result.left
                    assertEquals(1, errors.size)
                    val error = errors.first()
                    assertTrue(Room::numbers == error.property)
                    assertEquals("validation error", error.message)
                    assertEquals(Room().numbers, error.value)
                }
            }

            describe("validateByEach") {
                it("条件を満たしていればValidationOkが返る") {
                    val validator = validator<Room> {
                        Room::users validateByEach validator {
                            User::id shouldBe { it >= 0 }
                        }
                    }
                    val result = validator.validate(Room())
                    println(result)
                    assertTrue(result.isRight)
                }

                it("条件を満たしていなければValidationErrorのリストが返る") {
                    val validator = validator<Room> {
                        Room::users validateByEach validator {
                            User::id shouldBe { it != 2 }
                        }
                    }
                    val result = validator.validate(Room())

                    assertTrue(result.isLeft)

                    val errors = result.left
                    assertEquals(1, errors.size)
                    val error = errors.first()
                    assertTrue(User::id == error.property)
                    assertEquals("validation error", error.message)
                    assertEquals(2, error.value)
                }
            }
        }

        describe("class validation") {
            data class GrandChild(val name: String = "grand child")
            data class Child(val name: String = "child", val child: GrandChild = GrandChild())
            data class Parent(val name: String = "parent", val child: Child = Child())

            context("ネストしていないクラスに対するバリデーション") {
                it("条件を満たしていればValidationOkが返る") {
                    val validator = validator<Parent> {
                        Parent::class shouldBe { it.name == "parent" }
                        Parent::class validateBy validator {
                            Parent::name shouldBe { it.isNotEmpty() }
                        }
                    }
                    val result = validator.validate(Parent())
                    assertTrue(result.isRight)
                }

                it("条件を満たしていなければValidationErrorのリストが返る") {
                    val validator = validator<Parent> {
                        Parent::class shouldBe { it.name == "parent" }
                        Parent::class validateBy validator {
                            Parent::name shouldBe { it.isNotEmpty() }
                        }
                    }

                    val result = validator.validate(Parent(name = ""))
                    assertTrue(result.isLeft)

                    val errors = result.left
                    assertEquals(2, errors.size)

                    val (first, second) = errors

                    assertEquals(null, first.property)
                    assertEquals(Parent(name = ""), first.value)
                    assertEquals("validation error", first.message)

                    assertTrue(Parent::name == second.property)
                    assertEquals("", second.value)
                    assertEquals("validation error", second.message)
                }
            }

            context("ネストしたクラスに対するバリデーション") {
                it("条件を満たしていればValidationOkが返る") {
                    val validator = validator<Parent> {
                        Parent::class shouldBe { it.name == "parent" }
                        Parent::child validateBy validator {
                            Child::class shouldBe { it.name == "child" }
                        }
                    }
                    val result = validator.validate(Parent())
                    assertTrue(result.isRight)
                }

                it("条件を満たしていなければValidationErrorのリストが返る") {
                    val validator = validator<Parent> {
                        Parent::class shouldBe { it.name == "parent" }

                        Parent::child validateBy validator {
                            Child::class shouldBe { it.name == "child" }

                            Child::child validateBy validator {
                                GrandChild::class validateBy validator {
                                    GrandChild::name shouldBe { it.isBlank() } otherwise "must be empty"
                                }
                            }
                        }
                    }

                    val target = Parent(
                        name = "name1",
                        child = Child(
                            name = "name2",
                            child = GrandChild()
                        )
                    )
                    val result = validator.validate(target)
                    assertTrue(result.isLeft)

                    val errors = result.left
                    assertEquals(3, errors.size)

                    val (first, second, third) = errors

                    assertEquals(null, first.property)
                    assertSame(target, first.value)
                    assertEquals("validation error", first.message)

                    assertEquals(null, second.property)
                    assertEquals(target.child, second.value)
                    assertEquals("validation error", second.message)

                    assertTrue(GrandChild::name == third.property)
                    assertEquals(target.child.child.name, third.value)
                    assertEquals("must be empty", third.message)
                }
            }
        }
    }
})
