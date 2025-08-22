import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.ksp)
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

val keyStoreProperties = Properties().apply {
    load(rootProject.file("app/key.properties").inputStream())
}

android {
    namespace = "com.app.skycast"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.skycast"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "WEATHER_API_BASE_URL", "\"https://api.weatherapi.com\"")
        buildConfigField("String", "WEATHER_VC_API_BASE_URL", "\"https://weather.visualcrossing.com\"")
        buildConfigField("String", "CITY_SEARCH_API_BASE_URL", "\"https://api.thecompaniesapi.com\"")
        buildConfigField("String", "WEATHER_API_KEY", "\"${localProperties.getProperty("WEATHER_API_KEY")}\"")
        buildConfigField("String", "WEATHER_VC_API_KEY", "\"${localProperties.getProperty("WEATHER_VC_API_KEY")}\"")
        buildConfigField("String", "AQI_BASE_URL", "\"https://air-quality-api.open-meteo.com\"")
        buildConfigField("String", "NEWS_BASE_URL", "\"https://newsapi.org/\"")
        buildConfigField("String", "NEWS_API_KEY", "\"${localProperties.getProperty("NEWS_API_KEY")}\"")
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file(keyStoreProperties.getProperty("storeFile"))
            storePassword = keyStoreProperties.getProperty("storePassword")
            keyAlias = keyStoreProperties.getProperty("keyAlias")
            keyPassword = keyStoreProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
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
        buildConfig = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.androidx.constraintlayout)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.sharedPrefs)
    implementation(libs.androidx.palette.ktx)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt.navigation.compose)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.coil)

    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.logging)
    implementation(platform(libs.square.retrofit.bom))
    implementation(libs.square.retrofit)
    implementation(libs.gson)
    implementation(libs.square.retrofit.gson)

    implementation(libs.timber)
    implementation(libs.logger)

    implementation(libs.lottie)

    implementation(libs.accompanist.permission)
}