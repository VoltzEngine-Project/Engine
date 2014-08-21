package universalelectricity.compatibility.module

import buildcraft.api.mj.{IBatteryProvider}
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.compatibility.Compatibility

/**
 * Module for BuildCraft support
 * @author Calclavia
 */
object ModuleBuildCraft extends Compatibility.CompatibilityModule("BuildCraft|Energy", "BuildCraft", "Minecraft Joule", "MJ", 500)
{
  def receiveEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doReceive: Boolean): Double =
  {
    val receiver = handler.asInstanceOf[IBatteryProvider].getMjBattery("buildcraft.kinesis")

    if (receiver != null)
    {
      if (doReceive)
      {
        return receiver.addEnergy(energy * ratio) * reciprocal_ratio
      }

      return (receiver.getEnergyRequested * reciprocal_ratio)
    }
    return 0
  }

  def extractEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doExtract: Boolean): Double =
  {
    val receiver = handler.asInstanceOf[IBatteryProvider].getMjBattery("buildcraft.kinesis")

    if (receiver != null)
    {
      if (doExtract)
      {
        return receiver.addEnergy(energy * ratio) * reciprocal_ratio
      }

      return energy
    }

    return 0
  }

  def doIsHandler(obj: AnyRef): Boolean =
  {
    return obj.isInstanceOf[IBatteryProvider]
  }

  def canConnect(obj: AnyRef, direction: ForgeDirection, source: AnyRef): Boolean =
  {
    return obj.asInstanceOf[IBatteryProvider].getMjBattery("buildcraft.kinesis") != null
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
    return obj.isInstanceOf[IBatteryProvider]
  }

  def getEnergy(obj: AnyRef, direction: ForgeDirection): Double =
  {
    val receiver = obj.asInstanceOf[IBatteryProvider].getMjBattery("buildcraft.kinesis")

    if (receiver != null)
    {
      return receiver.getEnergyStored * reciprocal_ratio
    }

    return 0
  }

  def getMaxEnergy(obj: AnyRef, direction: ForgeDirection): Double =
  {
    val receiver = obj.asInstanceOf[IBatteryProvider].getMjBattery("buildcraft.kinesis")

    if (receiver != null)
    {
      return receiver.maxCapacity * reciprocal_ratio
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