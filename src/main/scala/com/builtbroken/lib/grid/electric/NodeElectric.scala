package com.builtbroken.lib.grid.electric

import net.minecraftforge.common.util.ForgeDirection
import com.builtbroken.api.tile.INodeProvider
import com.builtbroken.api.tile.node.{NodeType, IPressure, IFlow}
import com.builtbroken.lib.grid.branch.NodeBranchPart
import com.builtbroken.lib.grid.energy.EnergyStorage
import com.builtbroken.lib.prefab.TEnergyBuffer

import scala.beans.BeanProperty

/**
 * A node that runs on electricity
 * @author DarkCow
 */
class NodeElectric(parent: INodeProvider) extends NodeBranchPart(parent) with TEnergyBuffer with IFlow with IPressure
{
  @BeanProperty
  var resistance: Double = 0

  var buffer : EnergyStorage = null

  //Constructor
  buffer = new EnergyStorage()

  override def getEnergyStorage: EnergyStorage = buffer

  /** Current flowing through this node */
  def current : Double = getFlow(NodeType.ELECTRICITY, ForgeDirection.UNKNOWN)

  override def getFlow(t: NodeType, side: ForgeDirection): Double =
  {
    if(t == NodeType.ELECTRICITY || t == NodeType.ENERGY)
    {

    }
    return 0
  }

  /** Voltage across this node */
  def voltage : Double =
  {
    return getPressure(NodeType.ELECTRICITY, ForgeDirection.UNKNOWN)
  }

  override def getPressure(t: NodeType, side: ForgeDirection): Double =
  {
    if(t == NodeType.ELECTRICITY || t == NodeType.ENERGY)
    {

    }
    return 0
  }
}