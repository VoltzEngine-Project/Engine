package resonant.lib.prefab.item

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.{FluidStack, IFluidContainerItem}

/**
 * @author Calclavia
 */
trait TFluidContainerItem extends IFluidContainerItem
{
  var capacity: Int = 0

  def setCapacity(capacity: Int): TFluidContainerItem =
  {
    this.capacity = capacity
    return this
  }

  def getFluid(container: ItemStack): FluidStack =
  {
    if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
    {
      return null
    }
    return FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"))
  }

  def getCapacity(container: ItemStack): Int = capacity

  def fill(container: ItemStack, resource: FluidStack, doFill: Boolean): Int =
  {
    if (resource == null)
    {
      return 0
    }
    if (!doFill)
    {
      if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
      {
        return Math.min(capacity, resource.amount)
      }
      val stack: FluidStack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"))
      if (stack == null)
      {
        return Math.min(capacity, resource.amount)
      }
      if (!stack.isFluidEqual(resource))
      {
        return 0
      }
      return Math.min(capacity - stack.amount, resource.amount)
    }
    if (container.stackTagCompound == null)
    {
      container.stackTagCompound = new NBTTagCompound
    }
    if (!container.stackTagCompound.hasKey("Fluid"))
    {
      val fluidTag: NBTTagCompound = resource.writeToNBT(new NBTTagCompound)
      if (capacity < resource.amount)
      {
        fluidTag.setInteger("Amount", capacity)
        container.stackTagCompound.setTag("Fluid", fluidTag)
        return capacity
      }
      container.stackTagCompound.setTag("Fluid", fluidTag)
      return resource.amount
    }
    val fluidTag: NBTTagCompound = container.stackTagCompound.getCompoundTag("Fluid")
    val stack: FluidStack = FluidStack.loadFluidStackFromNBT(fluidTag)
    if (!stack.isFluidEqual(resource))
    {
      return 0
    }
    var filled: Int = capacity - stack.amount
    if (resource.amount < filled)
    {
      stack.amount += resource.amount
      filled = resource.amount
    }
    else
    {
      stack.amount = capacity
    }
    container.stackTagCompound.setTag("Fluid", stack.writeToNBT(fluidTag))
    return filled
  }

  def drain(container: ItemStack, maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
    {
      return null
    }
    val stack: FluidStack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"))
    if (stack == null)
    {
      return null
    }
    stack.amount = Math.min(stack.amount, maxDrain)
    if (doDrain)
    {
      if (maxDrain >= capacity)
      {
        container.stackTagCompound.removeTag("Fluid")
        if (container.stackTagCompound.hasNoTags)
        {
          container.stackTagCompound = null
        }
        return stack
      }
      val fluidTag: NBTTagCompound = container.stackTagCompound.getCompoundTag("Fluid")
      fluidTag.setInteger("Amount", fluidTag.getInteger("Amount") - maxDrain)
      container.stackTagCompound.setTag("Fluid", fluidTag)
    }
    return stack
  }
}
