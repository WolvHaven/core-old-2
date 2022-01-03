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

package net.wolvhaven.core.common.player

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.identity.Identified
import net.kyori.adventure.text.Component
import net.luckperms.api.model.user.User
import java.util.*

interface WhPlayer<P> : WhUser, Identified where P : Audience, P : Identified {
    val wrapped: P
    val uuid: UUID
    var nickname: Component?
    var vanished: Boolean
    var balance: Float
    val asLuckperms: User
}

abstract class AbstractWhPlayer<P>(override val wrapped: P) : WhPlayer<P>, ForwardingAudience.Single where P : Audience, P : Identified {
    override fun audience() = wrapped
    override fun identity() = wrapped.identity()
    override val uuid get() = identity().uuid()
}
