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

package net.wolvhaven.core.common

import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.function.Function

abstract class WhPlugin : JavaPlugin() {
    private var _commandManager: PaperCommandManager<CommandSender>? = null

    val commandManager: PaperCommandManager<CommandSender>
        get() = _commandManager ?: throw IllegalStateException("Attempted to get command manager before it was enabled!")

    private var _executorService: ScheduledExecutorService? = null

    val executorService: ScheduledExecutorService
        get() = _executorService ?: throw IllegalStateException("Attempted to get executor service before it was enabled!")

    final override fun onEnable() {
        _commandManager = PaperCommandManager(
            this,
            CommandExecutionCoordinator.simpleCoordinator(),
            Function.identity(),
            Function.identity()
        )
        _executorService = Executors.newSingleThreadScheduledExecutor()

        enable()
    }

    final override fun onDisable() {
        disable()

        executorService.shutdown()

        _commandManager = null
        _executorService = null
    }

    fun reload() {
        isEnabled = false
        isEnabled = true
    }

    abstract fun enable()
    abstract fun disable()
}
