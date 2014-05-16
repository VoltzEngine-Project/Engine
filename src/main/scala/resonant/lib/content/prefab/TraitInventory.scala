package resonant.lib.content.prefab

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.ForgeDirection
import resonant.api.IExternalInventory
import resonant.api.IExternalInventoryBox
import resonant.lib.content.module.TileBase
import resonant.lib.utility.inventory.ExternalInventory
import resonant.lib.utility.inventory.InventoryUtility
import universalelectricity.api.vector.Vector3

/**
 * @since 22/03/14
 * @author tgame14
 */
trait TraitInventory extends TileBase with IExternalInventory with ISidedInventory {
  protected var inventory: IExternalInventoryBox = _
  protected var maxSlots: Int = 1

  override def getInventory(): IExternalInventoryBox =
    {
      if (this.inventory == null) {
        this.inventory = new ExternalInventory(this, this.maxSlots)
      }
      return this.inventory
    }

  override def getSizeInventory(): Int =
    {
      return this.getInventory().getSizeInventory
    }

  override def getStackInSlot(index: Int): ItemStack =
    {
      return this.getInventory().getStackInSlot(index)
    }

  override def decrStackSize(index: Int, amount: Int): ItemStack =
    {
      return this.getInventory().decrStackSize(index, amount)
    }

  def incrStackSize(slot: Int, stack: ItemStack) {
    if (this.getStackInSlot(slot) == null) {
      setInventorySlotContents(slot, stack.copy())
    } else if (this.getStackInSlot(slot).isItemEqual(stack)) {
      getStackInSlot(slot).stackSize += stack.stackSize
    }

    onInventoryChanged()
  }

  override def getStackInSlotOnClosing(index: Int): ItemStack =
    {
      return this.getInventory().getStackInSlotOnClosing(index)
    }

  override def setInventorySlotContents(index: Int, stack: ItemStack) {
    this.getInventory().setInventorySlotContents(index, stack)
  }

  override def getInvName(): String =
    {
      return this.getBlockType.getLocalizedName
    }

  def isInvNameLocalized(): Boolean =
    {
      return true
    }

  def getInventoryStackLimit: Int =
    {
      return this.getInventory.getInventoryStackLimit
    }

  def isUseableByPlayer(entityplayer: EntityPlayer): Boolean =
    {
      return this.getInventory.isUseableByPlayer(entityplayer)
    }

  def openChest {
    this.getInventory.openChest
  }

  def closeChest {
    this.getInventory.closeChest
  }

  def isItemValidForSlot(i: Int, itemstack: ItemStack): Boolean = {
    return this.getInventory.isItemValidForSlot(i, itemstack)
  }

  def getAccessibleSlotsFromSide(var1: Int): Array[Int] = {
    return this.getInventory.getAccessibleSlotsFromSide(var1)
  }

  def canInsertItem(i: Int, itemstack: ItemStack, j: Int): Boolean = {
    return this.getInventory.canInsertItem(i, itemstack, j)
  }

  def canExtractItem(i: Int, itemstack: ItemStack, j: Int): Boolean = {
    return this.getInventory.canExtractItem(i, itemstack, j)
  }

  def canStore(stack: ItemStack, slot: Int, side: ForgeDirection): Boolean = {
    return false
  }

  def canRemove(stack: ItemStack, slot: Int, side: ForgeDirection): Boolean = {
    if (slot >= this.getSizeInventory) {
      return false
    }
    return true
  }

  /**
   * Player-Inventory interaction methods.
   */
  def interactCurrentItem(slotID: Int, player: EntityPlayer): Boolean = {
    return interactCurrentItem(this, slotID, player)
  }

  def interactCurrentItem(inventory: IInventory, slotID: Int, player: EntityPlayer): Boolean = {
    val stackInInventory: ItemStack = inventory.getStackInSlot(slotID)
    val current: ItemStack = player.inventory.getCurrentItem
    if (current != null) {
      if (stackInInventory == null || ItemStack.areItemStacksEqual(stackInInventory, current)) {
        return insertCurrentItem(inventory, slotID, player)
      }
    }
    return extractItem(inventory, slotID, player)
  }

  def insertCurrentItem(inventory: IInventory, slotID: Int, player: EntityPlayer): Boolean = {
    val stackInInventory: ItemStack = inventory.getStackInSlot(slotID)
    var current: ItemStack = player.inventory.getCurrentItem
    if (current != null) {
      if (stackInInventory == null || ItemStack.areItemStacksEqual(stackInInventory, current)) {
        if (inventory.isItemValidForSlot(slotID, current)) {
          if (isControlDown(player)) {
            if (stackInInventory == null) {
              inventory.setInventorySlotContents(slotID, current.splitStack(1))
            } else {
              stackInInventory.stackSize += 1
              current.stackSize -= 1
            }
          } else {
            if (stackInInventory == null) {
              inventory.setInventorySlotContents(slotID, current)
            } else {
              stackInInventory.stackSize += current.stackSize
              current.stackSize = 0
            }
            current = null
          }
          if (current == null || current.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null)
          }
          return true
        }
      }
    }
    return false
  }

  def extractItem(inventory: IInventory, slotID: Int, player: EntityPlayer): Boolean = {
    var stackInInventory: ItemStack = inventory.getStackInSlot(slotID)
    if (stackInInventory != null) {
      if (isControlDown(player)) {
        InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory.splitStack(1), 0)
      } else {
        InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory, 0)
        stackInInventory = null
      }
      if (stackInInventory == null || stackInInventory.stackSize <= 0) {
        inventory.setInventorySlotContents(slotID, null)
      }
      return true
    }
    return false
  }

  override def onRemove(par5: Int, par6: Int) {
    super.onRemove(par5, par6)
    //dropEntireInventory(par5, par6)
  }

  //  def dropEntireInventory(par5: Int, par6: Int)
  //  {
  //    if (this.isInstanceOf[IInventory]) {
  //      val inventory: IInventory = this.asInstanceOf[IInventory]
  //      {
  //        var i: Int = 0
  //        while (i < inventory.getSizeInventory) {
  //          val dropStack: ItemStack = inventory.getStackInSlot(i)
  //          if (dropStack != null) {
  //            val var11: Int = dropStack.stackSize
  //            dropStack.stackSize -= var11
  //            InventoryUtility.dropItemStack(world, center, dropStack)
  //            if (dropStack.stackSize <= 0) inventory.setInventorySlotContents(i, null)
  //          }
  //          i += 1;
  //        }
  //      }
  //      inventory.onInventoryChanged
  //    }
  //  }

  override def readFromNBT(nbt: NBTTagCompound) {
    super.readFromNBT(nbt)
    getInventory.load(nbt)
  }

  override def writeToNBT(nbt: NBTTagCompound) {
    super.writeToNBT(nbt)
    getInventory.save(nbt)
  }

}
