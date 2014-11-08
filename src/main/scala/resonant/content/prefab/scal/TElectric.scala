package resonant.lib.content.prefab

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.{INode, INodeProvider}
import resonant.lib.grid.electric.DCNode
import resonant.lib.utility.nbt.ISaveObj

/**
 * A trait for all INodeProviders that implement a DC circuit. Nodes must handle energy storage themself.
 */
trait TElectric extends TIO with INodeProvider with ISaveObj
{
  protected var dcNode : DCNode = new DCNode(this)

  override def start()
  {
    super.start()
    dcNode.reconstruct()
  }

  override def onWorldJoin()
  {
    dcNode.reconstruct()
  }

  override def onNeighborChanged(block: Block)
  {
    dcNode.reconstruct()
  }

  override def onWorldSeparate()
  {
    dcNode.deconstruct()
  }

  @deprecated
  def getVoltage = 120

  override def getNode[N <: INode](nodeType: Class[_ <: N], from: ForgeDirection): N =
  {
    if (classOf[DCNode].isAssignableFrom(nodeType))
      return dcNode.asInstanceOf[N]

    return null.asInstanceOf[N]
  }

  protected def recharge(stack: ItemStack)
  {
    //if (stack != null && Compatibility.getHandler(stack.getItem, null) != null)
    //   electricNode.removeEnergy(ForgeDirection.UNKNOWN, Compatibility.chargeItem(stack, electricNode.getEnergy(ForgeDirection.UNKNOWN), true), true)
  }

  protected def discharge(stack: ItemStack)
  {
    // if (stack != null && Compatibility.getHandler(stack.getItem, null) != null)
    //   electricNode.addEnergy(ForgeDirection.UNKNOWN, Compatibility.dischargeItem(stack, electricNode.getEnergyCapacity(ForgeDirection.UNKNOWN) - electricNode.getEnergy(ForgeDirection.UNKNOWN), true), true)
  }

}