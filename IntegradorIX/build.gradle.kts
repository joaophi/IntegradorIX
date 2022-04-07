// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false

    kotlin("android") version "1.6.20" apply false
    id("com.google.devtools.ksp") version "1.6.20-1.0.4" apply false

    id("androidx.navigation.safeargs") version "2.4.2" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}