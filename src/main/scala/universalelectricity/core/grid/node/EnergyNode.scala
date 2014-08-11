package universalelectricity.core.grid.node

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.EnergyStorage
import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.api.core.grid.electric.IEnergyNode
import universalelectricity.compatibility.Compatibility
import scala.collection.convert.wrapAll._

/**
 * Created by Darkguardsman on 7/29/2014.
 */
class EnergyNode(parent: INodeProvider) extends ConnectorNode[IEnergyNode](parent) with IEnergyNode {

  var energy: EnergyStorage = new EnergyStorage()
  protected var keepEnergy: Boolean = false

  override def update(deltaTime: Double)
  {
    if(!keepEnergy)
    {
      var energy : Double = getEnergy
      var cc : Integer = connections.size()
      connections.foreach(
        entry => {
          val node: IEnergyNode = entry._1
          val dir: ForgeDirection = entry._2
          val eToNode: Double = energy/cc
          energy -= node.addEnergy(eToNode, true)
          cc-=1
        }
      )
    }
  }

  override def addEnergy(wattage: Double, doAdd: Boolean): Double =
  {
    return energy.receiveEnergy(wattage, doAdd)
  }

  override def removeEnergy(wattage: Double, doRemove: Boolean): Double =
  {
    return energy.extractEnergy(wattage, doRemove)
  }

  def getEmptySpace = getEnergyCapacity() - getEnergy()

  override def getEnergy: Double = energy.getEnergy

  def setEnergy(value: Double)
  {
    energy.setEnergy(value)
  }

  override def getEnergyCapacity: Double = energy.getEnergyCapacity

  def setEnergyCapacity(value: Double)
  {
    energy.setCapacity(value)
  }

  override def load(nbt: NBTTagCompound)
  {
    energy.readFromNBT(nbt.getCompoundTag("EnergyStorage"))
  }

  override def save(nbt: NBTTagCompound)
  {
    val tag: NBTTagCompound = new NBTTagCompound()
    energy.writeToNBT(tag)
    nbt.setTag("EnergyStorage", tag)
  }

  /** Is the object a or contains an energy node */
  def isEnergyNode(obj : Any, accessSide : ForgeDirection) : Boolean =
  {
    if(obj.isInstanceOf[INodeProvider])
    {
      if(obj.asInstanceOf[INodeProvider].getNode(Class[IEnergyNode], accessSide).isInstanceOf[IEnergyNode])
      {
        return true;
      }
    }else if(obj.isInstanceOf[IEnergyNode])
    {
      return true
    }
    return false
  }
  /** Is the obj a handler of energy */
  def isEnergyHandler(obj : Any) : Boolean =
  {
    return Compatibility.isHandler(obj)
  }
}
