plugins {
    kotlin("multiplatform") version "1.4.10"
}

group = "tech.ketc"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "14"
                freeCompilerArgs = listOf(
                    "-XXLanguage:+InlineClasses",
                    "-Xopt-in=kotlin.RequiresOptIn"
                )
            }
        }
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))
            }
        }

        val jvmTest by getting {
            dependencies {
                val spekVersion by extra { "2.0.12" }
                val junitJupiterVersion by extra { "5.6.2" }
                implementation(kotlin("test"))
                implementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
                implementation("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
                implementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
                implementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform {
        includeEngines = setOf("spek", "spek2")
    }
}

tasks {
    wrapper { gradleVersion = "6.7" }
}
