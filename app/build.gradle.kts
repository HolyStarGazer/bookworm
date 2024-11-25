import java.io.FileInputStream
import java.util.Properties

// Creates a variable called keystorePropertiesFile, and initializes it to the
// keystore.properties file.
val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initializes a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Loads the keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.utsa.cs3773.bookworm"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "edu.utsa.cs3773.bookworm"
        minSdk = 27
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
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "REFRESH_TOKEN_SECRET", "\"${keystoreProperties["REFRESH_TOKEN_SECRET"]}\"")
            buildConfigField("String", "API_DOMAIN", "\"${keystoreProperties["API_DOMAIN"]}\"")
        }
        debug {
            buildConfigField("String", "REFRESH_TOKEN_SECRET", "\"${keystoreProperties["REFRESH_TOKEN_SECRET"]}\"")
            buildConfigField("String", "API_DOMAIN", "\"${keystoreProperties["API_DOMAIN"]}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.auth0.java.jwt)
    implementation(libs.security.crypto.v110alpha06)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.gson)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.recyclerview)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Add Glide dependencies
    implementation(libs.glide) // Glide
    annotationProcessor(libs.compiler) // Glide compiler
}