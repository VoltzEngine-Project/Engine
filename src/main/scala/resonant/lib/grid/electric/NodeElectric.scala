package resonant.lib.grid.electric

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.electric.EnergyStorage
import resonant.api.grid.INodeProvider
import resonant.api.grid.sim.{SimType, IPressure, IFlow}
import resonant.lib.grid.branch.NodeBranchPart
import resonant.lib.prefab.TEnergyBuffer

/**
 * Created by robert on 11/12/2014.
 */
class NodeElectric(parent: INodeProvider) extends NodeBranchPart(parent) with TEnergyBuffer with IFlow with IPressure
{
  var resistance: Double = 0
  var buffer : EnergyStorage = null

  //Constructor
  buffer = new EnergyStorage()

  override def getEnergyStorage(): EnergyStorage = buffer

  /** Current flowing threw this part */
  def getAmps() : Double =
  {
    return getFlow(SimType.ELECTRICITY, ForgeDirection.UNKNOWN)
  }

  override def getFlow(t: SimType, side: ForgeDirection): Double =
  {
    if(t == SimType.ELECTRICITY || t == SimType.ENERGY)
    {

    }
    return 0
  }

  /** Voltage across this part */
  def getVolts() : Double =
  {
    return getPressure(SimType.ELECTRICITY, ForgeDirection.UNKNOWN)
  }

  override def getPressure(t: SimType, side: ForgeDirection): Double =
  {
    if(t == SimType.ELECTRICITY || t == SimType.ENERGY)
    {

    }
    return 0
  }
}