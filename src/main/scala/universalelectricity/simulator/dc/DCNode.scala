package universalelectricity.simulator.dc

import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.core.grid.node.NodeConnector
import universalelectricity.simulator.dc.component.Component

/**
 * @author Calclavia
 */
class DCNode(parent: INodeProvider) extends NodeConnector(parent) with Component
{
  private var resistance = 0D

  def setResistance(resistance: Double)
  {
    //Resistance cannot be zero or there will be infinite current
    this.resistance = Math.max(resistance, Double.MinPositiveValue)
  }

  override def getResistance = resistance
}
