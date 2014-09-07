package universalelectricity.simulator.dc

/**
 * A single node wrapper component
 * @author Calclavia
 */
class NodeComponent extends Component
{
  private var resistance = 0D

 override def getResistance: Double = resistance
}
