/*
 * WHCore - Core features for the WolvHaven server
 * Copyright (C) 2021 Underscore11
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:Suppress("UnstableApiUsage")

rootProject.name = "core"

enableFeaturePreview("VERSION_CATALOGS")

include("paper-common")
include("common")
include("core-paper-plugin")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.jpenilla.xyz/snapshots/") // for shadow snapshot
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("adventure", "4.9.1")
            version("cloud", "1.5.0")
            version("kotlin", "1.5.30")

            alias("shadow").toPluginId("com.github.johnrengelman.shadow").version("7.0.0")
            alias("runpaper").toPluginId("xyz.jpenilla.run-paper").version("1.0.7-SNAPSHOT")
            alias("ktlint").toPluginId("org.jlleitschuh.gradle.ktlint").version("10.2.0")

            alias("configurate").to("org.spongepowered:configurate-hocon:4.1.1")
            alias("slf4j-api").to("org.slf4j:slf4j-api:1.7.32")
            alias("guava").to("com.google.guava:guava:30.1-jre")
            alias("kyori-events").to("net.kyori:event-api:5.0.0-SNAPSHOT")
            alias("kotlin-reflect").to("org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")

            adventure("api")
            adventure("text-serializer-plain")
            adventure("text-serializer-gson")
            // minimessage stable release when? :(
            alias("adventure-minimessage").to("net.kyori", "adventure-text-minimessage").version("4.1.0-SNAPSHOT")
            bundle("adventure", listOf(
                "adventure-api",
                "adventure-text-serializer-plain",
                "adventure-text-serializer-gson",
                "adventure-minimessage"
            ))

            cloud("core")
            cloud("kotlin-extensions")
            cloud("paper")
            cloud("velocity")

            alias("interfaces").to("org.incendo.interfaces:interfaces-paper:1.0.0-SNAPSHOT")

            alias("paper").to("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
            alias("velocity").to("com.velocitypowered:velocity-api:3.0.1")

            alias("lp").to("net.luckperms:api:5.3")
            alias("vanish").to("com.github.mbax:VanishNoPacket:0cb428ff27")
            alias("papi").to("me.clip:placeholderapi:2.10.9")
            alias("essentials").to("net.ess3:EssentialsX:2.18.2")
            alias("worldedit").to("com.sk89q.worldedit:worldedit-bukkit:7.2.6")
        }
    }
}

fun VersionCatalogBuilder.adventure(module: String) {
    alias("adventure-$module").to("net.kyori", "adventure-$module").versionRef("adventure")
}

fun VersionCatalogBuilder.cloud(module: String) {
    alias("cloud-$module").to("cloud.commandframework", "cloud-$module").versionRef("cloud")
}
include("velocity")
