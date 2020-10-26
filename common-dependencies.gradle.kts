@Suppress("UNUSED_VARIABLE")
subprojects {
    val spekVersion by extra { "2.0.0-alpha.2" }

    notation("spek") { module -> "org.spekframework.spek2:spek-$module:$spekVersion" }
}

fun Project.notation(name: String, creator: (module: String) -> Any) {
    extensions.extraProperties[name] = creator
}

fun Project.notation(name: String, creator: (module: String, version: String) -> Any) {
    extensions.extraProperties[name] = creator
}
