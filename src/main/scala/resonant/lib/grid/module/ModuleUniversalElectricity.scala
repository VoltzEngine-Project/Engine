package resonant.lib.grid.module

import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.items.IEnergyItem
import resonant.lib.grid.Compatibility
import resonant.api.electric.{IEnergyNode, IEnergyContainer}
import resonant.api.grid.{INodeProvider, INode}

/**
 * @author Calclavia
 */
object ModuleUniversalElectricity extends Compatibility.CompatibilityModule("UniversalElectricity", "Universal Electricity", "Watt", "W", 1)
{
  /**
   * Gets the node from the side of the machine that deals with energy
   * @param handler - instanceof INodeProvider that contains IEnergyNode, or an IEnergyNode
   * @param direction - direction of the connection coming from the handler
   * @return null if its not a handler, or there is no return from INodeProvider.getNode
   */
  def getEnergyNode(handler: AnyRef, direction: ForgeDirection) : IEnergyNode =
  {
    if(handler != null)
    {
      if(handler.isInstanceOf[IEnergyNode])
      {
        return handler.asInstanceOf[IEnergyNode]
      }
      else if(handler.isInstanceOf[INodeProvider])
      {
        val provider: INodeProvider = handler.asInstanceOf[INodeProvider]
        val node: INode = provider.getNode(classOf[IEnergyNode], direction)
        if(node != null && node.isInstanceOf[IEnergyNode])
        {
          return node.asInstanceOf[IEnergyNode]
        }
      }
    }
    return null
  }

  override def receiveEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doReceive: Boolean): Double =
  {
    val node :IEnergyNode = getEnergyNode(handler, direction)
    if(node != null)
    {
      node.addEnergy(direction, energy, doReceive)
    }
    return energy
  }

  override def extractEnergy(handler: AnyRef, direction: ForgeDirection, energy: Double, doExtract: Boolean): Double =
  {
    val node :IEnergyNode = getEnergyNode(handler, direction)
    if(node != null)
    {
      node.removeEnergy(direction, energy, doExtract)
    }
    return energy
  }

  override def doIsHandler(obj: AnyRef, dir: ForgeDirection): Boolean =
  {
    return  getEnergyNode(obj, dir) != null || obj != null && obj.isInstanceOf[IEnergyItem]
  }

  override def canConnect(obj: AnyRef, direction: ForgeDirection, source: AnyRef): Boolean =
  {
    return getEnergyNode(obj, direction) != null
  }

  override def chargeItem(itemStack: ItemStack, joules: Double, doCharge: Boolean): Double =
  {
    if (itemStack != null && itemStack.getItem.isInstanceOf[IEnergyItem])
    {
      return (itemStack.getItem.asInstanceOf[IEnergyItem]).recharge(itemStack, joules, doCharge)
    }
    return 0
  }

  override def dischargeItem(itemStack: ItemStack, joules: Double, doDischarge: Boolean): Double =
  {
    if (itemStack != null && itemStack.getItem.isInstanceOf[IEnergyItem])
    {
      return (itemStack.getItem.asInstanceOf[IEnergyItem]).discharge(itemStack, joules, doDischarge)
    }
    return 0
  }

  override def getItemWithCharge(itemStack: ItemStack, energy: Double): ItemStack =
  {
    if (itemStack != null && itemStack.getItem.isInstanceOf[IEnergyItem])
    {
      (itemStack.getItem.asInstanceOf[IEnergyItem]).setEnergy(itemStack, energy)
      return itemStack
    }
    return itemStack
  }

  override def doIsEnergyContainer(obj: AnyRef): Boolean =
  {
    return obj != null && obj.isInstanceOf[IEnergyContainer]
  }

  override def getEnergy(obj: AnyRef, direction: ForgeDirection): Double =
  {
      if (obj.isInstanceOf[IEnergyContainer])
      {
        return obj.asInstanceOf[IEnergyContainer].getEnergy(direction)
      }else
      if(obj.isInstanceOf[IEnergyNode])
      {
        return obj.asInstanceOf[IEnergyNode].getEnergy(direction)
      }
      return 0.0
  }

  override def getMaxEnergy(obj: AnyRef, direction: ForgeDirection): Double =
  {
    if(obj != null)
      return obj.asInstanceOf[IEnergyContainer].getEnergyCapacity(direction)
    else
      return 0.0
  }

  override def getEnergyItem(is: ItemStack): Double =
  {
    if(is != null && is.isInstanceOf[IEnergyItem])
      return is.getItem.asInstanceOf[IEnergyItem].getEnergy(is)
    else
      return 0.0
  }

  override def getMaxEnergyItem(is: ItemStack): Double =
  {
    if(is != null && is.isInstanceOf[IEnergyItem])
      return (is.getItem.asInstanceOf[IEnergyItem]).getEnergyCapacity(is)
    else
      return 0.0
  }
}