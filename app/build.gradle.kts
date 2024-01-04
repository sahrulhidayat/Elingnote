plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

apply("../shared_dependencies.gradle")

android {
    namespace = "com.sahi.elingnote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sahi.elingnote"
        minSdk = 21
        targetSdk = 34
        versionCode = 4
        versionName = "0.4.1"

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
        }
        debug {
            isDebuggable = true
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
    implementation(project(":core:ui"))
    implementation(project(":data:database"))
    implementation(project(":feature:note"))
    implementation(project(":feature:checklist"))
    implementation(project(":feature:trash"))
    implementation(project(":di"))
}