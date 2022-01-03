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

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.*
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.NamedTextColor.GREEN
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.wolvhaven.core.common.paper.server.server
import net.wolvhaven.core.common.player.WhUser
import net.wolvhaven.core.common.util.CommandCreatorFunction
import net.wolvhaven.core.common.util.buildCommand
import net.wolvhaven.core.common.util.minuteSecond
import net.wolvhaven.core.plugin.WhCorePlugin
import net.wolvhaven.core.plugin.config
import org.bukkit.scheduler.BukkitTask
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class TrainDestroy(plugin: WhCorePlugin) : WhModule {
    private val task: BukkitTask = plugin.server.scheduler.runTaskTimer(plugin, this::run, 10, 10)
    val config = config<TrainDestroyConfig>("trainDestroy", plugin).also {
        it.load()
        it.save()
    }

    val permRoot = "${net.wolvhaven.core.plugin.WhCorePlugin.permRoot}.traindestroy"
    val permissionDelay = "$permRoot.delay"
    val permissionNow = "$permRoot.now"

    var nextRun: Instant = Instant.now().plus(config().firstRun, ChronoUnit.MINUTES)
    var hasNotified = false
    val timeRemaining: String get() = minuteSecond.format(nextRun.minus(System.currentTimeMillis(), ChronoUnit.MILLIS).atOffset(ZoneOffset.UTC))
    val trainsWillBeDestroyedIn: TextComponent
        get() = text("Trains will be destroyed in ")
            .append(text(timeRemaining, Style.style(TextDecoration.BOLD)))

    init {
        val base: CommandCreatorFunction<WhUser> = {
            it.commandBuilder("traindestroy")
        }

        plugin.commandManager.buildCommand(base) { b ->
            b
                .literal("now")
                .permission(permissionNow)
                .handler {
                    nextRun = Instant.now().minus(1, ChronoUnit.MILLIS)
                }
        }
        plugin.commandManager.buildCommand(base) { b ->
            b
                .literal("info", "i", "when")
                .handler {
                    it.sender.sendMessage(
                        empty().color(NamedTextColor.GRAY)
                            .append(prefixed(trainsWillBeDestroyedIn))
                            .append(newline())
                            .append(prefixed(text("Train destroy will run every ${config().frequency} minutes")))
                            .append(newline())
                            .append(prefixed(text("Train destroy will run ${config().firstRun} minutes after restarts")))
                    )
                }
        }
        plugin.commandManager.buildCommand(base) { b ->
            b
                .literal("delay")
                .permission(permissionDelay)
                .handler {
                    nextRun = nextRun.plus(config().delay, ChronoUnit.MINUTES)
                    server.sendMessage(prefixed(text("Train destroy has been delayed ${config().delay} minutes by ${it.sender.name}", GREEN)))
                }
        }
    }

    override fun disable() {
        task.cancel()
    }

    private fun run() {
        val now = Instant.now()
        if (now.isAfter(nextRun)) {
            config().commands.forEach {
                server.dispatchCommand(server.consoleSender, it)
            }
            server.sendMessage(prefixed(text("Trains have been destroyed.", NamedTextColor.RED)))

            nextRun = now.plus(config().frequency, ChronoUnit.MINUTES)
            hasNotified = false
            return
        }

        if (now.isAfter(nextRun.minus(config().warning, ChronoUnit.MINUTES)) && !hasNotified) {

            server.sendMessage(
                empty().color(NamedTextColor.RED)
                    .append(prefixed(trainsWillBeDestroyedIn))
                    .append(newline())
                    .append(prefixed(text("Please do not board a coming train.")))
                    .append(newline())
                    .append(prefixed(text("Those on trains, please alight at the next platform.")))
            )

            hasNotified = true
            return
        }
    }

    private fun prefixed(component: Component): Component {
        return net.wolvhaven.core.common.util.prefixed(text("TrainDestroy"), component)
    }
}

@ConfigSerializable
data class TrainDestroyConfig(
    val frequency: Long = 60,
    val warning: Long = 3,
    val firstRun: Long = 6,
    val delay: Long = 15,
    val commands: List<String> = listOf("say Change this in the config!")
)
