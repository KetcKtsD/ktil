# ktil

KotlinのMPPなライブラリ郡､ リリースされたKotlinの新機能の実験場所も兼ねてる｡

![CI with Gradle](https://github.com/KetcKtsD/ktil/workflows/CI%20with%20Gradle/badge.svg?branch=master)

[![Kotlin](https://img.shields.io/badge/kotlin-1.4.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

## Installation

[GitHub Packages](https://docs.github.com/ja/free-pro-team@latest/packages/using-github-packages-with-your-projects-ecosystem/configuring-gradle-for-use-with-github-packages#authenticating-to-github-packages)に公開してありあます｡

* notations

```
implementation("tech.ketc.ktil:ktil-$module-$platform:$ktilVersion")
```

* repository setting

```kotlin
repositories {
    jcenter()
    maven {
        url = uri("https://maven.pkg.github.com/ketcktsd/ktil/")
        credentials {
            username = "github-username"
            password = "github-token"
        }
    }
}
```

## Supported platforms

- `metadata` (common)
- `jvm`

## Modules

### [ktil-core](https://github.com/KetcKtsD/ktil/tree/develop/core)

ちょっとした拡張関数や`Either<L､R>`などのユーティリティを提供する

### [ktil-validation](https://github.com/KetcKtsD/ktil/tree/develop/validation)

シンプルなバリデーション用のDSLを提供する
