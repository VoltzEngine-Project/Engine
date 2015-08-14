package com.builtbroken.mc.prefab.tile.multiblock.types

import com.builtbroken.mc.api.tile.IInventoryProvider
import com.builtbroken.mc.api.tile.multiblock.IMultiTileInvHost
import com.builtbroken.mc.prefab.tile.multiblock.TileMulti
import com.builtbroken.mc.prefab.tile.traits.TSidedInvProvider
import net.minecraft.inventory.IInventory

/**
 * Created by Cow Pi on 8/10/2015.
 */
class TileMultiInv extends TileMulti with TSidedInvProvider {

  override def getInventory: IInventory = {
    if (getHost != null) {
      if (getHost.isInstanceOf[IMultiTileInvHost]) {
        return getHost.asInstanceOf[IMultiTileInvHost].getInventoryForTile(this)
      }
      else if (getHost.isInstanceOf[IInventoryProvider]) {
        return getHost.asInstanceOf[IInventoryProvider].getInventory
      }
    }
    return inventory_module
  }
}
