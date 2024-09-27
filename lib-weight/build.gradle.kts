plugins {
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.lcb.weight"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures { compose = true }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation((libs.bundles.androidx.compose))

    implementation(libs.androidx.core)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.espresso)
}