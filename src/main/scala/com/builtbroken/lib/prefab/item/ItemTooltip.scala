package com.builtbroken.lib.prefab.item

import java.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}

/**
 * extend this instead of Item, gives the basis to Special Tooltip behaviour
 *
 * @author tgame14
 * @since 06/04/14
 */
class ItemTooltip extends Item with TItemToolTip
{
  override def addInformation(itemStack: ItemStack, par2EntityPlayer: EntityPlayer, list: util.List[_], par4: Boolean)
  {
    super.addInformation(itemStack, par2EntityPlayer, list, par4)
  }
}