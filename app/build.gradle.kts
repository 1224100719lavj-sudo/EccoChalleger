plugins {
    // Usamos 'alias' para leer las versiones correctas desde libs.versions.toml
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // ✅ ESTA ES LA SOLUCIÓN AL ERROR: El plugin compilador de Compose
    alias(libs.plugins.kotlin.compose)

    // ✅ ACTUALIZACIÓN KSP: Debe coincidir con Kotlin 2.0.21
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"

    id("com.google.gms.google-services")
}

android {
    namespace = "mx.edu.utng.lavj.eccochalleger"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.edu.utng.lavj.eccochalleger"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    // Nota: Ya no necesitas el bloque 'composeOptions' en Kotlin 2.0, por eso no está aquí.
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Dependencias de Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.7.5")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.4")

    // ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Room Database
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Firebase BOM
    // Versión estable recomendada
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // Librerías de Firebase (Sin versiones, el BOM las controla)
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-analytics")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}