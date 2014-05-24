package universalelectricity.core.vector

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.util.{AxisAlignedBB, ChunkCoordinates, MovingObjectPosition, Vec3}
import net.minecraft.nbt.NBTTagCompound
import java.lang.Double.doubleToLongBits
import net.minecraft.entity.Entity
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.block.Block

/**
 * @author Calclavia
 */
class Vector3(var x: Double, var y: Double, var z: Double) extends Cloneable with TraitVector[Vector3]
{
  def this() = this(0, 0, 0)

  def this(amount: Double) = this(amount, amount, amount)

  def this(yaw: Double, pitch: Double) = this(-Math.sin(Math.toRadians(yaw)), Math.sin(Math.toRadians(pitch)), -Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)))

  def this(tile: TileEntity) = this(tile.xCoord, tile.yCoord, tile.zCoord)

  def this(entity: Entity) = this(entity.posX, entity.posY, entity.posZ)

  def this(vec: Vec3) = this(vec.xCoord, vec.yCoord, vec.zCoord)

  def this(nbt: NBTTagCompound) = this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))

  def this(dir: ForgeDirection) = this(dir.offsetX, dir.offsetY, dir.offsetZ)

  def this(par1: MovingObjectPosition) = this(par1.blockX, par1.blockY, par1.blockZ)

  def this(par1: ChunkCoordinates) = this(par1.posX, par1.posY, par1.posZ)

  def x(amount: Double)
  {
    x = amount
  }

  def y(amount: Double)
  {
    y = amount
  }

  def z(amount: Double)
  {
    z = amount
  }

  override def set(vec: Vector3): Vector3 =
  {
    x = vec.x
    y = vec.y
    z = vec.z
    return this
  }


  /**
   * Conversion
   */
  def toVec3 = Vec3.createVectorHelper(x, y, z)

  def toVector2: Vector2 = new Vector2(x, z)

  def toForgeDirection: ForgeDirection =
  {
    for (direction <- ForgeDirection.VALID_DIRECTIONS)
    {
      if (this.x == direction.offsetX && this.y == direction.offsetY && this.z == direction.offsetZ)
      {
        return direction
      }
    }
    return ForgeDirection.UNKNOWN
  }

  override def toNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("x", x)
    nbt.setDouble("y", y)
    nbt.setDouble("z", z)
    return nbt
  }

  def toRotation = new Rotation(Math.toDegrees(Math.atan2(x, z)), Math.toDegrees(-Math.atan2(y, Math.hypot(z, x))), 0)

  def toRotation(target: Vector3): Rotation = (clone - target).toRotation

  def xi = x.toInt

  def yi = y.toInt

  def zi = z.toInt

  def xf = x.toFloat

  def yf = y.toFloat

  def zf = z.toFloat

  override def round: Vector3 = new Vector3(Math.round(x), Math.round(y), Math.round(z))

  override def ceil: Vector3 = new Vector3(Math.ceil(x), Math.ceil(y), Math.ceil(z))

  override def floor: Vector3 = new Vector3(Math.floor(x), Math.floor(y), Math.floor(z))

  def max(other: Vector3): Vector3 = new Vector3(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z))

  def min(other: Vector3): Vector3 = new Vector3(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z))

  override def reciprocal: Vector3 = new Vector3(1 / x, 1 / y, 1 / z)

  /**
   * Operations
   */
  override def +(amount: Double): Vector3 = new Vector3(x + amount, y + amount, z + amount)

  override def +(amount: Vector3): Vector3 = new Vector3(x + amount.x, y + amount.y, z + amount.z)

  def +(amount: ForgeDirection): Vector3 = this + new Vector3(amount)

  def +=(amount: ForgeDirection): Vector3 = set(this + new Vector3(amount))

  def add(amount: ForgeDirection): Vector3 = this + amount

  def addSet(amount: ForgeDirection): Vector3 = this += amount

  override def *(amount: Double): Vector3 = new Vector3(x * amount, y * amount, z * amount)

  override def *(amount: Vector3): Vector3 = new Vector3(x * amount.x, y * amount.y, z * amount.z)

  override def $(other: Vector3) = x * other.x + y * other.y + z * other.z

  def cross(other: Vector3) = %(other)

  def %(other: Vector3): Vector3 = new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)

  def xCross = new Vector3(0.0D, this.z, -this.y)

  def zCross = new Vector3(-this.y, this.x, 0.0D)

  /** @return The perpendicular vector to the axis. */
  def perpendicular: Vector3 =
  {
    if (this.z == 0.0F)
    {
      return this.zCross
    }
    return this.xCross
  }

  def isZero = x == 0 && y == 0 && z == 0

  def apply(transformer: ITransform): Vector3 =
  {
    transformer.transform(this)
    return this
  }

  /**
   * Gets the angle between this vector and another vector.
   * @return Angle in radians
   */
  def angle(other: Vector3) = Math.acos((this $ other) / (magnitude * other.magnitude))

  def rayTrace(world: World, end: Vector3): MovingObjectPosition =
  {
    val block = rayTraceBlocks(world, end)
    val entity = rayTraceEntities(world, end)

    if (block == null)
      return entity
    if (entity == null)
      return block

    if (distance(new Vector3(block.hitVec)) < distance(new Vector3(entity.hitVec)))
      return block

    return entity
  }

  def rayTraceBlocks(world: World, end: Vector3): MovingObjectPosition = world.rayTraceBlocks(toVec3, end.toVec3)

  def rayTraceEntities(world: World, end: Vector3): MovingObjectPosition =
  {
    var closestEntityMOP: MovingObjectPosition = null
    var closetDistance = 0D

    val checkDistance = distance(end)
    val scanRegion = AxisAlignedBB.getBoundingBox(-checkDistance, -checkDistance, -checkDistance, checkDistance, checkDistance, checkDistance).offset(x, y, z)

    val checkEntities = world.getEntitiesWithinAABB(classOf[Entity], scanRegion).asInstanceOf[List[Entity]]

    checkEntities.foreach(
      entity =>
      {
        if (entity != null && entity.canBeCollidedWith && entity.boundingBox != null)
        {
          val border = entity.getCollisionBorderSize
          val bounds = entity.boundingBox.expand(border, border, border)
          val hit = bounds.calculateIntercept(toVec3, end.toVec3)

          if (hit != null)
          {

            if (bounds.isVecInside(toVec3))
            {
              if (0 < closetDistance || closetDistance == 0)
              {
                closestEntityMOP = new MovingObjectPosition(entity)

                closestEntityMOP.hitVec = hit.hitVec
                closetDistance = 0
              }
            }
            else
            {
              val dist = distance(new Vector3(hit.hitVec))

              if (dist < closetDistance || closetDistance == 0)
              {
                closestEntityMOP = new MovingObjectPosition(entity)
                closestEntityMOP.hitVec = hit.hitVec

                closetDistance = dist
              }
            }
          }
        }
      }
    )

    return closestEntityMOP
  }

  /**
   * World Access
   */
  def getBlockID(world: IBlockAccess): Block = world.getBlock(xi, yi, zi)

  def getBlockMetadata(world: IBlockAccess) = world.getBlockMetadata(xi, yi, zi)

  def getTileEntity(world: IBlockAccess) = world.getTileEntity(xi, yi, zi)

  def setBlock(world: World, block: Block, metadata: Int, notify: Int): Boolean = world.setBlock(xi, yi, zi, block, metadata, notify)

  def setBlock(world: World, block: Block, metadata: Int): Boolean = setBlock(world, block, metadata, 3)

  def setBlock(world: World, block: Block): Boolean = setBlock(world, block, 0)

  override def hashCode(): Int =
  {
    val x = doubleToLongBits(this.x)
    val y = doubleToLongBits(this.y)
    val z = doubleToLongBits(this.z)
    var hash = (x ^ (x >>> 32))
    hash = 31 * hash + y ^ (y >>> 32)
    hash = 31 * hash + z ^ (z >>> 32)
    return hash.toInt;
  }

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[Vector3])
    {
      val other = o.asInstanceOf[Vector3]
      return other.x == x && other.y == y && other.z == z
    }

    return false
  }

  override def clone: Vector3 = new Vector3(x, y, z)

  override def toString = "Vector3[" + x + "," + y + "," + z + "]"

}