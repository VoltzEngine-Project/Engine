package universalelectricity.simulator.dc.macroscopic

import universalelectricity.api.core.grid.{INode, INodeProvider}
import universalelectricity.core.grid.node.NodeConnector
import universalelectricity.simulator.dc.macroscopic.component.DCComponent

/**
 * @author Calclavia
 */
class DCNode(parent: INodeProvider) extends NodeConnector(parent) with DCComponent
{
  private var _resistance = 0D

  //Resistance cannot be zero or there will be infinite current
  def resistance_=(resistance: Double) = _resistance = Math.max(resistance, Double.MinPositiveValue)

  override def resistance = _resistance

  protected override def getRelativeClass: Class[_ <: INode] = classOf[DCNode]
}
