package universalelectricity.simulator.dc.micro

import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.core.grid.node.NodeConnector

import scala.collection.convert.wrapAll._

/**
 * Represents a DC component within a circuit.
 *
 * Based on the concept of electricity as being the flow of charge.
 * This is not 100% realistic, but should contain similar mechanics as realistic electricity.
 *
 * @author Calclavia
 */
class DCNode(parent: INodeProvider) extends NodeConnector(parent)
{
  private var _current = 0D

  private var prevCharge = 0D
  private var _charge = 0D
  var chargeCapacity = 10000

  private var _resistance = Double.MinPositiveValue

  private var chargeBuffer = 0D

  //Gets the instantaneous current of this component
  def current = _current

  //Gets the instantaneous voltage of this component
  def voltage = current * resistance

  def charge = _charge

  def charge(terminal: ForgeDirection) : Double = charge

  def charge_=(charge: Double) = _charge = Math.min(_charge + charge, chargeCapacity)

  //Gets the resistance of this component
  def resistance = _resistance

  //Resistance cannot be zero or there will be infinite current
  def resistance_=(resistance: Double) = _resistance = Math.max(resistance, Double.MinPositiveValue)

  def energy = charge * voltage

  def power = current * voltage

  def update(deltaTime: Float)
  {
    //Calculate current based on the change of charges over time
    _current = Math.abs(charge - prevCharge) / deltaTime

    prevCharge = charge

    if (chargeBuffer > 0)
    {
      push(chargeBuffer)
      //TODO: Should we set this to zero?
      chargeBuffer = 0
    }
  }

  /**
   * Pushes charges in this DC Component
   * @param chargeAmount
   */
  def push(chargeAmount: Double, exclude: DCNode*)
  {
    val excluded = exclude :+ this

    val allComponents = connections.keys.map(_.asInstanceOf[DCNode]).toSet
    //We only want to consider components that have less charge.
    val components = allComponents.filter(c => charge > c.charge && !excluded.contains(c))

    /**
     * Push charge for every component like a "snake".
     * Charge always wants to flow to places with the least resistance.
     * Charge always wants to flow to places with less charge. Electrons repel each other.
     */
    val totalDeltaCharge = components.map(c => charge - c.charge(connections.get(c))).foldLeft(0D)(_ + _)
    val totalResistance = components.map(_.resistance).foldLeft(0D)(_ + _)

    components.foreach(c =>
    {
      val diff = charge - c.charge(connections.get(c))
      val chargePerct = diff / totalDeltaCharge
      val resisPerct = c.resistance / totalResistance

      //TODO: Consider resistance
      val transfer = Math.min(charge * chargePerct /*(chargePerct + (1 - resisPerct)) / 2*/ , diff)

      c.push(charge, excluded: _*)
      charge -= transfer
    })

    charge += chargeAmount
  }

  /**
   * Pushes charges in this DC Component
   * @param charge The amount of charge in coulombs
   */
  def buffer(charge: Double)
  {
    chargeBuffer += charge
  }

  override def toString = "DCNode [" + charge.toInt + "C " + current.toInt + "A " + voltage.toInt + "V]"
}
