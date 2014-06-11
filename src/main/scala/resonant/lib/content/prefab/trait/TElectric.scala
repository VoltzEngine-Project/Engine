package resonant.lib.content.prefab

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import resonant.lib.utility.nbt.ISaveObj
import universalelectricity.api.UniversalClass
import universalelectricity.api.core.grid.electric.{IElectricNode, IEnergyContainer}
import universalelectricity.api.core.grid.{NodeRegistry, INode, INodeProvider}
import universalelectricity.compatibility.Compatibility
import net.minecraftforge.common.util.ForgeDirection

@UniversalClass
trait TElectric extends TIO with INodeProvider with IEnergyContainer with ISaveObj
{
  protected var electricNode = NodeRegistry.get(this, classOf[IElectricNode])

  /**
   * @param nodeType - The type of node we are looking for.
   * @param from     - The direction.
   * @return Returns the node object.
   */
  def getNode[N >: INode](nodeType: Class[N], from: ForgeDirection): N =
  {
    if (nodeType == classOf[IElectricNode])
      return electricNode

    return null
  }

  //@Callback
  def recharge(stack: ItemStack)
  {

    electricNode.drawPower(Compatibility.getHandler(stack.getItem).chargeItem(stack, electricNode.getEnergy(getVoltage), true))
  }

  //@Callback
  protected def discharge(stack: ItemStack)
  {
    electricNode.applyPower(Compatibility.getHandler(stack.getItem).dischargeItem(stack, this.energy.getEmptySpace, true))
  }

  def getVoltage = 100D

  override def save(nbt: NBTTagCompound)
  {
    super.save(nbt);

    if (electricNode != null)
    {
      electricNode.save(nbt)
    }
  }

  override def load(nbt: NBTTagCompound)
  {
    super.load(nbt);

    if (electricNode != null)
    {
      electricNode.load(nbt)
    }
  }

}