@Suppress("UNUSED_VARIABLE")
subprojects {
    val coroutinesVersion by extra { "1.4.1" }
    val spekVersion by extra { "2.0.0-alpha.2" }

    notation("coroutines") { module -> "org.jetbrains.kotlinx:kotlinx-coroutines-$module:$coroutinesVersion" }
    notation("spek") { module -> "org.spekframework.spek2:spek-$module:$spekVersion" }
}

fun Project.notation(name: String, creator: (module: String) -> Any) {
    extensions.extraProperties[name] = creator
}

fun Project.notation(name: String, creator: (module: String, version: String) -> Any) {
    extensions.extraProperties[name] = creator
}
