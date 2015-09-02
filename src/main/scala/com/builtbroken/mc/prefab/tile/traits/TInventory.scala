package com.builtbroken.mc.prefab.tile.traits

import com.builtbroken.mc.api.ISave
import com.builtbroken.mc.prefab.inventory.InventoryUtility
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}


/** Basic inventory implementation as a scala trait
 * Created by Robert on 8/10/2015.
 */
trait TInventory extends IInventory with ISave {
  protected var slots: Int = 0
  protected var containedItems: Array[ItemStack] = null

  def getSizeInventory: Int = slots

  def getContainedItems: Array[ItemStack] = {
    if (this.containedItems == null) {
      this.containedItems = new Array[ItemStack](this.getSizeInventory)
    }
    return this.containedItems
  }

  def getStackInSlot(slot: Int): ItemStack = {
    if (slot < getContainedItems.length) {
      return this.getContainedItems(slot)
    }
    return null
  }

  def decrStackSize(slot: Int, ammount: Int): ItemStack = {
    if (this.getContainedItems(slot) != null) {
      var var3: ItemStack = null
      if (this.getContainedItems(slot).stackSize <= ammount) {
        var3 = this.getContainedItems(slot)
        getContainedItems(slot) = null
        markDirty
        return var3
      }
      else {
        var3 = this.getContainedItems(slot).splitStack(ammount)
        if (this.getContainedItems(slot).stackSize == 0) {
          this.getContainedItems(slot) = null
        }
        markDirty
        return var3
      }
    }
    else {
      return null
    }
  }

  def getStackInSlotOnClosing(par1: Int): ItemStack = {
    if (this.getContainedItems(par1) != null) {
      val var2: ItemStack = this.getContainedItems(par1)
      this.getContainedItems(par1) = null
      return var2
    }
    else {
      return null
    }
  }

  def setInventorySlotContents(slot: Int, insertStack: ItemStack) {
    val pre_stack: ItemStack = if (getStackInSlot(slot) != null) getStackInSlot(slot).copy else null
    this.getContainedItems(slot) = insertStack
    if (!InventoryUtility.stacksMatchExact(pre_stack, getStackInSlot(slot))) {
      markDirty
    }
  }

  def getInventoryName: String = {
    return "trait.inventory.base"
  }

  def openInventory {
  }

  def closeInventory {
  }

  def hasCustomInventoryName: Boolean = {
    return false
  }

  def isItemValidForSlot(i: Int, itemstack: ItemStack): Boolean = {
    return i >= this.getSizeInventory
  }

  def getInventoryStackLimit: Int = {
    return 64
  }

  def isUseableByPlayer(par1EntityPlayer: EntityPlayer): Boolean = {
    return true
  }

  def load(nbt: NBTTagCompound) {
    this.containedItems = null
    val nbtList: NBTTagList = nbt.getTagList("Items", 10)

    for (i <- 0 to nbtList.tagCount()) {
      val stackTag: NBTTagCompound = nbtList.getCompoundTagAt(i)
      val id: Byte = stackTag.getByte("Slot")
      if (id >= 0 && id < this.getSizeInventory) {
        this.setInventorySlotContents(id, ItemStack.loadItemStackFromNBT(stackTag))
      }
    }
    nbt.setTag("Items", nbtList)
  }

  def save(nbt: NBTTagCompound): NBTTagCompound = {
    val nbtList: NBTTagList = new NBTTagList

    for (i <- 0 to this.getSizeInventory) {
      if (this.getStackInSlot(i) != null) {
        val var4: NBTTagCompound = new NBTTagCompound
        var4.setByte("Slot", i.toByte)
        this.getStackInSlot(i).writeToNBT(var4)
        nbtList.appendTag(var4)
      }
    }

    nbt.setTag("Items", nbtList)
    return nbt
  }
}
