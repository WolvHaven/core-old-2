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

package net.wolvhaven.core.common.util

import cloud.commandframework.Command
import cloud.commandframework.CommandManager

fun <C : Any> CommandManager<C>.buildCommand(base: Command.Builder<C>, build: CommandModifierFunction<C>) {
    this.buildCommand(base, listOf(build))
}

fun <C : Any> CommandManager<C>.buildCommand(base: CommandCreatorFunction<C>, build: CommandModifierFunction<C>) {
    this.buildCommand(base, listOf(build))
}

fun <C : Any> CommandManager<C>.buildCommand(base: Command.Builder<C>, vararg build: CommandModifierFunction<C>) {
    this.buildCommand(base, listOf(*build))
}

fun <C : Any> CommandManager<C>.buildCommand(base: CommandCreatorFunction<C>, vararg build: CommandModifierFunction<C>) {
    this.buildCommand(base, listOf(*build))
}

fun <C : Any> CommandManager<C>.buildCommand(base: CommandCreatorFunction<C>, build: List<CommandModifierFunction<C>>) {
    var command = base(this)

    build.forEach { command = it(command) }

    this.command(command)
}

fun <C : Any> CommandManager<C>.buildCommand(base: Command.Builder<C>, build: List<CommandModifierFunction<C>>) {
    this.buildCommand({ base }, build)
}

typealias CommandCreatorFunction<C> = (CommandManager<C>) -> Command.Builder<C>
typealias CommandModifierFunction<C> = (Command.Builder<C>) -> Command.Builder<C>
