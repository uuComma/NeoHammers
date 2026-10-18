pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.neoforged.net/releases")
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}
