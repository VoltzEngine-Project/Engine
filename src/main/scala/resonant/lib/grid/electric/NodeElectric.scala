package resonant.lib.grid.electric

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.tile.INodeProvider
import resonant.api.tile.node.{NodeType, IPressure, IFlow}
import resonant.lib.grid.branch.NodeBranchPart
import resonant.lib.grid.energy.EnergyStorage
import resonant.lib.prefab.TEnergyBuffer

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
  def current : Double =
  {
    return getFlow(NodeType.ELECTRICITY, ForgeDirection.UNKNOWN)
  }

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