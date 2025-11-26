// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // AL USAR "alias", obligamos a Gradle a leer las versiones desde libs.versions.toml
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Agregamos el plugin de Compose compiler (necesario para Kotlin 2.0+)
    alias(libs.plugins.kotlin.compose) apply false

    // Firebase (Google Services)
    // Nota: Es mejor usar la versión 4.4.2 para compatibilidad actual
    id("com.google.gms.google-services") version "4.4.2" apply false
}