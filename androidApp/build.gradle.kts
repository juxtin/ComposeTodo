plugins {
    com.android.application
    org.jetbrains.kotlin.android
    org.jetbrains.compose
    license
}

android {
    namespace = "app.softwork.composetodo"
    compileSdk = 33

    defaultConfig {
        applicationId = "app.softwork.composetodo"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}

dependencies {
    implementation(projects.composeClients)
    implementation("androidx.activity:activity-compose:1.6.1")
}

licensee {
    allow("MIT")
}
