plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    id("org.sonarqube") version "7.3.0.8198"
}

sonar {
  properties {
    property("sonar.projectKey", "CDA-2025-CESI-Zen_CESI-Zen-Mobile")
    property("sonar.organization", "cda-2025-cesi-zen")
  }
}
