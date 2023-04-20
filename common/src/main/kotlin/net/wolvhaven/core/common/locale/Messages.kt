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

package net.wolvhaven.core.common.locale

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.*
import net.kyori.adventure.text.format.NamedTextColor.*
import net.kyori.adventure.text.format.Style.style
import net.kyori.adventure.text.format.TextDecoration.BOLD
import net.kyori.adventure.text.format.TextDecoration.ITALIC
import net.wolvhaven.core.common.player.WhPlayer
import net.wolvhaven.core.common.player.WhUser

object Messages {
    val PREFIX = prefix()
    val CUSTOM_PREFIX: Args1<Component?> = {
        prefix(it)
    }

    object TrainDestroy {
        private val PREFIX = prefix(text("TrainDestroy"))

        /**
         * Destroy Countdown
         */
        val DESTROY_IN: Args1<String> = { time ->
            empty()
                .append(PREFIX)
                .append(text("Trains will be destroyed in "))
                .append(text(time, style(BOLD)))
        }

        /**
         * Destroy Countdown, Frequency, First Run
         */
        val INFO: Args3<String, Long, Long> = { destroyIn, frequency, firstRun ->
            empty()
                .color(GRAY)
                .append(DESTROY_IN(destroyIn))
                .append(newline())
                .append(PREFIX)
                .append(text("Train destroy will run every "))
                .append(text(frequency, style(BOLD)))
                .append(text(" minutes"))
                .append(newline())
                .append(PREFIX)
                .append(text("Train destroy will run "))
                .append(text(firstRun, style(BOLD)))
                .append(text(" minutes after restarts"))
        }

        /**
         * User, Delay
         */
        val DELAYED: Args2<WhUser, Long> = { user, delay ->
            empty()
                .append(PREFIX)
                .append(text("Train destroy has been delayed $delay minutes by ${user.name}", GREEN))
        }

        /**
         * Countdown
         */
        val WARNING: Args1<String> = {
            empty()
                .color(RED)
                .append(DESTROY_IN(it))
                .append(newline())
                .append(PREFIX)
                .append(text("Please do not board a coming train."))
                .append(newline())
                .append(PREFIX)
                .append(text("Those on trains, please alight at the next platform."))
        }

        val RAN: Args0 = {
            empty()
                .color(RED)
                .append(PREFIX)
                .append(text("Trains have been destroyed."))
        }
    }

    object Core {
        val BROADCAST: Args3<Boolean, Component?, Component> = { prefix, custom, content ->
            if (prefix || custom != null) empty().append(prefix(custom)).append(content)
            else content
        }
    }

    object AntiGmspTp {
        val DENY: Args0 = {
            empty()
                .append(prefix())
                .color(RED)
                .append(text("Sorry, you can't teleport using spectator mode!"))
        }
    }

    object CPolicing {
        private val PREFIX = prefix(text("CPolice"))

        /**
         * Can enable?
         */
        val DISABLED: Args1<Boolean> = {
            val tmp = empty()
                .color(RED)
                .append(PREFIX)
                .append(text("Community Policing is currently disabled."))
            if (it) tmp.append(text("It can be enabled with /cpolicing enable", GRAY))
            else tmp
        }

        val NOW_ENABLED: Args0 = {
            empty()
                .color(GREEN)
                .append(PREFIX)
                .append(text("Community Policing is now enabled!"))
        }

        val ALREADY_ENABLED: Args0 = {
            empty()
                .color(BLUE)
                .append(PREFIX)
                .append(text("Community Policing is already enabled!"))
        }

        val NOW_DISABLED: Args0 = {
            empty()
                .color(GREEN)
                .append(PREFIX)
                .append(text("Community Policing is now disabled!"))
        }

        val ALREADY_DISABLED: Args0 = {
            empty()
                .color(BLUE)
                .append(PREFIX)
                .append(text("Community Policing is already disabled!"))
        }

        /**
         * Exempt Player
         */
        val PLAYER_EXEMPT: Args1<WhPlayer<*>> = {
            empty()
                .color(RED)
                .append(PREFIX)
                .append(text(it.name, style(BOLD)))
                .append(text(" is exempt from voting!"))
        }

        /**
         * Voted Player
         */
        val ALREADY_VOTED: Args1<WhPlayer<*>> = {
            empty()
                .color(RED)
                .append(PREFIX)
                .append(text("You've already voted for "))
                .append(text(it.name, style(BOLD)))
                .append(text("!"))
        }

        /**
         * Voter, Voted, votes, threshold
         */
        val VOTED: Args4<WhPlayer<*>, WhPlayer<*>, Int, Int> = { voter, voted, votes, threshold ->
            empty()
                .color(GREEN)
                .append(PREFIX)
                .append(text(voter.name, style(BOLD)))
                .append(text(" has voted for "))
                .append(text(voted.name, style(BOLD)))
                .append(text("! [$votes/$threshold]")) // Not a fan of this, but not sure a better way
        }

        /**
         * Banned Player
         */
        val BANNED: Args1<WhPlayer<*>> = {
            empty()
                .color(GREEN)
                .append(PREFIX)
                .append(text(it.name, style(BOLD)))
                .append(text(" has been banned."))
        }
    }

    object InvisibleItemFrames {
        /**
         * Is glow
         */
        val ITEM_NAME: Args1<Boolean> = {
            empty()
                .color(AQUA)
                .style(style(ITALIC))
                .append(text("Invisible", style(BOLD)))
                .append(if (it) text(" Glow ") else space())
                .append(text(" Item Frame"))
        }

        /**
         * Is glow
         */
        val GIVE_SUCCESS: Args1<Boolean> = {
            empty()
                .color(GREEN)
                .append(prefix())
                .append(text("Gave you an "))
                .append(ITEM_NAME(it))
                .append(text("!"))
        }

        val GIVE_FAIL: Args0 = {
            empty()
                .color(RED)
                .append(prefix())
                .append(text("Couldn't give you the item! Is your inventory full?"))
        }
    }
}

/**
* Not to be used except by other Message classes
*/
fun prefix(type: Component? = null): Component {
    return if (type == null)
        text("[", GRAY, BOLD)
            .append(text("WH", GOLD)).append(text("]"))
            .append(space())
    else
        text("[", GRAY, BOLD)
            .append(text("WH", GOLD)).append(text(" | "))
            .append(type.colorIfAbsent(GOLD)).append(text("]"))
            .append(space())
}

/**
 * Not to be used except by other Message classes
 */
typealias Args0 = () -> Component
/**
 * Not to be used except by other Message classes
 */
typealias Args1<A1> = (A1) -> Component
/**
 * Not to be used except by other Message classes
 */
typealias Args2<A1, A2> = (A1, A2) -> Component
/**
 * Not to be used except by other Message classes
 */
typealias Args3<A1, A2, A3> = (A1, A2, A3) -> Component
/**
 * Not to be used except by other Message classes
 */
typealias Args4<A1, A2, A3, A4> = (A1, A2, A3, A4) -> Component
/**
 * Not to be used except by other Message classes
 */
typealias Args5<A1, A2, A3, A4, A5> = (A1, A2, A3, A4, A5) -> Component
/**
 * Not to be used except by other Message classes
 */
typealias Args6<A1, A2, A3, A4, A5, A6> = (A1, A2, A3, A4, A5, A6) -> Component
