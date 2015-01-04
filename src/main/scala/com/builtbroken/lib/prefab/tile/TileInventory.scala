package com.builtbroken.lib.prefab.tile

import net.minecraft.block.material.Material
import com.builtbroken.lib.content.prefab.TInventory

abstract class TileInventory(material: Material) extends TileAdvanced(material: Material) with TInventory
{
  var maxSlots = 0

  override def getSizeInventory = maxSlots

  def setSizeInventory(slots: Int)
  {
      maxSlots = slots
  }

}