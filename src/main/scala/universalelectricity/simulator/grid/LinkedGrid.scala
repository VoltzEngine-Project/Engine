package universalelectricity.simulator.grid

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.api.core.grid.{INode, INodeProvider, IUpdate}
import universalelectricity.compatibility.Compatibility
import universalelectricity.core.grid.{Grid, UpdateTicker}
import universalelectricity.simulator.dc.CircuitSolver
import universalelectricity.simulator.grid.parts.NetworkNode

import scala.collection.JavaConversions._

/**
 * Basic network of parts that function together to simulate a collection of co-existing tiles.
 * @author Darkguardsman, Calclavia
 */
abstract class LinkedGrid[N](clazz: Class[_]) extends Grid[N](clazz) with IUpdate
{
  protected var changed = false
  protected var ticks = 0L

  UpdateTicker.addUpdater(this)

  override def add(node: N)
  {
    changed = true
    super.add(node)
  }

  override def remove(node: N)
  {
    changed = true
    super.remove(node)
  }

  def update(deltaTime: Double)
  {
    if (ticks == 0 || changed)
    {
      changed = false
      rebuildGrid()
    }

    updateSimulation()

    ticks = ticks + 1 % Long.MAX_VALUE
  }

  /**
   * Maps the entire network out from start to finish
   */
  def rebuildGrid()
  {
    getNodes foreach (_.reconstruct)
    circuit = new CircuitSolver(this).solve()
  }

  /** Called each update to simulate changes */
  def updateSimulation
  {
    circuit.solve()
  }

  /** Updates changes to connections preventing a network wide remap */
  def updateConnections
  {
    for (vec <- changeLocations)
    {
      var connections: Int = 0
      for (direction <- ForgeDirection.VALID_DIRECTIONS)
      {
        val tile: TileEntity = vec.clone.add(direction).getTileEntity
        if (tile.isInstanceOf[INodeProvider])
        {
          val node: INode = (tile.asInstanceOf[INodeProvider]).getNode(classOf[NetworkNode], direction.getOpposite)
          if (node.isInstanceOf[NetworkNode] && (node.asInstanceOf[NetworkNode]).getGrid eq this)
          {
            connections += 1
            continue //todo: continue is not supported
          }
        }
        if (Compatibility.isHandler(tile))
        {
          connections += 1
        }
      }
      if (connections > 2)
      {
      }
      else if (connections > 1)
      {
      }
    }
  }

  /** Is this object(most likely a tile) an input of data(most likely energy) from the network */
  def isInputDevice(`object`: AnyRef, connectingFrom: ForgeDirection): Boolean =
  {
    return false
  }

  /** Is this object(most likely a tile) an output of data(most likely energy) for the network */
  def isOutputDevice(`object`: AnyRef, connectingFrom: ForgeDirection): Boolean =
  {
    return false
  }

  override def canUpdate = true

  override def continueUpdate = getNodes.size > 0
}