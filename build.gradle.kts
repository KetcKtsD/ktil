buildscript {
    val kotlinVersion by extra { "1.4.10" }

    apply(from = "common-dependencies.gradle.kts")

    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

subprojects {
    repositories {
        jcenter()
        google()
    }

    group = "tech.ketc.ktil"
    version = "0.1.1"
}
