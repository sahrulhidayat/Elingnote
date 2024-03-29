plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply("../shared_dependencies.gradle")

android {
    namespace = "com.sahi.di"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain:usecase"))
    implementation(project(":data:database"))
    implementation(project(":core:notifications"))
    implementation(project(":feature:note"))
    implementation(project(":feature:checklist"))
    implementation(project(":feature:trash"))
}