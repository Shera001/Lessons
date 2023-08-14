package uz.sherali.test

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun configureKotlinAndroid(
    commonExtensions: CommonExtension<*, *, *, *, *>
) {
    with(commonExtensions) {
        compileSdk = Config.compileSdkVersion

        defaultConfig {
            minSdk = Config.minSdkVersion
        }

        compileOptions {
            sourceCompatibility = Config.javaVersion
            targetCompatibility = Config.javaVersion
        }

        buildFeatures {
            aidl = true
            prefab = false
            shaders = false
            compose = false
            resValues = false
            buildConfig = false
            viewBinding = true
            renderScript = false
        }

        kotlinOptions {
            jvmTarget = Config.javaVersion.toString()
        }
    }
}

fun CommonExtension<*, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }

    configureKotlin()
}

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = Config.javaVersion.toString()
        }
    }
}