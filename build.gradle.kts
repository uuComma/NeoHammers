import dev.isxander.mtk.multiloader.utils.ngRuns

plugins {
    `java-library`

    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.neoforge.gradle) apply false

    alias(libs.plugins.modstitch.multiloader)
    alias(libs.plugins.modstitch.manifests)
    alias(libs.plugins.modstitch.modrepos)
    alias(libs.plugins.modstitch.accessx)

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

val awName = "$modid.accessWidener"
val fabricAWTask = accessx.convert("fabric", sourceSets.fabric.get()) {
    inputFiles.from(awName)
    outputFormat = accessx.CT_V2
}

val neoforgeATTask = accessx.convert("neoforge", sourceSets.fabric.get()) {
    inputFiles.from(awName)
    outputFormat = accessx.AT
}

loom.accessWidenerPath = project.file(awName)
accessTransformers.files.from(neoforgeATTask.flatMap { it.outputFile })
tasks.named { it in listOf("neoFormTransformSource", "applyAccessTransformer") }.configureEach {
    dependsOn(neoforgeATTask)
}

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

        accessWidener(fabricAWTask)
    }

    neoForgeModsToml(sourceSets.neoforge.get()) {
        from(common)

        dependency {
            modId = "neoforge"
            versionRange(providers.gradleProperty("neoforge.vr").get())
            required()
        }

        accessTransformer(neoforgeATTask)
    }
}

runs.register("neoforgeClient") {
    runType("client")
}

val generatedResourcesFolder: File = project.file("src/main/generated").absoluteFile

sourceSets {
    main {
        resources {
            srcDirs.add(generatedResourcesFolder)
        }
    }
}

ngRuns.create("clientData") {
    arguments.addAll("--mod", modid, "--all", "--output", generatedResourcesFolder.absolutePath, "--exisiting", file("src/main/resources/").absolutePath)
}

repositories {
    mavenCentral()
    terraformersMC()
}

dependencies {
    minecraft(libs.minecraft)
    fabricLoader(libs.fabric.loader)
    neoforgeImplementation(libs.neoforge)

    fabricImplementation(libs.fabric.api)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories { }
}