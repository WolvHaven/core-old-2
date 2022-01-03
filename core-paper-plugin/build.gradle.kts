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

import com.github.jengelman.gradle.plugins.shadow.ShadowJavaPlugin
import com.github.jengelman.gradle.plugins.shadow.relocation.SimpleRelocator
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import xyz.jpenilla.runpaper.RunPaper
import xyz.jpenilla.runpaper.task.RunServerTask

apply<ShadowJavaPlugin>()
apply(plugin = "com.github.johnrengelman.shadow")
apply<RunPaper>()

dependencies {
    api(project(":paper-common"))
}

tasks {
    val shadowJar = named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")

        listOf(
            "com.github",
            "com.google",
            "com.typesafe",
            "io.leangen",
            "io.lettuce",
            "io.netty",
            "cloud",
            "org.checkerframework",
            "org.jetbrains",
            "org.spongepowered",
            "org.reactivestreams",
            "reactor",
            "kotlin",
            "net.kyori.event"
        ).forEach {
//            relocateWh(it)
        }

        minimize()
    }

    named<RunServerTask>("runServer") {
        dependsOn(shadowJar)
        minecraftVersion("1.18.1")
        jvmArgs("-DLog4jContextSelector=org.apache.logging.log4j.core.selector.ClassLoaderContextSelector") // https://github.com/PaperMC/Paper/issues/4155
    }
}

fun ShadowJar.relocateWh(thing: String) {
    relocate(thing, "net.wolvhaven.core.relocated.$thing")
}

fun ShadowJar.relocateWh(thing: String, configure: Action<SimpleRelocator>) {
    relocate(thing, "net.wolvhaven.core.relocated.$thing", configure)
}
