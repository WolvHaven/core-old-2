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

dependencies {
    api(libs.configurate)
    api(libs.kyori.events)
    api(libs.kotlin.reflect)
    api(libs.guava)

    compileOnlyApi(libs.slf4j.api)
    compileOnlyApi(libs.adventure.api)

    api(libs.adventure.minimessage) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-bom")
    }
    api(libs.adventure.text.serializer.gson) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-bom")
    }
    api(libs.adventure.text.serializer.plain) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-bom")
    }

    api(libs.cloud.core)
    api(libs.cloud.kotlin.extensions)

    compileOnlyApi(libs.lp)
}
