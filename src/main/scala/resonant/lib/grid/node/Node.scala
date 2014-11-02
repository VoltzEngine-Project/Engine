package resonant.lib.grid.node

import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import resonant.api.grid.{INode, INodeProvider}
import resonant.lib.transform.vector.{IVectorWorld, VectorWorld}

/**
 * A node is any single part of a grid.
 * @author Calclavia
 */
abstract class Node(var parent: INodeProvider) extends INode with IVectorWorld
{
  def reconstruct()
  {
  }

  def deconstruct()
  {
  }

  def getParent: INodeProvider = parent

  def position: VectorWorld = new VectorWorld(this)

  def world: World =
  {
    if (getParent.isInstanceOf[TileEntity])
      return (getParent.asInstanceOf[TileEntity]).getWorldObj

    return null
  }

  def z: Double =
  {
    if (getParent.isInstanceOf[TileEntity])
      return (getParent.asInstanceOf[TileEntity]).zCoord

    return 0
  }

  def x: Double =
  {
    if (getParent.isInstanceOf[TileEntity])
      return (getParent.asInstanceOf[TileEntity]).xCoord

    return 0
  }

  def y: Double =
  {
    if (getParent.isInstanceOf[TileEntity])
      return (getParent.asInstanceOf[TileEntity]).yCoord

    return 0
  }

  override def toString: String = getClass.getSimpleName + "[" + hashCode + "]"
}