# ktil-validation

バリデーション用DSL

## Installation

* gradle.kts

```kotlin
implementation("tech.ketc.ktil:ktil-validation-$platform:$ktilVersion")
```

## samples

### シンプルな例

```kotlin
 validator<String> { //ValidationScope<String>
     String::class shouldBe { it.isNotBlank() } otherwise "error-message"
 }
```

### クラスのプロパティに対するバリデーション

```kotlin
val validator = validator<User> {
    User::name shouldBe lengthIsBetween(3..128)
    User::email shouldBe email

    User::product validateBy ProductValidator()
}
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
```

### 実際にバリデートする
```
val validator = validator<User> {
    // いろいろ定義する
}

val result: ValidationResult<User> = validator.validate(User())
result.left { println("validation failed") }
result.right { println("validation succeeded") }
```
