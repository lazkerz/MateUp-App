plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("io.realm.kotlin") version "1.11.0"
    id("kotlin-kapt")
    id("realm-android")
}

android {
        namespace = "com.example.apps_magang"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.example.apps_magang"
            minSdk = 24
            targetSdk = 33
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
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Realm
    implementation("io.realm.kotlin:library-base:1.11.0")
    implementation("io.realm.kotlin:library-sync:1.11.0") // If using Device Sync
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0") // If using coroutines with the SDK
    kapt("io.realm:realm-annotations-processor:10.9.0")
    implementation("io.realm:realm-android-library:10.9.0")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //chucker
    debugImplementation ("com.github.chuckerteam.chucker:library:3.5.2")
    releaseImplementation ("com.github.chuckerteam.chucker:library-no-op:3.5.2")
    // OkHttp
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

// Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.0")


}