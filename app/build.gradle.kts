import org.gradle.kotlin.dsl.test

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.firebase.appdistribution)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.finapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.finapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation("androidx.compose.material3:material3:1.0.0")
    implementation("androidx.compose.foundation:foundation:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    dependencies {
        implementation("com.google.android.gms:play-services-maps:18.1.0")  // Google Maps SDK
        implementation("com.google.android.material:material:1.7.0")  // Material Components (if needed)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.firebase.auth)
        implementation(libs.firebase.database)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
        implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
        implementation("androidx.navigation:navigation-compose:2.7.4")
        implementation("androidx.compose.ui:ui-tooling:1.5.3")
        implementation("androidx.compose.ui:ui:1.4.0")
        implementation("io.coil-kt:coil-compose:2.0.0")
        implementation("androidx.navigation:navigation-fragment-compose:2.8.4")




        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Optional, for JSON conversion


        implementation("com.squareup.okhttp3:okhttp:4.9.1")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")
        implementation("androidx.activity:activity-compose:1.6.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    }
}