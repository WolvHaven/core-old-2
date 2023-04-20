/*
 * WHCore - Core features for the WolvHaven server
 * Copyright (C) 2023 Underscore11
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
// Naughty interfaces includes paper 1.16.5 - exclude
//    api(libs.interfaces)

    api(project(":common"))

    // Paper
    compileOnlyApi(libs.paper)

    api(libs.cloud.paper)

    // Plugins
    // -------
    compileOnlyApi(libs.vanish)
    compileOnlyApi(libs.papi)
    compileOnlyApi(libs.essentials)
    compileOnlyApi(libs.worldedit)
}
