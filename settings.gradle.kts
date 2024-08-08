pluginManagement {
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


rootProject.name = "My Recipe App"
include(":app")
include(":common")
include(":media_player")
include(":feature:search:ui")
include(":feature:search:data")
include(":feature:search:domain")
