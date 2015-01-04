package com.builtbroken.lib.grid.electric.part

import com.builtbroken.lib.grid.branch.NodeBranchPart
import com.builtbroken.lib.grid.branch.part.Part
import com.builtbroken.lib.grid.electric.NodeElectric
import scala.collection.JavaConversions._

/**
 * Created by robert on 11/17/2014.
 */
trait TElectricPart extends Part
{
  var resistance : Double = 0

  def getResistance(): Double = return resistance

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
