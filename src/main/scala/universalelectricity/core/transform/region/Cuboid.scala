package universalelectricity.core.transform.region

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.ArrayList
import java.util.List
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import net.minecraft.nbt.NBTTagCompound
import universalelectricity.core.transform.TraitOperation
import universalelectricity.core.transform.vector.Vector3
import universalelectricity.core.transform.rotation.Rotation

/**
 * A cubical region class to specify a region.
 *
 * @author Calclavia
 */
object Cuboid
{
  def full() = new Cuboid(0, 0, 0, 1, 1, 1)
}

class Cuboid(var min: Vector3, var max: Vector3) extends TraitOperation[Cuboid]
{
  def this() = this(new Vector3, new Vector3)

  def this(amount: Double) = this(new Vector3, new Vector3(amount))

  def this(cuboid: Cuboid) = this(cuboid.min.clone, cuboid.max.clone)

  def this(minx: Double, miny: Double, minz: Double, maxx: Double, maxy: Double, maxz: Double) = this(new Vector3(minx, miny, minz), new Vector3(maxx, maxy, maxz))

  def this(aabb: AxisAlignedBB) = this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ)

  def this(block: Block) = this(block.getBlockBoundsMinX, block.getBlockBoundsMinY, block.getBlockBoundsMinZ, block.getBlockBoundsMaxX, block.getBlockBoundsMaxY, block.getBlockBoundsMaxZ)

  def toAABB: AxisAlignedBB = AxisAlignedBB.getAABBPool.getAABB(min.x, min.y, min.z, max.x, max.y, max.z)

  def toRectangle: Rectangle = new Rectangle(min.toVector2, max.toVector2)

  override def set(other: Cuboid): Cuboid =
  {
    min = other.min.clone
    max = other.max.clone
    return this
  }

  /**
   * Conversion
   */
  override def round: Cuboid = new Cuboid(min.round, max.round)

  override def ceil: Cuboid = new Cuboid(min.ceil, max.ceil)

  override def floor: Cuboid = new Cuboid(min.floor, max.floor)

  override def max(other: Cuboid): Cuboid = new Cuboid(min.max(other.min), max.max(other.max))

  override def min(other: Cuboid): Cuboid = new Cuboid(min.min(other.min), max.min(other.max))

  override def reciprocal(): Cuboid = new Cuboid(min.reciprocal, max.reciprocal)

  /**
   * Operations
   */
  override def +(amount: Double): Cuboid = new Cuboid(min + amount, max + amount)

  override def +(amount: Cuboid): Cuboid = new Cuboid(min + amount.min, max + amount.max)

  def +(vec: Vector3): Cuboid =
  {
    min + vec
    max + vec
    return this
  }

  def +=(vec: Vector3): Cuboid =
  {
    min += vec
    max += vec
    return this
  }

  def -(vec: Vector3): Cuboid = this + (vec * -1)

  def -=(vec: Vector3): Cuboid = this += (vec * -1)

  def add(vec: Vector3): Cuboid = this + vec

  def addSet(vec: Vector3): Cuboid = this += vec

  def subtract(vec: Vector3): Cuboid = this - vec

  def subtractSet(vec: Vector3): Cuboid = this -= vec

  def *(amount: Double): Cuboid =
  {
    min * amount
    max * amount
    return this
  }

  def *(amount: Cuboid): Cuboid =
  {
    min * amount.min
    max * amount.max
    return this
  }

  def rotate(angle: Rotation): Cuboid =
  {
    min.apply(angle)
    max.apply(angle)
    return this
  }

  def setBounds(block: Block): Cuboid =
  {
    block.setBlockBounds(min.x.asInstanceOf[Float], min.y.asInstanceOf[Float], min.z.asInstanceOf[Float], max.x.asInstanceOf[Float], max.y.asInstanceOf[Float], max.z.asInstanceOf[Float])
    return this
  }

  def intersects(point: Vector3): Boolean =
  {
    return (point.x > this.min.x && point.x < this.max.x) && (point.y > this.min.y && point.y < this.max.y) && (point.z > this.min.z && point.z < this.max.z)
  }

  def intersects(other: Cuboid): Boolean =
  {
    return max.x - 1E-5 > other.min.x && other.max.x - 1E-5 > min.x && max.y - 1E-5 > other.min.y && other.max.y - 1E-5 > min.y && max.z - 1E-5 > other.min.z && other.max.z - 1E-5 > min.z
  }

  def center: Vector3 = (min + max) / 2

  def expand(difference: Vector3): Cuboid =
  {
    min - difference
    max + difference
    return this
  }

  def expand(difference: Double): Cuboid =
  {
    min - difference
    max + difference
    return this
  }

  /**
   * Iterates through block positions in this cubic region.
   * @param callback - The method we want to call back
   */
  def foreach(callback: Vector3 => Unit)
  {
    for (x <- min.xi until max.xi)
    {
      for (y <- min.yi until max.yi)
      {
        for (z <- min.zi until max.zi)
        {
          callback(new Vector3(x, y, z).floor);
        }
      }
    }
  }

  /** @return List of vector block positions within this region. */
  def getVectors: List[Vector3] =
  {
    val vectors = new ArrayList[Vector3];
    foreach(vector => vectors.add(vector))
    return vectors
  }

  def getVectors(center: Vector3, radius: Int): List[Vector3] =
  {
    val vectors = new ArrayList[Vector3];
    foreach(vector => if (center.distance(vector) <= radius) vectors.add(vector))
    return vectors
  }

  /** Returns all entities in this region. */
  def getEntities(world: World): List[Entity] = getEntities(world, classOf[Entity])

  def getEntities[E <: Entity](world: World, entityClass: Class[E]): List[E] = world.getEntitiesWithinAABB(entityClass, toAABB).asInstanceOf[List[E]]

  def getEntitiesExclude(world: World, entity: Entity): List[Entity] = world.getEntitiesWithinAABBExcludingEntity(entity, toAABB).asInstanceOf[List[Entity]]

  override def toNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setTag("min", min.toNBT)
    nbt.setTag("max", max.toNBT)
    return nbt
  }

  override def toString: String =
  {
    val cont: MathContext = new MathContext(4, RoundingMode.HALF_UP)
    return "Cuboid[" + new BigDecimal(min.x, cont) + ", " + new BigDecimal(min.y, cont) + ", " + new BigDecimal(min.z, cont) + "] -> [" + new BigDecimal(max.x, cont) + ", " + new BigDecimal(max.y, cont) + ", " + new BigDecimal(max.z, cont) + "]"
  }

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[Cuboid]) return (min == (o.asInstanceOf[Cuboid]).min) && (max == (o.asInstanceOf[Cuboid]).max)
    return false
  }

  override def clone: Cuboid = new Cuboid(this)
}