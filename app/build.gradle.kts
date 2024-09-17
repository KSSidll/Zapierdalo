import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.ksp)
}

android {
    val appNamespace = "com.kssidll.zapierdalo"

    namespace = appNamespace
    compileSdk = 34

    defaultConfig {
        applicationId = appNamespace
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            isJniDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeCompiler {
        includeSourceInformation = true

        featureFlags = setOf(
            ComposeFeatureFlag.OptimizeNonSkippingGroups
        )
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlinx.serialization.json)

    // AndroidX
    implementation(libs.androidx.activity)
    implementation(libs.androidx.splashscreen.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.navigation.compose)

    // Compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.window.sizeclass)
    implementation(libs.androidx.compose.material.icons.extended)

    // DI
    implementation(libs.google.hilt)
    kapt(libs.google.hilt.compiler)
    implementation(libs.google.hilt.navigation.compose)

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Helpers
    implementation(libs.google.accompanist)

    // Paging
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.compose)

    // Location
    implementation(libs.google.location)

    // Maps
    implementation(libs.osmdroid.android)
    implementation(libs.osmdroid.mapsforge)

    // Lint
    lintChecks(libs.slack.compose.lint)
}

ksp {
    arg(
        RoomSchemaArgProvider(
            File(
                projectDir,
                "schemas"
            )
        )
    )
}

kapt {
    correctErrorTypes = true
}

class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File
): CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> {
        return listOf("room.schemaLocation=${schemaDir.path}")
    }
}
