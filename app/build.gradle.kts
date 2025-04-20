import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android) // hilt
    alias(libs.plugins.kotlin.kapt) // kapt
    alias(libs.plugins.devtools.ksp) // ksp
    alias(libs.plugins.kotlin.serialization) // serialization
    alias(libs.plugins.kotlin.parcelize) // parcelize
    // google map
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    // firebase
    alias(libs.plugins.google.gms.google.services)
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
        val geoApiKey = localProperties.getProperty("GEOPIFY_API_KEY")
        val mapApiKey = localProperties.getProperty("MAPS_API_KEY")
        val imageBaseUrl = localProperties.getProperty("PEXELS_API_BASE_URL")
        val imageKey = localProperties.getProperty("PEXELS_API_KEY")
        val geminiBaseUrl = localProperties.getProperty("GEMINI_API_BASE_URL")
        val geminiKey = localProperties.getProperty("GEMINI_API_KEY")
        val n8nBaseUrl = localProperties.getProperty("N8N_BASE_URL")
        buildConfigField("String", "GEO_API_BASE_URL", "\"$geoApiBaseUrl\"")
        buildConfigField("String", "GEOPIFY_API_KEY", "\"$geoApiKey\"")
        buildConfigField("String", "MAPS_API_KEY", "\"$mapApiKey\"")
        buildConfigField("String", "IMAGE_BASE_URL", "\"$imageBaseUrl\"")
        buildConfigField("String", "IMAGE_API_KEY", "\"$imageKey\"")
        buildConfigField("String", "GEMINI_BASE_URL", "\"$geminiBaseUrl\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiKey\"")
        buildConfigField("String", "N8N_BASE_URL", "\"$n8nBaseUrl\"")
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
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

    // firebase - firestore
    implementation(libs.firebase.firestore)
    // firebase - auth
    implementation(libs.firebase.auth)

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
    implementation(libs.maps.compose)
    implementation(libs.maps.services)
    // places
    implementation(libs.google.maps.places)

    // google play location service
    implementation(libs.play.services.location)

    // accompanist permission
    // Permissions
    implementation (libs.accompanist.permissions)

    // agora
    implementation(libs.agora.voice.sdk)

    // supabase and ktor
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.1.4")
    implementation("io.github.jan-tennert.supabase:realtime-kt:3.1.4")
    implementation ("io.ktor:ktor-client-core:3.0.2")
    implementation("io.ktor:ktor-client-android:3.0.2")
    implementation("io.ktor:ktor-client-websockets:3.0.2")
    implementation("io.ktor:ktor-client-cio:3.0.2")
    implementation("io.ktor:ktor-client-okhttp:3.0.2")
}