package resonant.lib.content.prefab

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.utility.nbt.ISaveObj
import universalelectricity.api.UniversalClass
import universalelectricity.api.core.grid.electric.IElectricNode
import universalelectricity.api.core.grid.{INode, INodeProvider, NodeRegistry}

@UniversalClass
trait TElectric extends TIO with INodeProvider with ISaveObj
{
  protected var electricNode = NodeRegistry.get(this, classOf[IElectricNode])

  /**
   * @param nodeType - The type of node we are looking for.
   * @param from     - The direction.
   * @return Returns the node object.
   */
  def getNode[N <: INode](nodeType: Class[N], from: ForgeDirection): N =
  {
    if (nodeType == classOf[IElectricNode])
      return electricNode.asInstanceOf[N]

    return null.asInstanceOf[N]
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