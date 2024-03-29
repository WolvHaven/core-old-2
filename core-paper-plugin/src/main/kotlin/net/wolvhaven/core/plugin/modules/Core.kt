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

package net.wolvhaven.core.plugin.modules

import cloud.commandframework.arguments.flags.CommandFlag
import cloud.commandframework.arguments.standard.StringArgument
import net.kyori.adventure.text.minimessage.MiniMessage
import net.wolvhaven.core.common.locale.Messages
import net.wolvhaven.core.common.paper.util.isStaff
import net.wolvhaven.core.common.paper.util.playerCollection
import net.wolvhaven.core.common.player.WhUser
import net.wolvhaven.core.common.util.CommandCreatorFunction
import net.wolvhaven.core.common.util.CommandModifierFunction
import net.wolvhaven.core.common.util.Sounds
import net.wolvhaven.core.common.util.buildCommand
import net.wolvhaven.core.common.util.mapIfPresent
import net.wolvhaven.core.common.util.value
import net.wolvhaven.core.plugin.WhCorePlugin
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class Core(private val plugin: WhCorePlugin) : WhModule {
    private val permissionAdmin = "${net.wolvhaven.core.plugin.WhCorePlugin.permRoot}.admin"

    init {
        val base: CommandCreatorFunction<WhUser> = {
            it.commandBuilder("wolvhavencore", "whcore", "wh")
        }

        val mmBase: CommandCreatorFunction<WhUser> = {
            it.commandBuilder("minimessage", "mm")
        }

        val bcastBase: CommandCreatorFunction<WhUser> = {
            it.commandBuilder("bcast", "broadcast", "bc")
        }

        val bcastModifier: CommandModifierFunction<WhUser> = { b ->
            b
                .argument(StringArgument.quoted("content"))
                .permission("whcore.broadcast")
                .flag(CommandFlag.newBuilder("ding"))
                .flag(CommandFlag.newBuilder("perm").withArgument(StringArgument.of<CommandSender>("perm")))
                .flag(CommandFlag.newBuilder("staff"))
                .flag(CommandFlag.newBuilder("prefixed"))
                .flag(CommandFlag.newBuilder("customPrefix").withArgument(StringArgument.quoted<CommandSender>("prefix")))
                .handler { c ->
                    val content = MiniMessage.get().parse(c["content"])

                    val message = Messages.Core.BROADCAST(
                        c.flags().isPresent("prefixed"),
                        c.flags().getValue<String>("customPrefix").value.mapIfPresent { MiniMessage.get().parse(it) },
                        content
                    )

                    val playerCollection = if (c.flags().isPresent("staff")) Bukkit.getOnlinePlayers().filter { it.isStaff }
                    else if (c.flags().getValue<String>("perm").isPresent) Bukkit.getOnlinePlayers().filter { it.hasPermission(c.flags().getValue<String>("perm").orElse("aboajfhawlkdfalfw")) }
                    else Bukkit.getOnlinePlayers()

                    val players = playerCollection.toMutableSet().playerCollection

                    players.sendMessage(message)
                    Bukkit.getServer().consoleSender.sendMessage(message)

                    if (c.flags().isPresent("ding")) players.playSound(Sounds.DING.sound)
                }
        }

        plugin.commandManager.buildCommand(mmBase, { b ->
            b.literal("bcast", "broadcast", "bc") // "broadcast" as the main for some reason gives IJ a heart attack.
        }, bcastModifier)

        plugin.commandManager.buildCommand(bcastBase, bcastModifier)
    }
}
