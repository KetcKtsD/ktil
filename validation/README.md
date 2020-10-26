# ktil-validation

バリデーション用DSL

## Installation

* gradle.kts

```kotlin
implementation("tech.ketc.ktil:ktil-validation:$ktilVersion")
```

## samples

### シンプルなやつ

```kotlin
 fun getValue(): String = TODO("Implement me")
 
 val validator = validator<String> { //ValidationScope<String>
     String::class shouldBe { it.isNotBlank() } otherwise "this string must be not blank"
 }
 
 val result: Either<List<ValidationError>, out String> = validator.validate(getValue())
 result.fold(
     onLeft = { error -> error.forEach { println("error message: ${it.message}") } },
     onRight = { validValue -> println("valid: $validValue") }
 )
```

### クラスのプロパティに対するバリデーション

```kotlin
class Product(val name: String = "")
class User(val name: String = "", val email: String = "", val product: Product = Product())

class ProductValidator : Validator<Product>({
    Product::name shouldBe notBlankString otherwise "product-name must be not blank"
})

val validator = validator<User> {
    User::name shouldBe lengthIsBetween(3..128)
    User::email shouldBe email
    User::product validateBy ProductValidator()
}

val result: ValidationResult<User> = validator.validate(User())
result.left { println("validation failed") }
result.right { println("validation succeeded") }
```

### バリデーション定義の使いまわし

```kotlin
open class User(val name: String)
class PrincipalUser(name: String, val token: String) : User(name)

val userValidator = validator<User> {
    User::name shouldBe lengthIsBetween(1..20)
    User::name shouldBe notBlankString
    User::name shouldBe contains("[A-Z]".toRegex())
    User::name shouldBe contains("[0-9]".toRegex())
}

val principalUserValidator = validator<PrincipalUser> {
    PrincipalUser::class validateBy userValidator
    PrincipalUser::token shouldBe notBlankString
}

val target = PrincipalUser("name", "token")
if (principalUserValidator.validate(target).isRight) println("validation succeeded")
```
