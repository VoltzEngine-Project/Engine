package resonant.lib.grid.node

import java.util
import java.util.{Set => JSet}

import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.{INodeProvider, IUpdate}
import resonant.lib.grid.UpdateTicker

import scala.collection.convert.wrapAll._

/**
 * Represents a direct current component within a circuit.
 *
 * Based on the concept of electricity as being the flow of charge.
 *
 * Charge is taken from negative terminals and pushed to positive terminals. Depending on the change of charge, we can calculate instantaneous current.
 *
 * This is not 100% realistic, but should contain similar mechanics as realistic electricity.
 *
 * TODO: Check why += is not working.
 *
 * @author Calclavia
 */
class DCNode(parent: INodeProvider) extends NodeEnergy[DCNode](parent) with IUpdate with TileConnector[DCNode]
{
  //Charges are pushed to positive terminals
  val positiveTerminals: JSet[ForgeDirection] = new util.HashSet()

  private var _current = 0D
  var chargeCapacity = 10000D
  private var _charge = chargeCapacity
  private var chargeAccumulator = 0D

  private var _resistance = Double.MinPositiveValue

  //Amount of charge to push on the next update
  private var pushChargeBuffer = 0D

  //Gets the instantaneous current of this component
  def current = _current

  //Gets the instantaneous voltage of this component
  def voltage = current * resistance

  def charge = _charge

  def charge_=(newCharge: Double) = _charge = Math.min(newCharge, chargeCapacity)

  //Gets the resistance of this component
  def resistance = _resistance

  //Resistance cannot be zero or there will be infinite current
  def resistance_=(resistance: Double) = _resistance = Math.max(resistance, Double.MinPositiveValue)

  override def energy = charge * voltage

  override def power = current * voltage

  /**
   * Called during reconstruct to build the connection map. This is a general way used to search all adjacent TileEntity to see and try to connect to it.
   */
  override def reconstruct(): Unit =
  {
    UpdateTicker.addUpdater(this)
  }

  override def canUpdate = true

  override def continueUpdate = true

  override def update(deltaTime: Double)
  {
    //Calculate current based on the change of charges over time
    _current = chargeAccumulator / deltaTime
    chargeAccumulator = 0

    if (pushChargeBuffer > 0)
    {
      charge = charge - pushChargeBuffer
      var remain = 0D
      val positiveNodes = directionMap.filter(c => positiveTerminals.contains(c._2)).keys
      positiveNodes.foreach(c => remain = remain + c.push(pushChargeBuffer / positiveNodes.size, this))
      println("DCNode: Failed to push amount: " + remain)
      charge = charge + remain
      pushChargeBuffer = 0
    }
  }

  /**
   * Pushes charges in this DC Component in a snake fashion.
   *
   * Charges are pushed based on resistance and how "less" charged another area is.
   * All components start with no charge. A negative charge is created at negative terminals and the positive terminal gets pushed.
   *
   * TODO: Consider queuing into another thread instead of requiring all components ticking
   * TODO: Cache pathfinding operations in the grid
   * @param pushCharge The amount of charge in coulombs
   */
  def buffer(pushCharge: Double)
  {
    pushChargeBuffer = pushChargeBuffer + pushCharge
  }


  /**
   *
   * This recursive function will gather the paths into a list, then push charges backwards.
   *
   * @param pushAmount - The amount of charges we are pushing
   * @param passed - The nodes we already went through while pushing
   */
  protected def push(pushAmount: Double, passed: DCNode*): Double =
  {
    val excluded = passed :+ this

    val transfer = Math.min(charge + pushAmount, chargeCapacity) - charge
    var remain = pushAmount - transfer
    charge = charge  + transfer

    if (transfer > 0)
    {
      passed.foreach(c => c.chargeAccumulator = c.chargeAccumulator + transfer)
      println("Reached low charge area!")
    }

    val components = connections.filter(c => if (passed.size == 1) !excluded.contains(c) else !excluded.drop(1).contains(c))

    //    println("Stepping over: " + passed.size + " " + pushAmount + " " + remain)

    /**
     * Distribution:
     * Charge always wants to flow to places with the least resistance.
     * Charge always wants to flow to places with less charge. Like charges repel.
     */
    //    val totalDeltaCharge = components.map(c => charge - c.charge).foldLeft(0D)(_ + _)
    //    val totalResistance = components.map(_.resistance).foldLeft(0D)(_ + _)

    components.foreach(c =>
    {
      /*
      val diff = charge - c.charge
      val chargePerct = diff / totalDeltaCharge
      val resisPerct = c.resistance / totalResistance

      //TODO: Consider resistance
      val transfer = Math.min(charge * chargePerct /*(chargePerct + (1 - resisPerct)) / 2*/ , diff)
      */

      if (remain > 0)
      {
        val pushRemain = c.push(remain, excluded: _*)
        remain = remain - pushRemain
      }
    })

    return remain
  }

  /**
   * The class used to compare when making connections
   */
  override protected def getCompareClass = getClass

  override def toString = "DCNode [" + charge.toInt + "C " + current.toInt + "A " + voltage.toInt + "V][" + connections.size + " Connections]"
}
