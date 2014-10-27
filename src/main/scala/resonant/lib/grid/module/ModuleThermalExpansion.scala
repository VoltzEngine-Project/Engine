package resonant.lib.grid.module

import cofh.api.energy.{IEnergyContainerItem, IEnergyHandler}
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.grid.Compatibility

/**
 * Module for Thermal Expansion support
 * @author Calclavia
 */
object ModuleThermalExpansion extends Compatibility.CompatibilityModule("ThermalExpansion", "ThermalExpansion", "Redstone Flux", "RF", 50)
{
  override def receiveEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doReceive: Boolean): Double =
  {
    return (handler.asInstanceOf[IEnergyHandler]).receiveEnergy(direction, (energy * ratio).toInt, !doReceive) * reciprocal_ratio
  }

  override def extractEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doExtract: Boolean): Double =
  {
    return ((handler.asInstanceOf[IEnergyHandler]).extractEnergy(direction, (energy * ratio).toInt, !doExtract) * reciprocal_ratio)
  }

  override def doIsHandler(obj: AnyRef, dir: ForgeDirection): Boolean =
  {
    return obj.isInstanceOf[IEnergyHandler] || obj.isInstanceOf[IEnergyContainerItem]
  }

  override def canConnect(obj: AnyRef, direction: ForgeDirection, source: AnyRef): Boolean =
  {
    return (obj.asInstanceOf[IEnergyHandler]).canConnectEnergy(direction)
  }

  override def chargeItem(itemStack: ItemStack, joules: Double, docharge: Boolean): Double =
  {
    if (itemStack.getItem.isInstanceOf[IEnergyContainerItem])
    {
      return Math.floor((itemStack.getItem.asInstanceOf[IEnergyContainerItem]).receiveEnergy(itemStack, Math.ceil(joules * ratio).toInt, !docharge) * reciprocal_ratio)
    }
    return 0
  }

  override def dischargeItem(itemStack: ItemStack, joules: Double, doDischarge: Boolean): Double =
  {
    if (itemStack.getItem.isInstanceOf[IEnergyContainerItem])
    {
      return Math.floor((itemStack.getItem.asInstanceOf[IEnergyContainerItem]).extractEnergy(itemStack, Math.ceil(joules * ratio).toInt, !doDischarge) * reciprocal_ratio)
    }
    return 0
  }

  override def getItemWithCharge(is: ItemStack, energy: Double): ItemStack =
  {
    return null
  }

  override def doIsEnergyContainer(obj: AnyRef): Boolean =
  {
    return obj.isInstanceOf[IEnergyHandler]
  }

  override def getEnergy(obj: AnyRef, from: ForgeDirection): Double =
  {
    return ((obj.asInstanceOf[IEnergyHandler]).getEnergyStored(from) * reciprocal_ratio)
  }

  override def getMaxEnergy(obj: AnyRef, from: ForgeDirection): Double =
  {
    return ((obj.asInstanceOf[IEnergyHandler]).getMaxEnergyStored(from) * reciprocal_ratio)
  }

  override def getEnergyItem(is: ItemStack): Double =
  {
    if (is.getItem.isInstanceOf[IEnergyContainerItem])
    {
      return ((is.getItem.asInstanceOf[IEnergyContainerItem]).getEnergyStored(is) * reciprocal_ratio)
    }
    return 0
  }

  override def getMaxEnergyItem(is: ItemStack): Double =
  {
    if (is.getItem.isInstanceOf[IEnergyContainerItem])
    {
      return ((is.getItem.asInstanceOf[IEnergyContainerItem]).getMaxEnergyStored(is) * reciprocal_ratio)
    }
    return 0
  }
}
