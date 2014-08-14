package resonant.lib.content.prefab

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.utility.nbt.ISaveObj
import universalelectricity.api.{EnergyStorage, UniversalClass}
import universalelectricity.api.core.grid.electric.IElectricNode
import universalelectricity.api.core.grid.{INode, INodeProvider}
import universalelectricity.compatibility.Compatibility
import universalelectricity.core.grid.node.NodeElectric

@UniversalClass
trait TElectric extends TIO with INodeProvider with ISaveObj
{
  protected var electricNode = new NodeElectric(this)

  def energy : EnergyStorage = electricNode.buffer

  def setCapacity(value: Double) {energy.setCapacity(value)}

  def setMaxTransfer(maxTransfer: Double) {energy.setMaxTransfer(maxTransfer)}

  def setMaxReceive(maxReceive: Double) {energy.setMaxReceive(maxReceive)}

  def setMaxExtract(maxExtract: Double) {energy.setMaxExtract(maxExtract)}

  protected def recharge(stack: ItemStack)
  {
    if (stack != null && Compatibility.getHandler(stack.getItem) != null)
      electricNode.removeEnergy(ForgeDirection.UNKNOWN, Compatibility.chargeItem(stack, electricNode.getEnergy(ForgeDirection.UNKNOWN), true), true)
  }

  protected def discharge(stack: ItemStack)
  {
    if (stack != null && Compatibility.getHandler(stack.getItem) != null)
      electricNode.addEnergy(ForgeDirection.UNKNOWN, Compatibility.dischargeItem(stack, electricNode.getEnergyCapacity(ForgeDirection.UNKNOWN) - electricNode.getEnergy(ForgeDirection.UNKNOWN), true), true)
  }

  /**
   * @param nodeType - The type of node we are looking for.
   * @param from     - The direction.
   * @return Returns the node object.
   */
  override def getNode(nodeType: Class[_ <: INode], from: ForgeDirection): INode =
  {
    if (nodeType == classOf[IElectricNode])
      return electricNode

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

}