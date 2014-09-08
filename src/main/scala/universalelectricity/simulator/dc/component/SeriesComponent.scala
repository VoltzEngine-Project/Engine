package universalelectricity.simulator.dc.component

import java.util

import scala.collection.convert.wrapAll._

/**
 * The voltage of the branch is determined by the sources of voltage (e.g battery cells)
 * @author Calclavia
 */
class SeriesComponent extends Component
{
  /**
   * A list of components in the branch. Some components may consist of parallel branches.
   */
  val components = new util.LinkedList[Component]()

  /**
   * Solves the current based on the voltage
   */
  def solveEnergy()
  {
    //Calculate current
    val resistance = getResistance
    current = getVoltage / resistance

    //Set each component's current, since in a series all components have the same current
    components foreach (_.setCurrent(current))
  }

  /**
   * Computes resistance by sum of all resistances in series
   * R = R1 + R2
   * TODO: Consider cache?
   */
  override def getResistance: Double = components.foldLeft(0D)((b, a) => b + a.getResistance)
}
