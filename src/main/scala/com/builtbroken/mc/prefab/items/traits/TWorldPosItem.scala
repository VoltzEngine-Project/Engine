package com.builtbroken.mc.prefab.items.traits

import com.builtbroken.mc.api.IWorldPosition
import com.builtbroken.mc.api.items.tools.IWorldPosItem
import com.builtbroken.mc.lib.transform.vector.Location
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/** Prefab trait for { @link com.builtbroken.mc.api.items.tools.IWorldPosItem }
  * Created by Dark on 8/25/2015.
  */
trait TWorldPosItem extends IWorldPosItem {
  def getLocation(stack: ItemStack): Location = {
    if (stack.hasTagCompound && stack.getTagCompound.hasKey("linkPos")) {
      return new Location(stack.getTagCompound.getCompoundTag("linkPos"))
    }
    return null
  }

  def setLocation(stack: ItemStack, loc: IWorldPosition) {
      if (!stack.hasTagCompound) stack.setTagCompound(new NBTTagCompound)
      stack.getTagCompound.setTag("linkPos", new Location(loc).toNBT)
  }

  def canAccessLocation(stack: ItemStack, obj: AnyRef): Boolean = {
    return false
  }
}
