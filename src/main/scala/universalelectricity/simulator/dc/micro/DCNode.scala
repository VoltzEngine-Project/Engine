package universalelectricity.simulator.dc.micro

import java.util
import java.util.{Set => JSet}

import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.INodeProvider
import universalelectricity.core.grid.node.NodeConnector

import scala.collection.convert.wrapAll._

/**
 * Represents a DC component within a circuit.
 *
 * Based on the concept of electricity as being the flow of charge.
 *
 * Charge is taken from negative terminals and pushed to positive terminals. Depending on the change of charge, we can calculate instantaneous current.
 *
 * This is not 100% realistic, but should contain similar mechanics as realistic electricity.
 *
 * @author Calclavia
 */
class DCNode(parent: INodeProvider) extends NodeConnector(parent)
{
  //Charges are pushed to positive terminals
  val positiveTerminals: JSet[ForgeDirection] = new util.HashSet()

  //Charges are taken from negative terminals
  val negativeTerminals: JSet[ForgeDirection] = new util.HashSet()

  private var _current = 0D

  private var prevCharge = 0D
  private var _charge = 0D

  var canCharge = true

  var chargeCapacity = 10000

  private var _resistance = Double.MinPositiveValue

  //Amount of charge to push on the next update
  private var pushChargeBuffer = 0D

  //Gets the instantaneous current of this component
  def current = _current

  //Gets the instantaneous voltage of this component
  def voltage = current * resistance

  def charge = _charge

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

    if (pushChargeBuffer > 0)
    {
      //Attempt to gather charge from negative terminals
      val negatives = connections.filter(c => negativeTerminals.contains(c._2)).keys.map(_.asInstanceOf[DCNode])

      if (negatives.size > 0)
      {
        //TODO: Make it more difficult to gather charge when a side is very negatively charged. Consider exponential difficulty.
        negatives.foreach(_.charge -= pushChargeBuffer / negatives.size)
        charge += pushChargeBuffer
      }

      //TODO: Should we set this to zero?
      pushChargeBuffer = 0

      //TODO: Temporarily solution to prevent backfeeding into the battery
      canCharge = false
    }

    /**
     * Push charge for every component like a "snake".
     * Cause positive charged areas to move to negative charged areas
     * Charge always wants to flow to places with the least resistance.
     * Charge always wants to flow to places with less charge. Electrons repel each other.
     */
    //TODO: We shouldn't need the isInstance check
    val allComponents = connections.keys.filter(_.isInstanceOf[DCNode]).map(_.asInstanceOf[DCNode]).toSet
    //We only want to consider components that have less charge.
    val components = allComponents.filter(c => charge > c.prevCharge && !negativeTerminals.contains(c) && c.canCharge)

    val totalDeltaCharge = components.map(c => charge - c.prevCharge).foldLeft(0D)(_ + _)
    val totalResistance = components.map(_.resistance).foldLeft(0D)(_ + _)

    components.foreach(c =>
    {
      val diff = charge - c.prevCharge
      val chargePerct = diff / totalDeltaCharge
      val resisPerct = c.resistance / totalResistance

      //TODO: Consider resistance
      val transfer = Math.min(charge * chargePerct /*(chargePerct + (1 - resisPerct)) / 2*/ , diff)

      c.charge += transfer
      charge -= transfer
    })
  }

  /**
   * Pushes charges in this DC Component
   * @param chargeAmount
   */
  @deprecated
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
    val totalDeltaCharge = components.map(c => charge - c.charge).foldLeft(0D)(_ + _)
    val totalResistance = components.map(_.resistance).foldLeft(0D)(_ + _)

    components.foreach(c =>
    {
      val diff = charge - c.charge
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
    pushChargeBuffer += charge
  }

  override protected def getRelativeClass = classOf[DCNode]

  override def toString = "DCNode [" + charge.toInt + "C " + current.toInt + "A " + voltage.toInt + "V][" + connections.size + " Connections]"
}
