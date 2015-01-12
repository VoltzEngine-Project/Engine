package com.builtbroken.mc.lib.grid.branch

import com.builtbroken.mc.api.IGrid
import com.builtbroken.mc.api.tile.{IGridProvider, ITileModuleProvider}
import com.builtbroken.mc.lib.grid.branch.part.Part
import com.builtbroken.mc.lib.node.AbstractNode
import com.builtbroken.mc.lib.transform.vector.Location
import com.builtbroken.mc.prefab.TConnector
import net.minecraftforge.common.util.ForgeDirection

import scala.beans.BeanProperty
;

/**
 * A node that is part of a branch
 * @author DarkCow
 */
abstract class NodeBranchPart(parent: ITileModuleProvider) extends AbstractNode(parent) with TConnector[NodeBranchPart] with IGridProvider
{
  @BeanProperty
  var branch : Part = null

  var grid : BranchedGrid = null

  /** Sets the grid reference */
  override def setGrid(grid: IGrid[_]):Unit =
  {
    if(grid.isInstanceOf[BranchedGrid])
      this.grid = grid.asInstanceOf[BranchedGrid]
    else if(grid == null)
      this.grid = null
  }

  /** Gets the grid reference */
  override def getGrid: BranchedGrid =
  {
    if(grid == null)
    {
      //If no grid found threw connections
      if(grid == null)
      {
        grid = new BranchedGrid()
        grid.add(this)
      }
    }
    return grid
  }

  /** Is this connector allowed to connect to any side
    * @param connector - any connecting object, Most likely TileEntity, Node, INodeProvider */
  override protected def isValidConnector(connector: NodeBranchPart): Boolean =
  {
    return connector != null;
  }

  override def onJoinWorld()
  {
    super.onJoinWorld()
    buildConnections()
  }

  override def onLeaveWorld()
  {
    super.onLeaveWorld()
    getConnections.clear()
    setGrid(null)
    if(branch != null)
      branch.remove(this)
    branch = null
  }


  override def connect(obj: NodeBranchPart, dir: ForgeDirection)
  {
    super.connect(obj, dir)
    if(obj.getGrid != this.getGrid)
      obj.getGrid.merge(getGrid)
  }

  override def updateConnection(dir: ForgeDirection, loc: Location)
  {
    val tile = loc.getTileEntity
    if(tile != null && tile.isInstanceOf[ITileModuleProvider])
    {
        val node : NodeBranchPart = tile.asInstanceOf[ITileModuleProvider].getModule(classOf[NodeBranchPart], dir.getOpposite)

        if (canConnect(node, dir.getOpposite))
        {
          connect(node, dir)
        }
    }
  }
}