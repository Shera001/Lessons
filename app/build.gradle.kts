plugins {
    id("test.android.application")
    id("test.android.hilt")
}

android {
    namespace = "uz.sherali.test"

    defaultConfig {
        applicationId = "uz.sherali.test"
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

        debug {
            isDebuggable = false
        }
    }
}

dependencies {

    implementation(project(":core:navigation-api"))
    implementation(project(":core:data"))
    implementation(project(":feature:lessons"))
    implementation(project(":feature:video-player"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.androidx.core.splashscreen)
}