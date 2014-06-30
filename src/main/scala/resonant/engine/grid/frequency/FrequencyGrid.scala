package resonant.engine.grid.frequency

import java.util._

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

  def getNodes(clazz: Class[_], p: IBlockFrequency => Boolean): Set[IBlockFrequency] = getNodes(n => (n.getClass().isAssignableFrom(clazz) && p(n)))

  override def getNodes(clazz: Class[_]): Set[IBlockFrequency] = getNodes(_.getClass().isAssignableFrom(clazz))

  override def getNodes(frequency: Int): Set[IBlockFrequency] = getNodes(_.getFrequency() == frequency)

  override def getNodes(clazz: Class[_], frequency: Int): Set[IBlockFrequency] = getNodes(n => n.getFrequency() == frequency && n.getClass().isAssignableFrom(clazz))

  override def getNodes(world: World, position: Vector3, radius: Int, frequency: Int): Set[IBlockFrequency] =
  {
    return getNodes(n => n.getFrequency() == frequency && n.asInstanceOf[TileEntity].getWorldObj() == world && new Vector3(n.asInstanceOf[TileEntity]).distance(position) <= radius)
  }

  override def getNodes(clazz: Class[_], world: World, position: Vector3, radius: Int, frequency: Int): Set[IBlockFrequency] =
  {
    return getNodes(n => n.getFrequency() == frequency && n.getClass().isAssignableFrom(clazz) && n.asInstanceOf[TileEntity].getWorldObj() == world && new Vector3(n.asInstanceOf[TileEntity]).distance(position) <= radius)
  }

  override def getNodes(world: World, cuboid: Cuboid, frequency: Int): Set[IBlockFrequency] =
  {
    return getNodes(n => n.getFrequency() == frequency && n.asInstanceOf[TileEntity].getWorldObj() == world && cuboid.intersects(new Vector3(n.asInstanceOf[TileEntity])))
  }

  override def getNodes(clazz: Class[_], world: World, cuboid: Cuboid, frequency: Int): Set[IBlockFrequency] =
  {
    return getNodes(n => n.getFrequency() == frequency && n.getClass().isAssignableFrom(clazz) && n.asInstanceOf[TileEntity].getWorldObj() == world && cuboid.intersects(new Vector3(n.asInstanceOf[TileEntity])))
  }
}