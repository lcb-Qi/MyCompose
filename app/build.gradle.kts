import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.agp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.room)

    id("kotlin-parcelize")
}

composeCompiler {}

android {
    namespace = "com.lcb.one"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lcb.one"
        minSdk = 31
        targetSdk = 34
        versionCode = 10701
        versionName = "1.7.1-beta"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    defaultConfig {
        val now = LocalDateTime.now()
        val buildTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        val buildId = now.format(DateTimeFormatter.ofPattern("MMddHHmm"))
        resValue("string", "BUILD_TIME", buildTime)
        resValue("string", "BUILD_ID", buildId)
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

ksp {
    arg("room.incremental", "true")
    arg("room.generateKotlin", "true")
}

tasks.register("copyTask") {
    doLast {
        val appName = "Salted_fish"
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val fileName = "$appName-${android.defaultConfig.versionName}-release-$date.apk"
        val sourceFile = "./build/outputs/apk/release/app-release.apk"
        val destinationFile = File(project.projectDir.path + "/apk/")
        if (!destinationFile.exists()) {
            destinationFile.mkdir()
        }
        copy {
            from(sourceFile)
            into(destinationFile)
            rename { fileName }
        }
    }
}

project.afterEvaluate {
    val assembleRelease = tasks.getByName("assembleRelease")
    assembleRelease.finalizedBy("copyTask")
}

val stableCompose = false

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(libs.androidx.core)
    // compose
    if (stableCompose) {
        implementation(platform(libs.androidx.compose.bom))
    } else {
        implementation(platform(libs.androidx.compose.bom.snapshosts))
    }
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.compose.support)

    implementation(libs.kolor)
    implementation(libs.coil.compose)
    implementation(libs.zoomimage.compose.coil)
    // 协程
    implementation(libs.coroutines.android)
    // lifecycle
    implementation(libs.bundles.androidx.lifecycle)
    // preference
    implementation(libs.androidx.preference)
    // retrofit2
    implementation(libs.bundles.retrofit2)
    // json
    implementation(libs.kotlinx.serialization.json)
    // markdown
    implementation(libs.markdown)
    // room
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    implementation(libs.bundles.settings.ui)
    implementation(libs.compose.webview)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)
    implementation(files("libs/lib-androidx-media3-decoder-flac.aar"))

    implementation(libs.lottie.compose)
    implementation(libs.androidx.datastore.preferences)
    // for test
    addTestDependencies()
}

fun DependencyHandlerScope.addTestDependencies() {
    if (stableCompose) {
        androidTestImplementation(platform(libs.androidx.compose.bom))
    } else {
        androidTestImplementation(platform(libs.androidx.compose.bom.snapshosts))
    }
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.espresso)
    androidTestImplementation(libs.test.ui.junit4)
    debugImplementation(libs.test.ui.tooling)
    debugImplementation(libs.test.ui.manifest)
}