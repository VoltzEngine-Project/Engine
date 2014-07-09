package resonant.lib.content.prefab

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.utility.nbt.ISaveObj
import universalelectricity.api.UniversalClass
import universalelectricity.api.core.grid.electric.IElectricNode
import universalelectricity.api.core.grid.{INode, INodeProvider, NodeRegistry}
import universalelectricity.compatibility.Compatibility

@UniversalClass
trait TElectric extends TIO with INodeProvider with ISaveObj
{
  protected var electricNode = NodeRegistry.get(this, classOf[IElectricNode])

  protected def recharge(stack: ItemStack)
  {
    if (stack != null && Compatibility.getHandler(stack.getItem) != null)
      electricNode.drawPower(Compatibility.getHandler(stack.getItem).chargeItem(stack, electricNode.getEnergy(getVoltage), true))
  }

  protected def discharge(stack: ItemStack)
  {
    if (stack != null && Compatibility.getHandler(stack.getItem) != null)
      electricNode.applyPower(Compatibility.getHandler(stack.getItem).dischargeItem(stack, electricNode.getEmptySpace, true))
  }

  /**
   * @param nodeType - The type of node we are looking for.
   * @param from     - The direction.
   * @return Returns the node object.
   */
  override def getNode[N <: INode](nodeType: Class[N], from: ForgeDirection): N =
  {
    if (nodeType == classOf[IElectricNode])
      return electricNode.asInstanceOf[N]

    return null.asInstanceOf[N]
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