package resonant.lib.grid.electric.part

import resonant.lib.grid.branch.NodeBranchPart
import resonant.lib.grid.branch.part.Part
import resonant.lib.grid.electric.NodeElectric

/**
 * Created by robert on 11/17/2014.
 */
trait TElectricPart extends Part
{
  var resistance = 0

  def getResistance(): Double =
  {
    return resistance
  }

  /** Calculates resistance of the part */
  def calculateResistance()
  {
    resistance = 0
    for(node: NodeBranchPart <- getEcapsulatedNodes)
    {
      if(node.isInstanceOf[NodeElectric])
      {
        resistance += node.asInstanceOf[NodeElectric].getResistance
      }
    }
  }
}
