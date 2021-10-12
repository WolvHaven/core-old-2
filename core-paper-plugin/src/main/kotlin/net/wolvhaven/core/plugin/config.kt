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

package net.wolvhaven.core.plugin

import net.wolvhaven.core.common.Config
import net.wolvhaven.core.common.WhPlugin
import java.io.File

inline fun <reified C> config(file: File) = Config(C::class.java, file)
inline fun <reified C> config(fileName: String, plugin: WhPlugin) = config<C>(File(plugin.dataFolder, "$fileName.conf"))
