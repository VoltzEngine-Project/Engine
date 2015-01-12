package com.builtbroken.mc.lib.grid.electric

import net.minecraftforge.common.util.ForgeDirection
import com.builtbroken.mc.api.tile.ITileModuleProvider
import com.builtbroken.mc.api.tile.node.{NodeType, IPressure, IFlow}
import com.builtbroken.mc.lib.grid.branch.NodeBranchPart
import com.builtbroken.mc.lib.grid.energy.EnergyStorage
import com.builtbroken.mc.prefab.TEnergyBuffer

import scala.beans.BeanProperty

/**
 * A node that runs on electricity
 * @author DarkCow
 */
class NodeElectric(parent: ITileModuleProvider) extends NodeBranchPart(parent) with TEnergyBuffer with IFlow with IPressure
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

  override def getNodeFromConnection(provider: ITileModuleProvider, dir: ForgeDirection): NodeBranchPart =
  {
    return provider.getModule(classOf[NodeElectric], dir);
  }
}