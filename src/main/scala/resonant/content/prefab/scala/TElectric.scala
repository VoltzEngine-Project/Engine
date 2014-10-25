package resonant.lib.content.prefab

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.utility.nbt.ISaveObj
import universalelectricity.api.core.grid.electric.IElectricNode
import universalelectricity.api.core.grid.{INode, INodeProvider}
import universalelectricity.api.{EnergyStorage, UniversalClass}
import universalelectricity.compatibility.Compatibility
import universalelectricity.core.grid.node.NodeElectric
import universalelectricity.simulator.dc.micro.DCNode

@UniversalClass
trait TElectric extends TIO with INodeProvider with ISaveObj
{
  @deprecated
  protected var electricNode = new NodeElectric(this)

  protected var dcNode = new DCNode(this)

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
  def energy: EnergyStorage = electricNode.buffer

  @deprecated
  def setMaxTransfer(maxTransfer: Double)
  { energy.setMaxTransfer(maxTransfer) }

  @deprecated
  def setMaxReceive(maxReceive: Double)
  { energy.setMaxReceive(maxReceive) }

  @deprecated
  def setMaxExtract(maxExtract: Double)
  { energy.setMaxExtract(maxExtract) }

  /**
   * @param nodeType - The type of node we are looking for.
   * @param from     - The direction.
   * @return Returns the node object.
   */
  override def getNode(nodeType: Class[_ <: INode], from: ForgeDirection): INode =
  {
    if (classOf[IElectricNode].isAssignableFrom(nodeType))
      return electricNode

    if (classOf[DCNode].isAssignableFrom(nodeType))
      return dcNode

    return null
  }

  def getVoltage = 100D

  override def save(nbt: NBTTagCompound)
  {
    super.save(nbt)

    if (electricNode != null)
    {
      electricNode.save(nbt)
    }
  }

  override def load(nbt: NBTTagCompound)
  {
    super.load(nbt)

    if (electricNode != null)
    {
      electricNode.load(nbt)
    }
  }

  protected def recharge(stack: ItemStack)
  {
    if (stack != null && Compatibility.getHandler(stack.getItem, null) != null)
      electricNode.removeEnergy(ForgeDirection.UNKNOWN, Compatibility.chargeItem(stack, electricNode.getEnergy(ForgeDirection.UNKNOWN), true), true)
  }

  protected def discharge(stack: ItemStack)
  {
    if (stack != null && Compatibility.getHandler(stack.getItem, null) != null)
      electricNode.addEnergy(ForgeDirection.UNKNOWN, Compatibility.dischargeItem(stack, electricNode.getEnergyCapacity(ForgeDirection.UNKNOWN) - electricNode.getEnergy(ForgeDirection.UNKNOWN), true), true)
  }

}