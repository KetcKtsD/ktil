plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("maven-publish")
}

kotlin {
    val jvmTargetVersion: String by extra
    jvm {
        compilations.all { kotlinOptions { jvmTarget = jvmTargetVersion } }
        testRuns.all { executionTask.configure { useJUnitPlatform { includeEngines = setOf("spek", "spek2") } } }
        withJava()
    }

    sourceSets.all {
        languageSettings.apply {
            useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }

    val coroutines: (String) -> String by extra
    val spek: (String) -> String by extra

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies { api(kotlin("stdlib-common")) }
            dependencies { api(coroutines("core")) }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
                implementation(spek("dsl-common"))
                implementation(spek("runtime-common"))
            }
        }

        val jvmMain by getting {
            dependencies { api(kotlin("stdlib")) }
            dependencies { api(coroutines("core-jvm")) }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(spek("dsl-jvm"))
                implementation(spek("runtime-jvm"))
                implementation(spek("runner-junit5"))
            }
        }
    }
}

publishing {
    repositories {
        val token = System.getenv("GITHUB_TOKEN") ?: return@repositories
        val owner = System.getenv("GITHUB_OWNER") ?: return@repositories
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$owner/ktil")
            credentials {
                username = owner
                password = token
            }
        }
    }
}
