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

package net.wolvhaven.core.plugin

import net.wolvhaven.core.common.paper.WhPaperPlugin
import net.wolvhaven.core.plugin.modules.AntiGmspTp
import net.wolvhaven.core.plugin.modules.CPolicing
import net.wolvhaven.core.plugin.modules.Core
import net.wolvhaven.core.plugin.modules.InvisibleItemFrames
import net.wolvhaven.core.plugin.modules.TrainDestroy
import net.wolvhaven.core.plugin.modules.WhModule
import net.wolvhaven.core.plugin.modules.placeholders.WhPlaceholders

@Suppress("unused")
class WhCorePlugin(bootstrap: WhCoreBootstrap) : WhPaperPlugin(bootstrap) {
    private val modules = ArrayList<WhModule>()
    init {
        modules.addAll(
            listOf(
                TrainDestroy(this),
                CPolicing(this),
                Core(this),
                WhPlaceholders(this),
                AntiGmspTp(this),
                InvisibleItemFrames(this)
            )
        )
    }

    override fun disable() {
        modules.forEach(WhModule::disable)
        modules.clear()
    }

    companion object {
        val permRoot = "whcore"
    }
}
