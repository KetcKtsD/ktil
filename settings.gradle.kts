rootProject.name = "ktil"

val projects = arrayOf(":core", ":validation", ":coroutines")
include(*projects)

projects.forEach { project(it).name = "ktil-${it.replace(":", "")}" }
