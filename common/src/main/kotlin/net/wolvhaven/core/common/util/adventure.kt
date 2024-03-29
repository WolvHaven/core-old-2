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

package net.wolvhaven.core.common.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.GREEN
import net.kyori.adventure.text.format.NamedTextColor.RED
import net.kyori.adventure.text.format.TextColor
import java.awt.Color

val TextColor.awtColor get() = Color(this.value())

fun pretty(t: Any?): Component {
    return when (t) {
        null -> text("Null")
        is PrettyPrintable -> t.pretty
        is Boolean -> pretty(t)
        is Enum<*> -> {
            text(t.name.lowercase().replaceFirstChar { it.uppercase() })
        }
        else -> text(t.toString())
    }
}

fun pretty(boolean: Boolean) = if (boolean) text("true", GREEN) else text("false", RED)

interface PrettyPrintable {
    val pretty: Component
}

val Any?.pretty get() = pretty(this)
