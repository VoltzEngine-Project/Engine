package resonant.lib.grid.node

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.{INode, INodeProvider}

/**
 * A trait applied to NodeConnector
 * @author Calclavia
 */
trait TileConnector[A] extends NodeConnector[A]
{
  override def reconstruct()
  {
    super.reconstruct()

    for (direction <- ForgeDirection.VALID_DIRECTIONS)
    {
      if (canConnect(direction))
      {
        val tile: TileEntity = (position + direction).getTileEntity
        val node: INode = getNodeFrom(tile, direction.getOpposite)

        if (node != null)
        {
          connect(node.asInstanceOf[A], direction)
        }
      }
    }
  }

  protected def getNodeFrom(tile: TileEntity, from: ForgeDirection): INode =
  {
    if (tile.isInstanceOf[INodeProvider])
    {
      val node = tile.asInstanceOf[INodeProvider].getNode(getCompareClass, from)

      if (node != null)
      {
        return node
      }
    }

    return null
  }

  /**
   * The class used to compare when making connections
   */
  protected def getCompareClass: Class[_ <: A with INode]
}
