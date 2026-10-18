import dev.isxander.mtk.multiloader.utils.ngRuns

plugins {
    `java-library`

    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.neoforge.gradle) apply false

    alias(libs.plugins.modstitch.multiloader)
    alias(libs.plugins.modstitch.manifests)
    alias(libs.plugins.modstitch.modrepos)

    `maven-publish`
}

group = "com.github.uucomma.neohammers"
version = "${providers.gradleProperty("mod.version").get()}+mc${libs.versions.minecraft.get()}"

val buildNumber = providers.gradleProperty("mod.buildNum").get()
if (buildNumber.isNotBlank()) {
    version = "$version-b$buildNumber"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }

    withSourcesJar()
}

val modid = providers.gradleProperty("mod.id").get()

val minecraftVersionRange = providers.gradleProperty("minecraft.vr").get()
val supportedMinecraftVersions = manifests.minecraftReleasesMatching(minecraftVersionRange)

manifests {
    val common = manifest {
        modId = modid
        version = project.version.toString()
        displayName = providers.gradleProperty("mod.displayName")
        description = providers.gradleProperty("mod.description")
        authors.addAll(providers.gradleProperty("mod.authors").get().split(","))
        sourcesUrl = providers.gradleProperty("mod.sourcesURL")
        issueTrackerUrl = providers.gradleProperty("mod.issueURL")
        licenses.add(providers.gradleProperty("mod.license"))
        iconPath = providers.gradleProperty("mod.icon")

        mixin("$modid-common.mixins.json")

        dependency {
            modId = "minecraft"
            versionRange(minecraftVersionRange)
            required()
        }
    }

    fabricModJson(sourceSets.fabric.get()) {
        from(common)

        entrypoint("main", "com.github.uucomma.neohammers.fabric.NeoHammersFabric")
        entrypoint("client", "com.github.uucomma.neohammers.fabric.client.NeoHammersFabricClient")

        environment = BOTH

        dependency {
            modId = "fabricloader"
            versionRange(providers.gradleProperty("fabric-loader.vr").get())
            required()
        }

        dependency {
            modId = "fabric-api"
            versionRange(providers.gradleProperty("fabric-api.vr").get())
            required()
        }

        dependency {
            modId = "forgeconfigapiport"
            versionRange(providers.gradleProperty("fcapi.vr").get())
            required()
        }
    }

    neoForgeModsToml(sourceSets.neoforge.get()) {
        from(common)

        mixin("$modid-neoforge.mixins.json")

        dependency {
            modId = "neoforge"
            versionRange(providers.gradleProperty("neoforge.vr").get())
            required()
        }
    }
}

val generatedResourcesFolder: File = project.file("src/main/generated").absoluteFile

sourceSets {
    main {
        resources {
            srcDir(generatedResourcesFolder)
            exclude("**/*.bbmodel") // BlockBench project files
            exclude("src/main/generated/.cache") // datagen cache files
        }
    }
}

ngRuns.create("clientData") {
    arguments.addAll(
        "--mod", modid,
        "--all",
        "--output", generatedResourcesFolder.absolutePath,
        "--existing", file("src/main/resources/").absolutePath
    )
}

repositories {
    mavenCentral()
    terraformersMC()

    maven {
        name = "Fuzs Mod Resources"
        url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    }
}

dependencies {
    minecraft(libs.minecraft)
    fabricLoader(libs.fabric.loader)
    neoforgeImplementation(libs.neoforge)

    implementation(libs.fcapi.neoapi)

    // We apply this to both main and fabric so fabric api's class tweaker gets applied
    // But we don't actually use fabric api on main!!!
    implementation(libs.fabric.api)
    fabricImplementation(libs.fabric.api)

    fabricImplementation(libs.fcapi.fabricimpl)
    fabricRuntimeOnly(libs.modmenu)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories { }
}