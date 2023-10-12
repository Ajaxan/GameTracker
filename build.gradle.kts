import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

val pluginGroup = "com.redfootdev"
val pluginPath = "gametracker"
val pluginName = "GameTracker"
val pluginPrefix = "GT"

val pluginAuthors = listOf("Ajaxan")
val pluginDescription = "This plugin is a small overhead system for tracking a simple types of games."

val pluginVersion = "0.1-SNAPSHOT"
val pluginMinecraftVersion = "1.20.1"
val pluginApiVersion = "1.20"

val pluginDependencies = listOf("kotlin-stdlib")

plugins {
    kotlin("jvm") version "1.9.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

version = pluginVersion
group = pluginGroup
val copyJarLocation: String by project

repositories {
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://repo.papermc.io/repository/maven-public/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
    maven(url = "https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$pluginMinecraftVersion-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-platform-bukkit:4.3.1")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation(kotlin("reflect"))

    shadow("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    library("com.google.code.gson", "gson", "2.10.1") // All platform plugins
    bukkitLibrary("com.google.code.gson", "gson", "2.10.1") // Bukkit only
    //testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.31.0")
}

kotlin { // Extension for easy setup
    jvmToolchain(17)
    compilerOptions.javaParameters.set(true)
}


tasks.register<Copy>("copyJarToServer") {
    dependsOn("shadowJar")
    from("build/libs/${project.name}-$pluginVersion-all.jar")
    into(copyJarLocation)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.build {
    dependsOn("copyJarToServer")
}

application {
    mainClass.set("MainKt")
}
// Github for the guide on this gradle plugin: https://github.com/Minecrell/plugin-yml
bukkit {
    name = pluginName
    version = pluginVersion
    description = pluginDescription
    main = "$pluginGroup.$pluginPath.$pluginName"
    apiVersion = pluginApiVersion

    // Other possible properties from plugin.yml (optional)
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP // or POSTWORLD
    authors = pluginAuthors
    //depend = pluginDependencies
    //softDepend = listOf("Essentials")
    //loadBefore = listOf("BrokenPlugin")
    prefix = pluginPrefix
    defaultPermission = BukkitPluginDescription.Permission.Default.OP // TRUE, FALSE, OP or NOT_OP
    //provides = listOf("TestPluginOldName", "TestPlug")

    commands {
        register("gametracker") {
            description = "An Example Command!"
            usage = "/example"
            aliases = listOf("gt","gamet","gtracker")
            permission = "gametracker.admin"
            permissionMessage = "You must be an admin to create or modify a game."
        }
    }
}

