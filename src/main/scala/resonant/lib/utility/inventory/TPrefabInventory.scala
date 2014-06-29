package resonant.lib.utility.inventory

import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.{TileEntity, TileEntityChest}
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.IInventoryProvider
import universalelectricity.core.transform.vector.Vector3

/**
 * @author Calclavia
 */
trait TPrefabInventory extends IInventoryProvider
{
  //TODO: Inventory Utility?
  def addStackToInventory(slotIndex: Int, itemStack: ItemStack): ItemStack =
  {
    if (slotIndex < getInventory().getSizeInventory())
    {
      var stackInInventory = getInventory().getStackInSlot(slotIndex)

      if (stackInInventory == null)
      {
        getInventory().setInventorySlotContents(slotIndex, itemStack)

        if (getInventory().getStackInSlot(slotIndex) == null)
        {
          return itemStack
        }

        return null
      }
      else if (stackInInventory.isItemEqual(itemStack) && stackInInventory.isStackable)
      {
        stackInInventory = stackInInventory.copy
        val stackLim: Int = Math.min(getInventory().getInventoryStackLimit, itemStack.getMaxStackSize)
        val rejectedAmount: Int = Math.max((stackInInventory.stackSize + itemStack.stackSize) - stackLim, 0)
        stackInInventory.stackSize = Math.min(Math.max((stackInInventory.stackSize + itemStack.stackSize - rejectedAmount), 0), getInventory().getInventoryStackLimit)
        itemStack.stackSize = rejectedAmount
        getInventory().setInventorySlotContents(slotIndex, stackInInventory)
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
    if (!this.worldObj.isRemote)
    {
      for (direction <- ForgeDirection.VALID_DIRECTIONS)
      {
        if (itemStack != null)
        {
          itemStack = this.tryPlaceInPosition(itemStack, new Vector3(this).translate(direction), direction)
        }
      }
      if (itemStack != null)
      {
        this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord + 0.5, this.yCoord + 1, this.zCoord + 0.5, itemStack))
      }
    }
    return false
  }

  /**
   * Tries to place an itemStack in a specific position if it is an inventory.
   *
   * @return The ItemStack remained after place attempt
   */
  def tryPlaceInPosition(itemStack: ItemStack, position: Vector3, dir: Nothing): ItemStack =
  {
    val tileEntity: TileEntity = position.getTileEntity(this.worldObj)
    val direction: Nothing = dir.getOpposite
    if (tileEntity != null && itemStack != null)
    {
      if (tileEntity.isInstanceOf[Nothing])
      {
        val mainBlockPosition: Vector3 = (tileEntity.asInstanceOf[Nothing]).getMainBlock
        if (mainBlockPosition != null)
        {
          if (!(mainBlockPosition.getTileEntity(this.worldObj).isInstanceOf[Nothing]))
          {
            return tryPlaceInPosition(itemStack, mainBlockPosition, direction)
          }
        }
      }
      else if (tileEntity.isInstanceOf[TileEntityChest])
      {
        val chests: Array[TileEntityChest] = Array(tileEntity.asInstanceOf[TileEntityChest], null)
        {
          var i: Int = 2
          while (i < 6)
          {
            {
              val searchDirection: Nothing = ForgeDirection.getOrientation(i)
              val searchPosition: Vector3 = position.clone
              searchPosition.translate(searchDirection)
              if (searchPosition.getTileEntity(this.worldObj) != null)
              {
                if (searchPosition.getTileEntity(this.worldObj).getClass eq chests(0).getClass)
                {
                  chests(1) = searchPosition.getTileEntity(this.worldObj).asInstanceOf[TileEntityChest]
                  break //todo: break is not supported
                }
              }
            }
            ({
              i += 1;
              i - 1
            })
          }
        }
        for (chest <- chests)
        {
          if (chest != null)
          {
            {
              var i: Int = 0
              while (i < chest.getSizeInventory)
              {
                {
                  itemStack = this.addStackToInventory(i, chest, itemStack)
                  if (itemStack == null)
                  {
                    return null
                  }
                }
                ({
                  i += 1;
                  i - 1
                })
              }
            }
          }
        }
      }
      else if (tileEntity.isInstanceOf[ISidedInventory])
      {
        val inventory: ISidedInventory = tileEntity.asInstanceOf[ISidedInventory]
        val slots: Array[Int] = inventory.getAccessibleSlotsFromSide(direction.ordinal)
        {
          var i: Int = 0
          while (i < slots.length)
          {
            {
              if (inventory.canInsertItem(slots(i), itemStack, direction.ordinal))
              {
                itemStack = this.addStackToInventory(slots(i), inventory, itemStack)
              }
              if (itemStack == null)
              {
                return null
              }
            }
            ({
              i += 1;
              i - 1
            })
          }
        }
      }
      else if (tileEntity.isInstanceOf[IInventory])
      {
        val inventory: IInventory = tileEntity.asInstanceOf[IInventory]
        {
          var i: Int = 0
          while (i < inventory.getSizeInventory)
          {
            {
              itemStack = this.addStackToInventory(i, inventory, itemStack)
              if (itemStack == null)
              {
                return null
              }
            }
            ({
              i += 1;
              i - 1
            })
          }
        }
      }
    }
    if (itemStack.stackSize <= 0)
    {
      return null
    }
    return itemStack
  }
}
