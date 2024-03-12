import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlin-parcelize")

    kotlin("kapt")
    id("com.google.devtools.ksp")
}


android {
    namespace = "com.lcb.one"
    compileSdk = rootProject.ext.get("compileSdk") as Int

    defaultConfig {
        applicationId = "com.lcb.one"
        minSdk = rootProject.ext.get("minSdk") as Int
        targetSdk = rootProject.ext.get("compileSdk") as Int
        versionCode = rootProject.ext.get("version_code") as Int
        versionName = rootProject.ext.get("version_name") as String

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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    defaultConfig {
        resValue(
            "string",
            "BUILD_TIME",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        )

        resValue(
            "string",
            "BUILD_ID",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmm"))
        )
    }
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:${rootProject.ext["compose_bom"]}"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("androidx.navigation:navigation-compose:2.7.6")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")

    // glance
    implementation("androidx.glance:glance-appwidget:1.0.0")

    // 图片加载 coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // 网络请求 Okhttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // json
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}