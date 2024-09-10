import com.android.build.gradle.internal.utils.KOTLIN_KAPT_PLUGIN_ID
import com.android.build.gradle.internal.utils.KSP_PLUGIN_ID

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.firebase)
    alias(libs.plugins.dagger.hilt)
    id("kotlin-kapt")
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.ncodes.playlists"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ncodes.playlists"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
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
    implementation(libs.core.ktx)

    implementation(libs.coroutine.play)
    //annotation processor
    ksp(libs.androidx.room.annotation)

    //room
    implementation(libs.androidx.room.ktx)

    //SQUARE
    //square retrofit
    implementation(libs.square.retrofit)
    //square retrofit converter
    implementation(libs.square.retrofit.converter)

    //gson
    implementation(libs.gson)

    //coil
    implementation(libs.coil)


    //firebase
    //bom
    implementation(platform(libs.firebase.bom))
    //analytics
    implementation(libs.firebase.analytics)
    //database
    implementation(libs.firebase.database)

    //dagger - hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)


    //////////////////////////////TESTING////////////////////////////////////////////
    //espresso core
    androidTestImplementation(libs.androidx.espresso.core)


    //testCore
    androidTestImplementation(libs.androidx.test.core)

    //room test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    //compose test
    androidTestImplementation(platform(libs.androidx.compose.bom))

    //Jnuit test
    androidTestImplementation(libs.androidx.ui.test.junit4)


    //room test
    //androidTestImplementation(libs.androidx.room)
    androidTestImplementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.testing)

    //Kotlin coroutines
    testImplementation(libs.kotlin.coroutine.rule)

    //mockserver
    testImplementation(libs.square.mockserver)

    //mokito-core
    testImplementation(libs.mokito.core)
    testImplementation(libs.mokito.inline)

    //turbine
    testImplementation(libs.turbine)

    //Roboelectric
    //Does not support compose.
    testImplementation(libs.roboelectric)

    //////////////////////////////debug////////////////////////////////////////////
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}

//Allow references to generated code
kapt {
    correctErrorTypes = true
}