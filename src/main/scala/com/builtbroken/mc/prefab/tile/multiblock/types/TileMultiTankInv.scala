package com.builtbroken.mc.prefab.tile.multiblock.types

import com.builtbroken.mc.api.tile.IInventoryProvider
import com.builtbroken.mc.api.tile.multiblock.IMultiTileInvHost
import com.builtbroken.mc.prefab.tile.traits.TSidedInvProvider
import net.minecraft.inventory.IInventory

/**
  * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
  *      Created by Dark(DarkGuardsman, Robert) on 2/17/2017.
  */
class TileMultiTankInv extends TileMultiTank with TSidedInvProvider[IInventory] {

  override def getInventory: IInventory = {
    if (getHost != null) {
      if (getHost.isInstanceOf[IMultiTileInvHost]) {
        return getHost.asInstanceOf[IMultiTileInvHost].getInventoryForTile(this)
      }
      else if (getHost.isInstanceOf[IInventoryProvider[IInventory]]) {
        return getHost.asInstanceOf[IInventoryProvider[IInventory]].getInventory
      }
    }
    return inventory_module
  }

  override protected var inventory_module: IInventory = null
}
