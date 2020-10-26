plugins {
    kotlin("multiplatform")
}

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/spekframework/spek-dev/") }
}

kotlin {
    jvm {
        compilations.all { kotlinOptions { jvmTarget = "14" } }
        testRuns.all { executionTask.configure { useJUnitPlatform { includeEngines = setOf("spek", "spek2") } } }
        withJava()
    }

    sourceSets.all {
        languageSettings.apply {
            enableLanguageFeature("InlineClasses")
            useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }

    val kotlinVersion: String by extra
    val spek: (String) -> String by extra

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        all {
            dependencies {
                implementation(project(":ktil-core"))
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common", kotlinVersion))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common", kotlinVersion))
                implementation(kotlin("test-annotations-common", kotlinVersion))
                implementation(spek("dsl-common"))
                implementation(spek("runtime-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib", kotlinVersion))
                implementation(kotlin("reflect", kotlinVersion))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit", kotlinVersion))
                implementation(kotlin("test", kotlinVersion))
                implementation(spek("dsl-jvm"))
                implementation(spek("runtime-jvm"))
                implementation(spek("runner-junit5"))
            }
        }
    }
}
