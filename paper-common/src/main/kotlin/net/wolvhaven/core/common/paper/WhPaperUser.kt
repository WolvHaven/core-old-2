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

import net.kyori.adventure.text.Component
import net.luckperms.api.model.user.User
import net.wolvhaven.core.common.paper.plugins.WhLuckperms.luckperms
import net.wolvhaven.core.common.player.AbstractWhPlayer
import net.wolvhaven.core.common.player.WhUser
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WhPaperUser(val commandSender: CommandSender) : WhUser {
    override val name get() = commandSender.name
    override fun hasPermission(permission: String) = commandSender.hasPermission(permission)
}

class WhPaperPlayer(wrapped: Player) : AbstractWhPlayer<Player>(wrapped) {
    override val name get() = wrapped.name
    override var nickname: Component?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var vanished: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    override var balance: Float
        get() = TODO("Not yet implemented")
        set(value) {}
    override val asLuckperms: User
        get() = wrapped.luckperms

    override fun hasPermission(permission: String) = wrapped.hasPermission(permission)
}
