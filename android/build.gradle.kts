plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.molecule)
    alias(libs.plugins.apolloPlugin)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.viewmodel.compose)
    implementation(libs.koin.android)
    implementation(libs.apollo.runtime)

    implementation(libs.compose.ui.tooling.preview.android)
    debugImplementation(libs.compose.ui.tooling)
}

android {
    namespace = libs.versions.android.packageName.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.android.packageName.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

apollo {
    service("RickAndMorty") {
        packageName.set(libs.versions.android.packageName.get())
    }
}

