// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false

    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10" apply false


    kotlin("kapt") version "1.8.0" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

ext["compileSdk"] = 34
ext["minSdk"] = 31
ext["version_code"] = 10000
ext["version_name"] = "1.0.0"

ext["compose_bom"] = "2024.02.00"