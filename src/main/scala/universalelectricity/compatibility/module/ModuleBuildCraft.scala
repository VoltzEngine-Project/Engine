package universalelectricity.compatibility.module

import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import buildcraft.api.power.IPowerReceptor
import buildcraft.api.power.PowerHandler.Type
import universalelectricity.compatibility.Compatibility

/**
 * Module for BuildCraft support
 * @author Calclavia
 */
object ModuleBuildCraft extends Compatibility.CompatibilityModule("BuildCraft|Energy", "BuildCraft", "Minecraft Joule", "MJ", 500)
{
  def receiveEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doReceive: Boolean): Double =
  {
    val receptor = (handler.asInstanceOf[IPowerReceptor])
    val receiver = receptor.getPowerReceiver(direction)

    if (receiver != null)
    {
      if (doReceive)
      {
        return (receiver.receiveEnergy(Type.PIPE, (energy * ratio).asInstanceOf[Float], direction) * reciprocal_ratio)
      }
      return (receiver.powerRequest * reciprocal_ratio)
    }
    return 0
  }

  def extractEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doExtract: Boolean): Double =
  {
    return 0
  }

  def doIsHandler(obj: AnyRef): Boolean =
  {
    return obj.isInstanceOf[IPowerReceptor]
  }

  def canConnect(obj: AnyRef, direction: ForgeDirection, source: AnyRef): Boolean =
  {
    return (obj.asInstanceOf[IPowerReceptor]).getPowerReceiver(direction) != null
  }

  def chargeItem(itemStack: ItemStack, joules: Double, docharge: Boolean): Double =
  {
    return 0
  }

  def dischargeItem(itemStack: ItemStack, joules: Double, doDischarge: Boolean): Double =
  {
    return 0
  }

  def getItemWithCharge(itemStack: ItemStack, energy: Double): ItemStack =
  {
    return null
  }

  def doIsEnergyContainer(obj: AnyRef): Boolean =
  {
    return obj.isInstanceOf[IPowerReceptor]
  }

  def getEnergy(obj: AnyRef, direction: ForgeDirection): Double =
  {
    if (obj.isInstanceOf[IPowerReceptor])
    {
      if ((obj.asInstanceOf[IPowerReceptor]).getPowerReceiver(direction) != null)
      {
        return ((obj.asInstanceOf[IPowerReceptor]).getPowerReceiver(direction).getEnergyStored * reciprocal_ratio)
      }
    }
    return 0
  }

  def getMaxEnergy(obj: AnyRef, direction: ForgeDirection): Double =
  {
    if (obj.isInstanceOf[IPowerReceptor])
    {
      if ((obj.asInstanceOf[IPowerReceptor]).getPowerReceiver(direction) != null)
      {
        return ((obj.asInstanceOf[IPowerReceptor]).getPowerReceiver(direction).getMaxEnergyStored * reciprocal_ratio)
      }
    }
    return 0
  }

  def getEnergyItem(is: ItemStack): Double =
  {
    return 0
  }

  def getMaxEnergyItem(is: ItemStack): Double =
  {
    return 0
  }
}