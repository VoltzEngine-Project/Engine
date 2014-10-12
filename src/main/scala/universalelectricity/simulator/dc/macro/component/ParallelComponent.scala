package universalelectricity.simulator.dc

import java.util

import scala.collection.convert.wrapAll._

/**
 * A junction that consists of parallel branches
 * @author Calclavia
 */
class ParallelComponent extends DCComponent
{
  /**
   * A list of components in the branch. Some components may branch off themselves
   */
  val branches = new util.HashSet[SeriesComponent]()

  /**
   * Solves the current based on the voltage
   */
  override def solve()
  {
    //Set each component's voltage, since in parallel all components have the same voltage drop
    branches foreach (_.setVoltage(voltage))
    branches foreach (_.solve())
  }

  /**
   * Computes resistance in parallel
   * 1/R = 1/R1 + 1/R2
   */
  override def resistance: Double = 1 / branches.foldLeft(0D)((b, a) => b + 1 / a.resistance)
}
