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

import cloud.commandframework.arguments.flags.CommandFlag
import cloud.commandframework.kotlin.extension.senderType
import net.wolvhaven.core.common.locale.Messages
import net.wolvhaven.core.common.paper.WhPaperPlayer
import net.wolvhaven.core.common.util.buildCommand
import net.wolvhaven.core.plugin.WhCorePlugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

// This class largely inspired by https://github.com/techchrism/survival-invisiframes/blob/9350fa3794cf958c3d248e432a0511619ed4cc5d/src/main/java/com/darkender/plugins/survivalinvisiframes/SurvivalInvisiframes.java
class InvisibleItemFrames(val plugin: WhCorePlugin) : WhModule, Listener {
    private val permissionRoot = "${net.wolvhaven.core.plugin.WhCorePlugin.permRoot}.item"
    private val invisItemFrameKey = NamespacedKey(plugin.paperBootstrap, "invisible")

    init {
        plugin.paperBootstrap.server.pluginManager.registerEvents(this, plugin.paperBootstrap)
        plugin.commandManager.buildCommand({
            it.commandBuilder("itemframe", "if")
        }) { b ->
            b
                .flag(CommandFlag.newBuilder("glow"))
                .permission("$permissionRoot.itemframe")
                .senderType(WhPaperPlayer::class)
                .handler {
                    val p = it.sender as WhPaperPlayer
                    val glow = it.flags().isPresent("glow")
                    val stack = ItemStack(if (glow) Material.GLOW_ITEM_FRAME else Material.ITEM_FRAME)
                    stack.editMeta { m ->
                        m.persistentDataContainer[invisItemFrameKey, PersistentDataType.BYTE] = 1
                        m.displayName(Messages.InvisibleItemFrames.ITEM_NAME(glow))
                        m.addEnchant(Enchantment.DURABILITY, 1, true)
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    }
                    if (p.wrapped.inventory.addItem(stack).isEmpty()) {
                        p.sendMessage(Messages.InvisibleItemFrames.GIVE_SUCCESS(glow))
                    } else {
                        p.sendMessage(Messages.InvisibleItemFrames.GIVE_FAIL())
                    }
                }
        }
    }

    @EventHandler
    fun onHang(e: HangingPlaceEvent) {
        if (e.entity !is ItemFrame) return
        val stack = e.itemStack ?: return
        if (stack.itemMeta.persistentDataContainer[invisItemFrameKey, PersistentDataType.BYTE] != 1.toByte()) return
        (e.entity as ItemFrame).isVisible = false
    }
}
