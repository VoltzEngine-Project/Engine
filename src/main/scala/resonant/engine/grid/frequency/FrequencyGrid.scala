package resonant.engine.grid.frequency

import java.util.Set

import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import resonant.api.blocks.IBlockFrequency
import resonant.api.mffs.fortron.FrequencyGridRegistry
import universalelectricity.core.grid.Grid
import universalelectricity.core.transform.region.Cuboid
import universalelectricity.core.transform.vector.Vector3

import scala.collection.convert.wrapAll._

class FrequencyGrid extends Grid[IBlockFrequency] with FrequencyGridRegistry.IFrequencyGrid
{
  /**
   * Adds a node to the grid.
   * @param node
   */
  override def add(node: IBlockFrequency)
  {
    getNodes().synchronized(super.add(node))
  }

  /**
   * Removes a node to the grid.
   * @param node
   */
  override def remove(node: IBlockFrequency)
  {
    getNodes().synchronized(super.remove(node))
  }

  def getNodes(p: IBlockFrequency => Boolean): Set[IBlockFrequency] = getNodes().filter(p)

  def getNodes[C <: IBlockFrequency](clazz: Class[C], p: C => Boolean): Set[C] = getNodes(n => (clazz.isAssignableFrom(n.getClass()) && p(n.asInstanceOf[C]))).asInstanceOf[Set[C]]

  override def getNodes[C <: IBlockFrequency](clazz: Class[C]): Set[C] = getNodes(n => clazz.isAssignableFrom(n.getClass())).asInstanceOf[Set[C]]

  override def getNodes(frequency: Int): Set[IBlockFrequency] = getNodes(_.getFrequency() == frequency)

  override def getNodes[C <: IBlockFrequency](clazz: Class[C], frequency: Int): Set[C] = getNodes(n => n.getFrequency() == frequency && clazz.isAssignableFrom(n.getClass())).asInstanceOf[Set[C]]

  override def getNodes(world: World, position: Vector3, radius: Int, frequency: Int): Set[IBlockFrequency] =
  {
    return getNodes(n => n.getFrequency() == frequency && n.asInstanceOf[TileEntity].getWorldObj() == world && new Vector3(n.asInstanceOf[TileEntity]).distance(position) <= radius)
  }

  override def getNodes[C <: IBlockFrequency](clazz: Class[C], world: World, position: Vector3, radius: Int, frequency: Int): Set[C] =
  {
    return getNodes(n => n.getFrequency() == frequency && clazz.isAssignableFrom(n.getClass) && n.asInstanceOf[TileEntity].getWorldObj() == world && new Vector3(n.asInstanceOf[TileEntity]).distance(position) <= radius).asInstanceOf[Set[C]]
  }

  override def getNodes(world: World, cuboid: Cuboid, frequency: Int): Set[IBlockFrequency] =
  {
    return getNodes(n => n.getFrequency() == frequency && n.asInstanceOf[TileEntity].getWorldObj() == world && cuboid.intersects(new Vector3(n.asInstanceOf[TileEntity])))
  }

  override def getNodes[C <: IBlockFrequency](clazz: Class[C], world: World, cuboid: Cuboid, frequency: Int): Set[C] =
  {
    return getNodes(n => n.getFrequency() == frequency && clazz.isAssignableFrom(n.getClass()) && n.asInstanceOf[TileEntity].getWorldObj() == world && cuboid.intersects(new Vector3(n.asInstanceOf[TileEntity]))).asInstanceOf[Set[C]]
  }
}