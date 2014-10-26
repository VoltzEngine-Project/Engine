package resonant.lib.content.prefab

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.utility.nbt.ISaveObj
import universalelectricity.api.EnergyStorage
import universalelectricity.api.core.grid.{INode, INodeProvider}
import universalelectricity.core.grid.node.DCNode

/**
 * A trait for all INodeProviders that implement a DC circuit. Nodes must handle energy storage themself.
 */
trait TElectric extends TIO with INodeProvider with ISaveObj
{
  protected var dcNode = new DCNode(this)

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
  def setCapacity(value: Double)
  { energy.setCapacity(value) }

  @deprecated
  def energy: EnergyStorage = new EnergyStorage(0)

  @deprecated
  def setMaxTransfer(maxTransfer: Double)
  { energy.setMaxTransfer(maxTransfer) }

  @deprecated
  def setMaxReceive(maxReceive: Double)
  { energy.setMaxReceive(maxReceive) }

  @deprecated
  def setMaxExtract(maxExtract: Double)
  { energy.setMaxExtract(maxExtract) }

  @deprecated
  def getVoltage = 120

  /**
   * @param nodeType - The type of node we are looking for.
   * @param from     - The direction.
   * @return Returns the node object.
   */
  override def getNode(nodeType: Class[_ <: INode], from: ForgeDirection): INode =
  {
    if (classOf[DCNode].isAssignableFrom(nodeType))
      return dcNode

    return null
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