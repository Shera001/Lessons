package uz.sherali.test

import org.gradle.api.JavaVersion

internal object Config {
    const val compileSdkVersion = 34

    const val minSdkVersion = 23
    const val targetSdkVersion = 34

    val javaVersion = JavaVersion.VERSION_17
}