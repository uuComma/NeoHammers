import dev.isxander.mtk.multiloader.utils.ngRuns
import me.modmuss50.mpp.ReleaseType
import java.time.LocalDateTime

plugins {
    ->
    `java-library`

    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.neoforge.gradle) apply false

    alias(libs.plugins.modstitch.multiloader)
    alias(libs.plugins.modstitch.manifests)
    alias(libs.plugins.modstitch.modrepos)
    alias(libs.plugins.modpublishplugin)

    `maven-publish`
}

val modversion = providers.gradleProperty("mod.version").get();

group = "com.github.uucomma.neohammers"
version = "$modversion+mc${libs.versions.minecraft.get()}"

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
        sourcesUrl = providers.gradleProperty("mod.sourceURL");
        issueTrackerUrl = providers.gradleProperty("mod.issueURL")
        homepage = providers.gradleProperty("mod.homepageURL")
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

        //customData.put("modmenu.links.modmenu\\.source", sourcesUrl.get())
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

tasks.neoforgeJar {
    archiveClassifier.set("neo")
}

tasks.neoforgeSourcesJar {
    archiveClassifier.set("neo-sources")
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories { }
}

publishMods {
    val fabricJar = tasks.fabricJar.flatMap { it.archiveFile }
    val neoforgeJar = tasks.neoforgeJar.flatMap { it.archiveFile }
    val fabricSourcesJar = tasks.fabricSourcesJar.flatMap { it.archiveFile }
    val neoforgeSourcesJar = tasks.neoforgeSourcesJar.flatMap { it.archiveFile }

    type.set(ReleaseType.of(providers.gradleProperty("publish.releaseType").get()))
    displayName.set("Neo Hammers v${project.version}")

    dryRun.set(providers.gradleProperty("publish.dryRun").get() == "true")

    changelog.set(providers.fileContents(rootProject.layout.projectDirectory.file("Changelog.md")).asText
        .map {
            it
                .replace("{version}", project.version.toString())
                .replace("{mc}", supportedMinecraftVersions.get().joinToString(","))
        })

    val baseCurseForgeOptions = curseforgeOptions {
        accessToken.set(providers.environmentVariable("CURSEFORGE_TOKEN"))
        javaVersions.add(JavaVersion.VERSION_25)
        minecraftVersions.set(supportedMinecraftVersions)
        client.set(true)
        server.set(true)
        projectId.set(providers.gradleProperty("publish.curseforgeId"))
    }

    val baseModrinthOptions = modrinthOptions {
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
        minecraftVersions.set(supportedMinecraftVersions)
        environment.set(CLIENT_AND_SERVER)
        projectId.set(providers.gradleProperty("publish.modrinthId"))
    }

    curseforge("curseforgeFabric") {
        from(baseCurseForgeOptions)

        file = fabricJar
        additionalFiles.from(fabricSourcesJar)

        modLoaders.add("fabric")

        requires("fabric-api", "forge-config-api-port")
        optional("modmenu")
    }

    curseforge("curseforgeNeoForge") {
        from(baseCurseForgeOptions)

        file = neoforgeJar
        additionalFiles.from(neoforgeSourcesJar)

        modLoaders.add("neoforge")
    }

    modrinth("modrinthFabric") {
        from(baseModrinthOptions)

        file = fabricJar
        additionalFiles.from(fabricSourcesJar)

        modLoaders.add("fabric")

        requires("fabric-api", "forge-config-api-port")
        optional("modmenu")
    }

    modrinth("modrinthNeoForge") {
        from(baseModrinthOptions)

        file = neoforgeJar
        additionalFiles.from(neoforgeSourcesJar)

        modLoaders.add("neoforge")
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set(providers.gradleProperty("publish.githubRepo"))
        commitish.set(providers.gradleProperty("publish.githubRepo.branch").get())
        tagName.set(project.version.toString())

        file = neoforgeJar
        additionalFiles.from(fabricJar, fabricSourcesJar, neoforgeSourcesJar)
    }
}