package universalelectricity.compatibility.module

import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.api.core.grid.electric.{IEnergyNode, IEnergyContainer}
import universalelectricity.api.item.IEnergyItem
import universalelectricity.compatibility.Compatibility

/**
 * @author Calclavia
 */
object ModuleUniversalElectricity extends Compatibility.CompatibilityModule("UniversalElectricity", "Universal Electricity", "Watt", "W", 1)
{
  def receiveEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doReceive: Boolean): Double =
  {
    (handler.asInstanceOf[INodeProvider]).getNode(classOf[IEnergyNode], direction).asInstanceOf[IEnergyNode].addEnergy(direction, energy, doReceive)
    return energy
  }

  def extractEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doExtract: Boolean): Double =
  {
    (handler.asInstanceOf[INodeProvider]).getNode(classOf[IEnergyNode], direction).asInstanceOf[IEnergyNode].removeEnergy(direction, energy, doExtract)
    return energy
  }

  def doIsHandler(obj: AnyRef): Boolean =
  {
    return (obj.isInstanceOf[INodeProvider] && (obj.asInstanceOf[INodeProvider]).getNode(classOf[IEnergyNode], null) != null) || obj.isInstanceOf[IEnergyItem]
  }

  def canConnect(obj: AnyRef, direction: ForgeDirection, source: AnyRef): Boolean =
  {
    return (obj.asInstanceOf[INodeProvider]).getNode(classOf[IEnergyNode], direction) != null
  }

  def chargeItem(itemStack: ItemStack, joules: Double, doCharge: Boolean): Double =
  {
    if (itemStack != null && itemStack.getItem.isInstanceOf[IEnergyItem])
    {
      return (itemStack.getItem.asInstanceOf[IEnergyItem]).recharge(itemStack, joules, doCharge)
    }
    return 0
  }

  def dischargeItem(itemStack: ItemStack, joules: Double, doDischarge: Boolean): Double =
  {
    if (itemStack != null && itemStack.getItem.isInstanceOf[IEnergyItem])
    {
      return (itemStack.getItem.asInstanceOf[IEnergyItem]).discharge(itemStack, joules, doDischarge)
    }
    return 0
  }

  def getItemWithCharge(itemStack: ItemStack, energy: Double): ItemStack =
  {
    if (itemStack != null)
    {
      if (itemStack.getItem.isInstanceOf[IEnergyItem])
      {
        (itemStack.getItem.asInstanceOf[IEnergyItem]).setEnergy(itemStack, energy)
        return itemStack
      }
    }
    return itemStack
  }

  def doIsEnergyContainer(obj: AnyRef): Boolean =
  {
    return obj.isInstanceOf[IEnergyContainer]
  }

  def getEnergy(obj: AnyRef, direction: ForgeDirection): Double =
  {
    return (obj.asInstanceOf[IEnergyContainer]).getEnergy(direction)
  }

  def getMaxEnergy(obj: AnyRef, direction: ForgeDirection): Double =
  {
    return (obj.asInstanceOf[IEnergyContainer]).getEnergyCapacity(direction)
  }

  def getEnergyItem(is: ItemStack): Double =
  {
    return (is.getItem.asInstanceOf[IEnergyItem]).getEnergy(is)
  }

  def getMaxEnergyItem(is: ItemStack): Double =
  {
    return (is.getItem.asInstanceOf[IEnergyItem]).getEnergyCapacity(is)
  }
}