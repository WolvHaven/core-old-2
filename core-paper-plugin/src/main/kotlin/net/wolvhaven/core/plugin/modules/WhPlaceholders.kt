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

package net.wolvhaven.core.plugin.modules

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.wolvhaven.core.common.plugins.WhEssentials.afk
import net.wolvhaven.core.common.plugins.WhVanishNoPacket.canSee
import net.wolvhaven.core.common.plugins.WhVanishNoPacket.unvanished
import net.wolvhaven.core.common.plugins.WhVanishNoPacket.vanished
import net.wolvhaven.core.common.util.playerCollection
import net.wolvhaven.core.plugin.WhCorePlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class WhPlaceholders(private val plugin: WhCorePlugin) : PlaceholderExpansion(), WhModule {
    init {
        plugin.server.pluginManager.getPlugin("PlaceholderAPI")
            ?: throw IllegalStateException("PlaceholderAPI not installed!")
        this.register()
    }

    override fun getIdentifier() = "wh"

    override fun getAuthor() = "Underscore11"

    override fun getVersion() = plugin.description.version

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        if (player == null) return null
        return when (params) {
            "online" -> {
                val players = Bukkit.getOnlinePlayers().toMutableSet().playerCollection.canSee(player)
                var out = "&f${players.unvanished}"
                if (players.vanished != 0) out += "&b+${players.vanished}"
                if (players.afk != 0) out += "&7-${players.afk}"
                out += "&f/${Bukkit.getMaxPlayers()}"
                out
            }
            "isvanished" -> if (player.vanished) "&b[V]" else ""
            else -> null
        }
    }

    override fun disable() {
        unregister()
    }
}
