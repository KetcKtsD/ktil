plugins {
    kotlin("multiplatform")
}

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

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

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

        all {
            languageSettings.apply {
                enableLanguageFeature("InlineClasses")
                useExperimentalAnnotation("kotlin.RequiresOptIn")
            }
        }
    }
}

//tasks.named<Test>("jvmTest") {
//    useJUnitPlatform(object : Action<JUnitPlatformOptions> {
//        override fun execute(p0: JUnitPlatformOptions) {
//            p0.includeEngines = setOf("spek", "spek2")
//        }
//    })
//}
