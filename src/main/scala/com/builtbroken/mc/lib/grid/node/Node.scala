package com.builtbroken.mc.lib.grid.node

import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import com.builtbroken.mc.api.tile.INodeProvider
import com.builtbroken.mc.api.tile.node.INode
import com.builtbroken.mc.lib.transform.vector.TVectorWorld

/**
 * A node is any single part of a grid.
 * @author Calclavia
 */
abstract class Node(var parent: INodeProvider) extends INode with TVectorWorld
{
  override def reconstruct()
  {
  }

  override def deconstruct()
  {
  }

  override def getParent: INodeProvider = parent

  override def x: Double =
  {
    return parent match
    {
      case x: TileEntity => x.xCoord
      case x: Entity => x.posX
      case _ => 0
    }
  }

  override def y: Double =
  {
    return parent match
    {
      case x: TileEntity => x.yCoord
      case x: Entity => x.posY
      case _ => 0
    }
  }

  override def z: Double =
  {
    return parent match
    {
      case x: TileEntity => x.zCoord
      case x: Entity => x.posZ
      case _ => 0
    }
  }

  override def world: World =
  {
    return parent match
    {
      case x: TileEntity => x.getWorldObj
      case x: Entity => x.worldObj
      case _ => null
    }
  }

  /**
   * Helper method to gets a node on the side
   * @param nodeType - class of the node
   * @param dir - side of the tile
   * @tparam N - generic to force the return to equal the nodeType
   * @return the node, or null if no node
   */
  def getNode[N <: INode](nodeType : Class[_ <: N], dir: ForgeDirection) : N =
  {
    val tile = (toVectorWorld + dir).getTileEntity
    if (tile != null && tile.isInstanceOf[INodeProvider])
    {
      return tile.asInstanceOf[INodeProvider].getNode(nodeType, dir.getOpposite)
    }
    return null.asInstanceOf[N]
  }

  override def toString: String = getClass.getSimpleName + "[" + hashCode + "]"
}