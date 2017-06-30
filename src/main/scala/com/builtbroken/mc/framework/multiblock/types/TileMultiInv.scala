package com.builtbroken.mc.framework.multiblock.types

import com.builtbroken.mc.api.tile.multiblock.IMultiTileInvHost
import com.builtbroken.mc.api.tile.provider.IInventoryProvider
import com.builtbroken.mc.framework.multiblock.TileMulti
import com.builtbroken.mc.prefab.tile.traits.TSidedInvProvider
import net.minecraft.inventory.IInventory

/**
 * Created by Cow Pi on 8/10/2015.
 */
class TileMultiInv extends TileMulti with TSidedInvProvider[IInventory] {

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
