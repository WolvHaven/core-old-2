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

package net.wolvhaven.core.common

import cloud.commandframework.CommandManager
import net.wolvhaven.core.common.player.WhUser
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

abstract class WhPlugin(val bootstrap: WhBootstrap) {
    abstract val commandManager: CommandManager<WhUser>
    val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    abstract fun disable()

    fun reload() {
        bootstrap.reload()
    }
}
