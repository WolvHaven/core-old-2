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

package net.wolvhaven.core.common.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.*
import net.kyori.adventure.text.format.NamedTextColor.GOLD
import net.kyori.adventure.text.format.NamedTextColor.GRAY
import net.kyori.adventure.text.format.TextDecoration.BOLD

fun prefix(type: Component? = null): Component {
    return if (type == null)
        text("[", GRAY, BOLD).append(text("WH", GOLD)).append(text("]"))
    else
        text("[", GRAY, BOLD).append(text("WH", GOLD)).append(text(" | "))
            .append(type.colorIfAbsent(GOLD)).append(text("]"))
}

fun prefixed(component: Component): Component {
    return prefixed(null, component)
}

fun prefixed(prefixType: Component? = null, main: Component): Component {
    return empty().append(prefix(prefixType)).append(space()).append(main)
}
