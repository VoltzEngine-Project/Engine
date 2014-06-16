package universalelectricity.core.grid

import universalelectricity.api.core.grid.IUpdate

/**
 * A grid that ticks. May be multi-threaded.
 *
 * @param nodeClass - The class the node
 * @tparam N - The type of the node
 *
 * @author Calclavia
 */
class TickingGrid[N <: Node] extends NodeGrid[N] with IUpdate
{
  /** Upon init, add this grid into the ticker. */
  UpdateTicker.addUpdater(this)

  /**
   * An grid update called only server side.
   */
  def update(deltaTime: Double)
  {
    /**
     * TODO: Perform test to check if parallel evaluation is worth it.
     */
    nodes.par.foreach(_.update(deltaTime))
  }

  def canUpdate() = nodes.size > 0

  def continueUpdate = canUpdate()

}