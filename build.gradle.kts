// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.9.21"
    val kspVersion = "1.9.21-1.0.15"
    val androidApplicationVersion = "8.1.2"

    id("com.android.application") version androidApplicationVersion apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("com.google.devtools.ksp") version kspVersion apply false

    id("androidx.room") version "2.6.1" apply false
}

ext["compileSdk"] = 34
ext["minSdk"] = 31
ext["version_code"] = 10400
ext["version_name"] = "1.4.0"