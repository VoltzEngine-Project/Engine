package universalelectricity.compatibility.module

import cofh.api.energy.{IEnergyContainerItem, IEnergyHandler}
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.compatibility.Compatibility

/**
 * Module for Thermal Expansion support
 * @author Calclavia
 */
object ModuleThermalExpansion extends Compatibility.CompatibilityModule("ThermalExpansion", "ThermalExpansion", "Redstone Flux", "RF", 50)
{
  def receiveEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doReceive: Boolean): Double =
  {
    return (handler.asInstanceOf[IEnergyHandler]).receiveEnergy(direction, (energy * ratio).toInt, !doReceive) * reciprocal_ratio
  }

  def extractEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doExtract: Boolean): Double =
  {
    return ((handler.asInstanceOf[IEnergyHandler]).extractEnergy(direction, (energy * ratio).toInt, !doExtract) * reciprocal_ratio)
  }

  def doIsHandler(obj: AnyRef): Boolean =
  {
    return obj.isInstanceOf[IEnergyHandler] || obj.isInstanceOf[IEnergyContainerItem]
  }

  def canConnect(obj: AnyRef, direction: ForgeDirection, source: AnyRef): Boolean =
  {
    return (obj.asInstanceOf[IEnergyHandler]).canConnectEnergy(direction)
  }

  def chargeItem(itemStack: ItemStack, joules: Double, docharge: Boolean): Double =
  {
    if (itemStack.getItem.isInstanceOf[IEnergyContainerItem])
    {
      return Math.floor((itemStack.getItem.asInstanceOf[IEnergyContainerItem]).receiveEnergy(itemStack, Math.ceil(joules * ratio).toInt, !docharge) * reciprocal_ratio)
    }
    return 0
  }

  def dischargeItem(itemStack: ItemStack, joules: Double, doDischarge: Boolean): Double =
  {
    if (itemStack.getItem.isInstanceOf[IEnergyContainerItem])
    {
      return Math.floor((itemStack.getItem.asInstanceOf[IEnergyContainerItem]).extractEnergy(itemStack, Math.ceil(joules * ratio).toInt, !doDischarge) * reciprocal_ratio)
    }
    return 0
  }

  def getItemWithCharge(is: ItemStack, energy: Double): ItemStack =
  {
    return null
  }

  def doIsEnergyContainer(obj: AnyRef): Boolean =
  {
    return obj.isInstanceOf[IEnergyHandler]
  }

  def getEnergy(obj: AnyRef, from: ForgeDirection): Double =
  {
    return ((obj.asInstanceOf[IEnergyHandler]).getEnergyStored(from) * reciprocal_ratio)
  }

  def getMaxEnergy(obj: AnyRef, from: ForgeDirection): Double =
  {
    return ((obj.asInstanceOf[IEnergyHandler]).getMaxEnergyStored(from) * reciprocal_ratio)
  }

  def getEnergyItem(is: ItemStack): Double =
  {
    if (is.getItem.isInstanceOf[IEnergyContainerItem])
    {
      return ((is.getItem.asInstanceOf[IEnergyContainerItem]).getEnergyStored(is) * reciprocal_ratio)
    }
    return 0
  }

  def getMaxEnergyItem(is: ItemStack): Double =
  {
    if (is.getItem.isInstanceOf[IEnergyContainerItem])
    {
      return ((is.getItem.asInstanceOf[IEnergyContainerItem]).getMaxEnergyStored(is) * reciprocal_ratio)
    }
    return 0
  }
}
