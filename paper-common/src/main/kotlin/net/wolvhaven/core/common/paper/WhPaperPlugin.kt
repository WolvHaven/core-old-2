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

package net.wolvhaven.core.common.paper

import cloud.commandframework.CommandManager
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import net.wolvhaven.core.common.WhPlugin
import net.wolvhaven.core.common.player.WhUser
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

abstract class WhPaperPlugin(val paperBootstrap: WhPaperBootstrap) : WhPlugin(paperBootstrap), Plugin by paperBootstrap {
    override val commandManager: CommandManager<WhUser> = PaperCommandManager(
        paperBootstrap,
        CommandExecutionCoordinator.simpleCoordinator(),
        {
            when (it) {
                is Player -> WhPaperPlayer(it)
                else -> WhPaperUser(it)
            }
        },
        {
            when (it) {
                is WhPaperPlayer -> it.wrapped
                is WhPaperUser -> it.commandSender
                else -> null
            }
        }
    )
}
