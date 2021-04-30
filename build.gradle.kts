buildscript {
    val kotlinVersion by extra { "1.4.31" }
    val dokkaVersion by extra { "1.4.10.2" }

    apply(from = "common-dependencies.gradle.kts")

    repositories {
        maven(url = "https://dl.bintray.com/kotlin/dokka")
        jcenter()
        google()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${dokkaVersion}")
    }
}

subprojects {
    repositories {
        jcenter()
        google()
        maven(url = "https://dl.bintray.com/spekframework/spek-dev/")
    }

    group = "tech.ketc.ktil"
    version = "0.2.0"

    extensions.extraProperties.apply {
        this["jvmTargetVersion"] = "15"
    }
}

tasks {
    wrapper { gradleVersion = "6.8.3" }
}
