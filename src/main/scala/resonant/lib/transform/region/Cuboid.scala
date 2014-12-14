package resonant.lib.transform.region

import java.math.{BigDecimal, MathContext, RoundingMode}
import java.util.{ArrayList, List}

import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{Vec3, AxisAlignedBB}
import net.minecraft.world.World
import resonant.lib.transform.{ITransform, AbstractOperation}
import resonant.lib.transform.vector.{IVector3, Vector3}

/**
 * A cubical region class to specify a region.
 *
 * @author Calclavia
 */
object Cuboid
{
  def full= new Cuboid(0, 0, 0, 1, 1, 1)
}

class Cuboid(var min: Vector3, var max: Vector3) extends AbstractOperation[Cuboid]
{
  def this() = this(new Vector3, new Vector3)

  def this(amount: Double) = this(new Vector3, new Vector3(amount))

  def this(cuboid: Cuboid) = this(cuboid.min.clone, cuboid.max.clone)

  def this(minx: Double, miny: Double, minz: Double, maxx: Double, maxy: Double, maxz: Double) = this(new Vector3(minx, miny, minz), new Vector3(maxx, maxy, maxz))

  def this(aabb: AxisAlignedBB) = this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ)

  def this(block: Block) = this(block.getBlockBoundsMinX, block.getBlockBoundsMinY, block.getBlockBoundsMinZ, block.getBlockBoundsMaxX, block.getBlockBoundsMaxY, block.getBlockBoundsMaxZ)

  def toAABB: AxisAlignedBB = AxisAlignedBB.getBoundingBox(min.x, min.y, min.z, max.x, max.y, max.z)

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

  def +(vec: Vector3): Cuboid = new Cuboid(min + vec, max + vec)

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

  def *(amount: Double): Cuboid = new Cuboid(min * amount, max * amount)

  def *(amount: Cuboid): Cuboid = new Cuboid(min * amount.min, max * amount.max)

  def transform(transform: ITransform): Cuboid = new Cuboid(min.transform(transform), max.transform(transform))

  def setBounds(block: Block): Cuboid =
  {
    block.setBlockBounds(min.x.asInstanceOf[Float], min.y.asInstanceOf[Float], min.z.asInstanceOf[Float], max.x.asInstanceOf[Float], max.y.asInstanceOf[Float], max.z.asInstanceOf[Float])
    return this
  }

  def intersects(v: Vec3): Boolean =
  {
    return isWithinX(v) && isWithinY(v) && isWithinZ(v)
  }
  
  def intersects(v: IVector3): Boolean =
  {
    return isWithinX(v) && isWithinY(v) && isWithinZ(v)
  } 
  
  def isInsideBounds(x: Double, y: Double, z: Double, i: Double, j: Double, k: Double): Boolean =
  {
    return max.x - 1E-5 > x && i - 1E-5 > min.x && max.y - 1E-5 > y && j - 1E-5 > min.y && max.z - 1E-5 > z && k - 1E-5 > min.z
  }

  def isInsideBounds(other: Cuboid): Boolean =
  {
    return isInsideBounds(other.min.x, other.min.y, other.min.z, other.max.x, other.max.y, other.max.z)
  }

  def isInsideBounds(other: AxisAlignedBB) : Boolean =
  {
    return isInsideBounds(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ)
  }
  
  def isVecInYZ(v : Vec3): Boolean = isWithinY(v) && isWithinZ(v)
  def isVecInYZ(v : IVector3): Boolean = isWithinY(v) && isWithinZ(v)

  def isWithinXZ(v : Vec3): Boolean = isWithinX(v) && isWithinZ(v)
  def isWithinXZ(v : IVector3): Boolean = isWithinX(v) && isWithinZ(v)
  
  def isWithinX(v: Double) = isWithin(min.x, max.x, v)
  def isWithinX(v: Vec3) = isWithin(min.x, max.x, v.xCoord)
  def isWithinX(v: IVector3) = isWithin(min.x, max.x, v.x())

  def isWithinY(v: Double) = isWithin(min.y, max.y, v)
  def isWithinY(v: Vec3) = isWithin(min.x, max.x, v.yCoord)
  def isWithinY(v: IVector3) = isWithin(min.x, max.x, v.y())

  def isWithinZ(v: Double) = isWithin(min.z, max.z, v)
  def isWithinZ(v: Vec3) = isWithin(min.x, max.x, v.zCoord)
  def isWithinZ(v: IVector3) = isWithin(min.x, max.x, v.z())
  
  def isWithin(min: Double, max: Double, v: Double) : Boolean = v - 1E-5 >= min  && v <= max - 1E-5

  /** Checks to see if a line segment is within the defined line. Assume the lines overlap each other.
   * @param min - min point
   * @param max - max point
   * @param a - min line point
   * @param b - max line point
   * @return true if the line segement is within the bounds
   */
  def isWithin(min: Double, max: Double, a: Double , b: Double) : Boolean = a - 1E-5 >= min  && b <= max - 1E-5

  /**
   * Checks if the specified vector is within the XY dimensions of the bounding box. Args: Vec3D
   */
  def isVecInXY(v : Vec3): Boolean =
  {
    return v.xCoord >= this.min.x && v.xCoord <= this.max.x && v.yCoord >= this.min.y && v.yCoord <= this.max.y
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
    foreach(vector => if (center.distance(vector.asInstanceOf[IVector3]) <= radius) vectors.add(vector))
    return vectors
  }

  /** Returns all entities in this region. */
  def getEntities(world: World): List[Entity] = getEntities(world, classOf[Entity])

  def getEntities[E <: Entity](world: World, entityClass: Class[E]): List[E] = world.getEntitiesWithinAABB(entityClass, toAABB).asInstanceOf[List[E]]

  def getEntitiesExclude(world: World, entity: Entity): List[Entity] = world.getEntitiesWithinAABBExcludingEntity(entity, toAABB).asInstanceOf[List[Entity]]

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setTag("min", min.toNBT)
    nbt.setTag("max", max.toNBT)
    return nbt
  }

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    min.writeByteBuf(data)
    max.writeByteBuf(data)
    return data
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