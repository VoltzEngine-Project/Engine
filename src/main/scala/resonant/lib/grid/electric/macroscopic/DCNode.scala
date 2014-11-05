package resonant.lib.grid.electric.macroscopic

import resonant.api.grid.INodeProvider
import resonant.lib.grid.electric.macroscopic.component.DCComponent
import resonant.lib.grid.node.NodeConnector

/**
 * @author Calclavia
 */
class DCNode(parent: INodeProvider) extends NodeConnector(parent) with DCComponent
{
  private var _resistance = 0D

  //Resistance cannot be zero or there will be infinite current
  def resistance_=(resistance: Double) = _resistance = Math.max(resistance, Double.MinPositiveValue)

  override def resistance = _resistance
}
