import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("androidx.room")
}


android {
    namespace = "com.lcb.one"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lcb.one"
        minSdk = 31
        targetSdk = 34
        versionCode = 10405
        versionName = "1.4.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    composeOptions {
        // Compose 与 Kotlin 的兼容性对应关系：
        // https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.5.6"
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

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    // compose
    // bom对照表：https://developer.android.google.cn/develop/ui/compose/bom/bom-mapping
    val composeVersion = "2024.04.00"
    implementation(platform("androidx.compose:compose-bom:$composeVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // glance
    implementation("androidx.glance:glance-appwidget:1.0.0")

    // 图片加载 coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // 网络请求 retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.2")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")

    // json
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // markdown
    implementation("io.noties.markwon:core:4.6.2")

    // room数据库
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // implementation("xyz.junerver.compose:hooks:1.0.11")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}