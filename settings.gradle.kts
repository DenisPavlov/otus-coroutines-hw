rootProject.name = "otus-coroutines-hw"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
    }
}

include("m1l5-coroutines")