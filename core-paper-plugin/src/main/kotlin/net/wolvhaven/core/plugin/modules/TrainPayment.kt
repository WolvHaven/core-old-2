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

import net.wolvhaven.core.common.util.CommandCreatorFunction
import net.wolvhaven.core.common.util.buildCommand
import net.wolvhaven.core.plugin.WhCorePlugin
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TrainPayment(private val plugin: WhCorePlugin) : WhModule {
    private val permRoot = "${net.wolvhaven.core.plugin.WhCorePlugin.permRoot}.trainpayment"
    private val permissionNotify = "$permRoot.notify"
    private val permissionTrigger = "$permRoot.trigger"
    private val permissionReceiveLegitimately = "$permRoot.receiveLegitimately"

    init {
        val base: CommandCreatorFunction<CommandSender> = {
            it.commandBuilder("metropay")
        }

        plugin.commandManager.buildCommand(base) { b ->
            b
                .permission(permissionTrigger)
                .handler {
                    val loc = when (val sender = it.sender) {
                        is BlockCommandSender -> {
                            sender.block.location
                        }
                        is Player -> {
                            sender.location
                        }
                        else -> {
                            sender.sendMessage("Command blocks or players only!")
                            return@handler
                        }
                    }
                    val players =
                        loc.world.players.map { p -> DistancedPlayer(p, loc.distance(p.location)) }.sortedBy { it }
                }
        }
    }
}

data class DistancedPlayer(val player: Player, val distance: Double) : Comparable<DistancedPlayer> {
    override fun compareTo(other: DistancedPlayer) = distance.compareTo(other.distance)
}
