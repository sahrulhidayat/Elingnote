plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply("./../../shared_dependencies.gradle")

android {
    namespace = "com.sahi.feature.note"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))
    implementation(project(":data:database"))
    implementation(project(":core:ui"))
    implementation(project(":core:notifications"))
    implementation(project(":core:utils"))
}