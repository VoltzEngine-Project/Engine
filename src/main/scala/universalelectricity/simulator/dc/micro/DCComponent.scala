package universalelectricity.simulator.dc.micro

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
class DCComponent(parent: INodeProvider) extends NodeConnector(parent)
{
  private var _current = 0D
  private var _charge = 0D
  private var _resistance = Double.MinPositiveValue

  private var chargeBuffer = 0D

  //Gets the instantaneous current of this component
  def current = _current

  //Gets the instantaneous voltage of this component
  def voltage = current * resistance

  def charge = _charge

  //Gets the resistance of this component
  def resistance = _resistance

  //Resistance cannot be zero or there will be infinite current
  def resistance_=(resistance: Double) = _resistance = Math.max(resistance, Double.MinPositiveValue)

  def energy = charge * voltage

  def power = current * voltage

  def update(deltaTime: Float)
  {
    val chargeBefore = _charge

    _charge = Math.max(charge, chargeBuffer)
    //TODO: Should we set this to zero?
    chargeBuffer = 0

    /**
     * Push charge for every component like a "snake".
     * Charge always wants to flow to places with the least resistance.
     * Charge always wants to flow to places with less charge. Electrons repell each other.
     */
    val components = connections.keys.map(_.asInstanceOf[DCComponent])

    //We only want to push to components that have less charge.
    val comp = components.filter(_charge > _._charge)

    val totalDeltaCharge = comp.map(_charge - _._charge).reduceLeft(_ + _)
    val totalResistance = comp.map(_.resistance).reduceLeft(_ + _)

    comp.foreach(c =>
    {
      val chargePerct = (_charge - c._charge) / totalDeltaCharge
      val resisPerct = c.resistance / totalResistance
      val transfer = _charge * (chargePerct + (1 - resisPerct)) / 2

      c._charge += transfer
      _charge -= transfer
    })

    //Calculate current based on the change of charges over time
    _current = Math.abs(chargeBefore - _charge) / deltaTime
  }

  /**
   * Charges this DC Component
   * @param charge The amount of charge in coulombs
   */
  def charge(charge: Double)
  {
    chargeBuffer += charge
  }
}
