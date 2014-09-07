package universalelectricity.simulator.dc

import java.util

import scala.collection.convert.wrapAll._

/**
 * A junction that consists of parallel branches
 * @author Calclavia
 */
class ParallelComponent extends Component
{
  /**
   * A list of components in the branch. Some components may branch off themselves
   */
  val branches = new util.HashSet[SeriesComponent]()

  /**
   * Computes resistance in parallel
   * 1/R = 1/R1 + 1/R2
   */
  override def getResistance: Double = 1 / branches.foldLeft(0D)((b, a) => b + 1 / a.getResistance)
}
