pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Test"
include(":app")
include(":feature:lessons")
include(":feature:video-player")
include(":core:model")
include(":core:domain")
include(":core:network")
include(":core:data")
include(":core:ui")
include(":core:navigation-api")
include(":core:database")
