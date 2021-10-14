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

import cloud.commandframework.arguments.standard.IntegerArgument
import cloud.commandframework.arguments.standard.StringArgument
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.wolvhaven.core.common.server.server
import net.wolvhaven.core.common.util.CommandCreatorFunction
import net.wolvhaven.core.common.util.buildCommand
import net.wolvhaven.core.common.util.isTrainDriver
import net.wolvhaven.core.common.util.onlinePlayers
import net.wolvhaven.core.plugin.WhCorePlugin
import org.bukkit.Location
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class TrainLogging(private val plugin: WhCorePlugin) : WhModule {
    private val permRoot = "${net.wolvhaven.core.plugin.WhCorePlugin.permRoot}.trainlog"
    private val permissionNotify = "$permRoot.notify"
    private val permissionTrigger = "$permRoot.trigger"

    init {
        val base: CommandCreatorFunction<CommandSender> = {
            it.commandBuilder("trainlog").permission(permissionTrigger)
        }

        plugin.commandManager.buildCommand(base) { b ->
            b
                .literal("pay")
                .argument(StringArgument.of("line"))
                .argument(IntegerArgument.newBuilder<CommandSender>("amount").withMin(0))
                .handler {
                    val loc = when (val sender = it.sender) {
                        is BlockCommandSender -> {
                            sender.block.location
                        }
                        is Entity -> {
                            sender.location
                        }
                        else -> {
                            sender.sendMessage("Only CommandSenders with locations can use this!")
                            return@handler
                        }
                    }
                    runPay(loc, it["amount"], it["amount"])
                }
        }
    }

    fun runPay(loc: Location, line: String, amount: Int) {
        val paid = playersByLocation(loc)[0]
        var flag = false
        if (paid.distance >= 20) flag = true
        if (!paid.player.isTrainDriver) flag = true

        log("${paid.player.name} has been paid \$$amount for driving $line",
            "D: ${paid.distance}\nDriver: ${paid.player.isTrainDriver}"
        )
    }

    private fun log(main: String, info: String, color: TextColor = NamedTextColor.GREEN) {
        val audience = onlinePlayers.withPermission(permissionNotify).toAudienceCollection()
            .also { it.add(server.consoleSender) }

        audience.sendMessage(
            prefixed(
                text(main)
                    .color(color)
                    .hoverEvent(HoverEvent.showText(text(info)))
            )
        )
    }

    private fun prefixed(component: Component) = net.wolvhaven.core.common.util.prefixed(text("TrainLog"), component)

    private fun playersByLocation(loc: Location) = loc.world.players
        .map { p -> DistancedPlayer(p, loc.distance(p.location)) }
        .sortedBy { it }
}

data class DistancedPlayer(val player: Player, val distance: Double) : Comparable<DistancedPlayer> {
    override fun compareTo(other: DistancedPlayer) = distance.compareTo(other.distance)
}
