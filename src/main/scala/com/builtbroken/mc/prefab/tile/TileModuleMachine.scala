package com.builtbroken.mc.prefab.tile

import com.builtbroken.mc.prefab.tile.module.TileModuleInventory
import com.builtbroken.mc.prefab.tile.traits.TSidedInvProvider
import net.minecraft.block.material.Material

/**
 * Created by Cow Pi on 8/10/2015.
 */
class TileModuleMachine(name: String, material: Material) extends TileModuleMachineBase(name, material) with TSidedInvProvider
{
  def addInventoryModule(size: Int): Boolean = {
    super.readFromNBT()
    if (inventory_module == null) {
      inventory_module = new TileModuleInventory(this, size)
      modules.add(inventory_module)
      return true
    }
    return false
  }
}
