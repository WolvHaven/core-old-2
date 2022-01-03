/*
 * WHCore - Core features for the WolvHaven server
 *  Copyright (C) 2021 Underscore11
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wolvhaven.core.velocity

import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.velocity.VelocityCommandManager
import com.google.inject.Inject
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import net.wolvhaven.core.common.util.value
import java.nio.file.Path

@Plugin(id = "WHCore", version = "Unknown", authors = ["Underscore11"])
class WhVelocityPlugin @Inject constructor(val server: ProxyServer, @DataDirectory val dataDir: Path) {

    val commandManager get() = if (_commandManager != null) _commandManager
    else throw IllegalStateException("Attempt to get commandManager before init!")

    private var _commandManager: VelocityCommandManager<CommandSource>? = null

    @Subscribe
    fun onInit(e: ProxyInitializeEvent) {
        _commandManager = VelocityCommandManager(
            server.pluginManager.getPlugin("WHCore").value ?: throw IllegalStateException(),
            server,
            CommandExecutionCoordinator.simpleCoordinator(),
            { it },
            { it }
        )
    }
}
