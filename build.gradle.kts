// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    kotlin("kapt") version "1.9.0"
    id("com.google.gms.google-services") version "4.3.15" apply false
}

buildscript {
    repositories {
        google()  // Ensure Google repository is included
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
}
