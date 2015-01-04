package com.builtbroken.lib.utility.inventory

import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import com.builtbroken.api.tile.IInventoryProvider
import com.builtbroken.lib.transform.vector.Vector3

/**
 * Some extra inventory prefab methods.
 * @author Calclavia
 */
trait TPrefabInventory extends TileEntity with IInventoryProvider
{
  //TODO: Inventory Utility?
  /**
   * Adds an ItemStack into this inventory.
   * @return The remaining stack
   */
  def addStackToInventory(slot: Int, itemStack: ItemStack): ItemStack =
  {
    if (slot < getInventory().getSizeInventory())
    {
      var stackInInventory = getInventory().getStackInSlot(slot)

      if (stackInInventory == null)
      {
        getInventory().setInventorySlotContents(slot, itemStack)

        if (getInventory().getStackInSlot(slot) == null)
        {
          return itemStack
        }

        return null
      }
      else if (stackInInventory.isItemEqual(itemStack) && stackInInventory.isStackable)
      {
        stackInInventory = stackInInventory.copy()
        val stackLim: Int = Math.min(getInventory().getInventoryStackLimit, itemStack.getMaxStackSize)
        val rejectedAmount: Int = Math.max((stackInInventory.stackSize + itemStack.stackSize) - stackLim, 0)
        stackInInventory.stackSize = Math.min(Math.max((stackInInventory.stackSize + itemStack.stackSize - rejectedAmount), 0), getInventory().getInventoryStackLimit)
        itemStack.stackSize = rejectedAmount
        getInventory().setInventorySlotContents(slot, stackInInventory)
      }
    }

    if (itemStack.stackSize <= 0)
    {
      return null
    }

    return itemStack
  }

  def mergeIntoInventory(itemStack: ItemStack): Boolean =
  {
    var returnStack = itemStack

    if (!getWorldObj().isRemote)
    {
      for (direction <- ForgeDirection.VALID_DIRECTIONS)
      {
        if (returnStack != null)
        {
          returnStack = tryPlaceInPosition(returnStack, new Vector3(this) + direction, direction)
        }
      }

      if (returnStack != null)
      {
        getWorldObj().spawnEntityInWorld(new EntityItem(getWorldObj(), this.xCoord + 0.5, this.yCoord + 1, this.zCoord + 0.5, itemStack))
      }
    }
    return false
  }

  /**
   * Tries to place an itemStack in a specific position if it is an inventory.
   *
   * @return The ItemStack remained after place attempt
   */
  def tryPlaceInPosition(itemStack: ItemStack, position: Vector3, dir: ForgeDirection): ItemStack =
  {
    val tileEntity = position.getTileEntity(getWorldObj())

    if (tileEntity.isInstanceOf[IInventory])
      return InventoryUtility.putStackInInventory(tileEntity.asInstanceOf[IInventory], itemStack, false)

    return itemStack
  }
}
