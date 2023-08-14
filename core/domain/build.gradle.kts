plugins {
    id("test.jvm.library")
}

dependencies {
    implementation(project(":core:model"))
    implementation("javax.inject:javax.inject:1")

    implementation(libs.kotlinx.coroutines.core)
}