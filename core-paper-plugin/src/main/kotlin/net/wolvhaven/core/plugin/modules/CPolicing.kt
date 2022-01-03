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

import cloud.commandframework.bukkit.parsers.PlayerArgument
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.empty
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.*
import net.wolvhaven.core.common.paper.WhPaperPlayer
import net.wolvhaven.core.common.paper.server.server
import net.wolvhaven.core.common.player.WhUser
import net.wolvhaven.core.common.util.CommandCreatorFunction
import net.wolvhaven.core.common.util.CommandModifierFunction
import net.wolvhaven.core.common.util.buildCommand
import net.wolvhaven.core.common.util.logger
import net.wolvhaven.core.plugin.WhCorePlugin
import net.wolvhaven.core.plugin.config
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.spongepowered.configurate.objectmapping.ConfigSerializable

class CPolicing(plugin: WhCorePlugin) : WhModule {
    val votes = HashMap<Player, MutableList<Player>>()
    val config = config<CPolicingConfig>("cpolicing", plugin).also {
        it.load()
        it.save()
    }

    private val permRoot = "${net.wolvhaven.core.plugin.WhCorePlugin.permRoot}.cpolicing"
    private val permissionExempt = "$permRoot.exempt"
    private val permissionVote = "$permRoot.vote"
    private val permissionAdmin = "$permRoot.admin"

    private val threshold: Int get() = (server.onlinePlayers.size * config().percentRequired).toInt()

    init {
        val base: CommandCreatorFunction<WhUser> = {
            it.commandBuilder("cpolicing", "cpolice")
        }

        plugin.commandManager.buildCommand(base) { b ->
            b
                .literal("enable")
                .permission(permissionAdmin)
                .handler {
                    if (config().enabled)
                        return@handler it.sender.sendMessage(prefixed(text("CPolicing is already enabled!", RED)))
                    config().enabled = true
                    config.save()
                    server.sendMessage(prefixed(text("Community Policing is now enabled.", BLUE)))
                }
        }

        plugin.commandManager.buildCommand(base) { b ->
            b
                .literal("disable")
                .permission(permissionAdmin)
                .handler {
                    if (!config().enabled)
                        return@handler it.sender.sendMessage(prefixed(text("CPolicing is already disabled!", RED)))
                    config().enabled = false
                    config.save()
                    server.sendMessage(prefixed(text("Community Policing is now disabled.", BLUE)))
                }
        }

        val vote: CommandModifierFunction<WhUser> = {
            it
                .literal("vote", "v")
                .permission(permissionVote)
                .senderType(WhPaperPlayer::class.java)
        }

        plugin.commandManager.buildCommand(
            base, vote,
            { b ->
                b
                    .argument(PlayerArgument.of("target"))
                    .handler {
                        if (!checkEnabled(it.sender)) return@handler
                        val sender = it.sender as Player
                        val target = it.get("target") as Player
                        if (target.hasPermission(permissionExempt))
                            return@handler it.sender.sendMessage(prefixed(text("${target.name} is exempt from voting!", RED)))
                        vote(sender, target)
                    }
            }
        )
    }

    fun vote(sender: Player, target: Player) {
        // votes: HashMap<UUID, MutableList<UUID>>
        val targetVotes = votes.computeIfAbsent(target) { ArrayList() }
        if (targetVotes.contains(sender))
            return sender.sendMessage(prefixed(text("You've already voted for ${target.name}!")))
        targetVotes.add(sender)
        votes[target] = targetVotes // Do I need this?
        server.sendMessage(prefixed(text("${sender.name} has voted for ${target.name} [${targetVotes.size}/$threshold]", AQUA)))
    }

    fun checkVotes() {
        votes.forEach { (k, v) ->
            if (k.hasPermission(permissionExempt)) {
                votes.remove(k)
                return@forEach
            }
            if (v.size >= threshold) {
                k.banPlayer("You have been banned by Community Policing. To report abuse, appeal@wolvhaven.net")
                logger().info("${k.name} has been banned by CPolice. Voters: ${v.joinToString { it.name }}")
                server.sendMessage(
                    empty()
                        .append(prefixed(text("${k.name} has been banned.")))
                )
            }
        }
    }

    fun checkEnabled(sender: WhUser): Boolean {
        if (config().enabled) return true
        sender.sendMessage(prefixed(text("Community Policing is currently disabled.", RED)))
        if (sender.hasPermission(permissionAdmin))
            sender.sendMessage(prefixed(text("It can be enabled with /cpolicing enable", GRAY)))
        return false
    }

    private fun prefixed(component: Component): Component {
        return net.wolvhaven.core.common.util.prefixed(text("CPolicing"), component)
    }
}

@ConfigSerializable
data class CPolicingConfig(
    val percentRequired: Float = 0.5f,
    var enabled: Boolean = false
)
