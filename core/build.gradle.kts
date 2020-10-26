plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/spekframework/spek-dev/") }
}

kotlin {
    metadata { mavenPublication { artifactId = "${project.name}-common" } }

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

    val spek: (String) -> String by extra

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies { api(kotlin("stdlib-common")) }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(spek("dsl-common"))
                implementation(spek("runtime-common"))
            }
        }

        val jvmMain by getting {
            dependencies { api(kotlin("stdlib")) }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
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
