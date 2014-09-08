package universalelectricity.simulator.dc

import java.util._

import universalelectricity.api.core.grid.IUpdate
import universalelectricity.core.grid.{Grid, UpdateTicker}
import universalelectricity.core.transform.vector.Vector3
import universalelectricity.simulator.dc.component.SeriesComponent
import universalelectricity.simulator.parts.{NetworkNode, NetworkPart}

import scala.collection.JavaConversions._

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman, Calclavia
 */
class DCGrid extends Grid[DCNode](classOf[DCNode]) with IUpdate
{
  private var circuit: SeriesComponent = _

  protected var hasChanged = false
  protected var changeLocations = new HashSet[Vector3]
  protected var ticks = 0L

  UpdateTicker.addUpdater(this)

  override def add(node: NetworkNode)
  {
    hasChanged = true
    super.add(node)
  }

  override def remove(node: NetworkNode)
  {
    hasChanged = true
    super.remove(node)
  }

  def update(deltaTime: Double)
  {
    if (ticks == 0 || hasChanged)
    {
      hasChanged = false
      rebuildGrid()
    }

    updateSimulation()

    ticks = ticks + 1 % Long.MAX_VALUE
  }

  /**
   * Maps the entire network out from start to finish
   */
  def rebuildGrid
  {
    getNodes foreach (_.reconstruct)

    for (node <- getNodes)
    {
      node.reconstruct
    }

    val networkPathFinder: Nothing = new Nothing(this)
    val parts: List[NetworkPart] = networkPathFinder.generateParts
    calculateDeltaPoints
    updateSimulation
  }

  /** Called each update to simulate changes */
  def updateSimulation
  {
    circuit.solveEnergy()
  }

  override def canUpdate = true

  override def continueUpdate = getNodes.size > 0
}