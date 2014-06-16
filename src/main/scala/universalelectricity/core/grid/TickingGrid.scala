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
class TickingGrid[N <: Node](nodeClass: Class[_ <: N]) extends NodeGrid[N](nodeClass) with IUpdate
{
  /** Upon init, add this grid into the ticker. */
  UpdateTicker.addUpdater(this)

  /**
   * An grid update called only server side.
   */
  def update(deltaTime: Double)
  {
    //TODO: Optimize parallel evaluation of nodes only after node count exceeds a specific limit.
    nodes.par.foreach(_.update(deltaTime))
  }


  def canUpdate() = nodes.size > 0

  def continueUpdate = canUpdate()

}