import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // hilt
    id("com.google.dagger.hilt.android")

    // kapt
    id("kotlin-kapt")

    // ksp
    id("com.google.devtools.ksp")

    // serialization
    kotlin("plugin.serialization") version "2.1.10"

    // parcelize
    id("kotlin-parcelize")

    // google map
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "gli.project.tripmate"
    compileSdk = 35

    defaultConfig {
        applicationId = "gli.project.tripmate"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val geoApiBaseUrl = localProperties.getProperty("GEO_API_BASE_URL")
        val apiKey = localProperties.getProperty("API_KEY")
        val mapApiKey = localProperties.getProperty("MAPS_API_KEY")
        buildConfigField("String", "BASE_URL", "\"$geoApiBaseUrl\"")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String", "MAPS_API_KEY", "\"$mapApiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /**
     * Domain Dependency
     */

    // coroutines core
    implementation(libs.kotlinx.coroutines.core)
    // paging
    implementation(libs.paging.runtime)

    /**
     * Data Dependency
     */

    // retrofit
    implementation (libs.retrofit)
    implementation (libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)

    // room database
    implementation(libs.androidx.room.runtime)
    implementation (libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp (libs.androidx.room.compiler)
    implementation (libs.gson)

    // paging
    implementation (libs.paging.runtime)

    // coroutines
    implementation (libs.kotlinx.coroutines.android)

    // datastore-preferences
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    /**
     * App Dependency
     */

    // hilt for DI
    implementation(libs.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)

    // viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // coil
    implementation(libs.coil3.coil.compose)
    implementation(libs.coil.network.okhttp)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // serialization
    implementation(libs.kotlinx.serialization.json)

    // icons
    implementation (libs.composeIcons.fontAwesome)
    implementation(libs.composeIcons.lineAwesome)
    implementation(libs.composeIcons.tablerIcons)

    // google fonts
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.material.icons.extended)

    // shimmer
    implementation(libs.compose.shimmer)

    // paging compose
    implementation(libs.androidx.paging.compose)

    // maps
    implementation("com.google.maps.android:maps-compose:2.11.4")
    implementation("com.google.android.gms:play-services-maps:19.1.0")

    // google play location service
    implementation(libs.play.services.location)
}