import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
}
val props = Properties()
file("../key.properties").inputStream().use{ props.load(it) }

android {
    namespace = "com.praveen.weathertest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.praveen.weathertest"
        minSdk = 28
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            buildConfigField( "String", "WEATHER_API_KEY", "\"" + props.getProperty("WEATHER_API_KEY") + "\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            buildConfigField( "String", "WEATHER_API_KEY", "\"" + props.getProperty("WEATHER_API_KEY") + "\"")
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
    }
    composeOptions {
        // Explicitly specify the Compose Compiler version
        kotlinCompilerExtensionVersion = "1.5.3"
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
    implementation(libs.retrofit)
    implementation(libs.gsonConverter)
    implementation(libs.hiltAndroid)
    implementation(libs.firebase.config.ktx)
    implementation(libs.androidx.junit.ktx)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    implementation(libs.coil.compose)
//    implementation("com.google.firebase:firebase-analytics:22.1.2")
//    implementation("com.google.firebase:firebase-config:22.0.1")
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.okhttpInterceptor)
    implementation(libs.location)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}