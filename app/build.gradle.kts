plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.cloner.swfaddressesgenerator"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.cloner.swfaddressesgenerator"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 36
        versionCode = 6
        versionName = "1.0.0006"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    buildToolsVersion = "37.0.0"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":WalletAddressesGenerator"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}