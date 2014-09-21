package universalelectricity.simulator.dc

import universalelectricity.api.core.grid.{INode, INodeProvider}
import universalelectricity.core.grid.node.NodeConnector
import universalelectricity.simulator.dc.component.DCComponent

/**
 * @author Calclavia
 */
class DCNode(parent: INodeProvider) extends NodeConnector(parent) with DCComponent
{
  private var resistance = 0D

  def setResistance(resistance: Double)
  {
    //Resistance cannot be zero or there will be infinite current
    this.resistance = Math.max(resistance, Double.MinPositiveValue)
  }

  override def getResistance = resistance

  protected override def getRelativeClass: Class[_ <: INode] = classOf[DCNode]
}
