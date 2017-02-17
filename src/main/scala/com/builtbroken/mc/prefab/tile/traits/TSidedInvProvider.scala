package com.builtbroken.mc.prefab.tile.traits

import com.builtbroken.mc.api.tile.IInventoryProvider
import com.builtbroken.mc.prefab.tile.module.TileModuleInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{ISidedInventory, IInventory}
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection


/**
 * Created by Cow Pi on 8/10/2015.
 */
trait TSidedInvProvider[I <: IInventory] extends IInventoryProvider[I] with ISidedInventory
{
  protected var inventory_module: I;

  def getInventory: I = {
    return inventory_module
  }

  def canStore(stack: ItemStack, slot: Int, side: ForgeDirection): Boolean = {
    return false
  }

  def canRemove(stack: ItemStack, slot: Int, side: ForgeDirection): Boolean = {
    return false
  }

  //==================================
  //====== Inventory redirects =======
  //==================================

  def getAccessibleSlotsFromSide(side: Int): Array[Int] = {
    if (getInventory.isInstanceOf[ISidedInventory]) {
      return (getInventory.asInstanceOf[ISidedInventory]).getAccessibleSlotsFromSide(side)
    }
    return new Array[Int](0)
  }

  def canInsertItem(slot: Int, itemStack: ItemStack, side: Int): Boolean = {
    if (getInventory.isInstanceOf[ISidedInventory]) {
      return (getInventory.asInstanceOf[ISidedInventory]).canInsertItem(slot, itemStack, side)
    }
    return false
  }

  def canExtractItem(slot: Int, itemStack: ItemStack, side: Int): Boolean = {
    if (getInventory.isInstanceOf[ISidedInventory]) {
      return (getInventory.asInstanceOf[ISidedInventory]).canExtractItem(slot, itemStack, side)
    }
    return false
  }

  def getSizeInventory: Int = {
    if (getInventory != null) {
      return getInventory.getSizeInventory
    }
    return 0
  }

  def getStackInSlot(slot: Int): ItemStack = {
    if (getInventory != null) {
      return getInventory.getStackInSlot(slot)
    }
    return null
  }

  def decrStackSize(slot: Int, amount: Int): ItemStack = {
    if (getInventory != null) {
      return getInventory.decrStackSize(slot, amount)
    }
    return null
  }

  def getStackInSlotOnClosing(slot: Int): ItemStack = {
    if (getInventory != null) {
      return getInventory.getStackInSlotOnClosing(slot)
    }
    return null
  }

  def setInventorySlotContents(slot: Int, stack: ItemStack) {
    if (getInventory != null) {
      getInventory.setInventorySlotContents(slot, stack)
    }
  }

  def getInventoryName: String = {
    if (getInventory != null) {
      return getInventory.getInventoryName
    }
    return ""
  }

  def hasCustomInventoryName: Boolean = {
    if (getInventory != null) {
      return getInventory.hasCustomInventoryName
    }
    return false
  }

  def getInventoryStackLimit: Int = {
    if (getInventory != null) {
      return getInventory.getInventoryStackLimit
    }
    return 0
  }

  def isUseableByPlayer(player: EntityPlayer): Boolean = {
    if (getInventory != null) {
      return getInventory.isUseableByPlayer(player)
    }
    return false
  }

  def openInventory {
    if (getInventory != null) {
      getInventory.openInventory
    }
  }

  def closeInventory {
    if (getInventory != null) {
      getInventory.closeInventory
    }
  }

  def isItemValidForSlot(slot: Int, stack: ItemStack): Boolean = {
    if (getInventory != null) {
      return getInventory.isItemValidForSlot(slot, stack)
    }
    return false
  }
}
