package universalelectricity.core.transform.vector

import java.lang.Double.doubleToLongBits

import com.google.common.io.ByteArrayDataInput
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util._
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.core.transform.rotation.EulerAngle
import universalelectricity.core.transform.{AbstractVector, ITransform}

import scala.collection.convert.wrapAll._
/**
 * @author Calclavia
 */
object Vector3
{
  def getLook(entity: Entity, distance: Double): Vector3 =
  {
    var f1 = 0D
    var f2 = 0D
    var f3 = 0D
    var f4 = 0D

    if (distance == 1.0F)
    {
      f1 = Math.cos(-entity.rotationYaw * 0.017453292F - Math.PI)
      f2 = Math.sin(-entity.rotationYaw * 0.017453292F - Math.PI)
      f3 = -Math.cos(-entity.rotationPitch * 0.017453292F)
      f4 = Math.sin(-entity.rotationPitch * 0.017453292F)
      return new Vector3((f2 * f3), f4, (f1 * f3))
    }
    else
    {
      f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * distance
      f2 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * distance
      f3 = Math.cos(-f2 * 0.017453292F - Math.PI)
      f4 = Math.sin(-f2 * 0.017453292F - Math.PI)
      val f5 = -Math.cos(-f1 * 0.017453292F)
      val f6 = Math.sin(-f1 * 0.017453292F)
      return new Vector3((f4 * f5), f6, (f3 * f5))
    }
  }

  def getLook(yaw: Double, pitch: Double, distance: Double): Vector3 =
  {
    var f1 = 0D
    var f2 = 0D
    var f3 = 0D
    var f4 = 0D

    if (distance == 1.0F)
    {
      f1 = Math.cos(-yaw * 0.017453292F - Math.PI.asInstanceOf[Float])
      f2 = Math.sin(-yaw * 0.017453292F - Math.PI.asInstanceOf[Float])
      f3 = -Math.cos(-pitch * 0.017453292F)
      f4 = Math.sin(-pitch * 0.017453292F)
      return new Vector3((f2 * f3), f4, (f1 * f3))
    }
    else
    {
      f1 = pitch * distance
      f2 = yaw * distance
      f3 = Math.cos(-f2 * 0.017453292F - Math.PI.asInstanceOf[Float])
      f4 = Math.sin(-f2 * 0.017453292F - Math.PI.asInstanceOf[Float])
      val f5 = -Math.cos(-f1 * 0.017453292F)
      val f6 = Math.sin(-f1 * 0.017453292F)
      return new Vector3((f4 * f5), f6, (f3 * f5))
    }
  }

  def up = new Vector3(ForgeDirection.UP)

  def down = new Vector3(ForgeDirection.DOWN)

  def north = new Vector3(ForgeDirection.NORTH)

  def south = new Vector3(ForgeDirection.SOUTH)

  def east = new Vector3(ForgeDirection.EAST)

  def west = new Vector3(ForgeDirection.WEST)
}

class Vector3(var x: Double, var y: Double, var z: Double) extends AbstractVector[Vector3] with Cloneable with IVector3
{
  def this() = this(0, 0, 0)

  def this(amount: Double) = this(amount, amount, amount)

  def this(yaw: Double, pitch: Double) = this(-Math.sin(Math.toRadians(yaw)), Math.sin(Math.toRadians(pitch)), -Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)))

  def this(tile: TileEntity) = this(tile.xCoord, tile.yCoord, tile.zCoord)

  def this(entity: Entity) = this(entity.posX, entity.posY, entity.posZ)

  def this(vec: IVector3) = this(vec.x, vec.y, vec.z)

  def this(vec: Vec3) = this(vec.xCoord, vec.yCoord, vec.zCoord)

  def this(nbt: NBTTagCompound) = this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))

  def this(data: ByteBuf) = this(data.readDouble(), data.readDouble(), data.readDouble())

  def this(dir: ForgeDirection) = this(dir.offsetX, dir.offsetY, dir.offsetZ)

  def this(par1: MovingObjectPosition) = this(par1.blockX, par1.blockY, par1.blockZ)

  def this(par1: ChunkCoordinates) = this(par1.posX, par1.posY, par1.posZ)

  def this(par: Seq[Double]) = this(par(0), par(1), par(2))

  def this(par: (Double, Double, Double)) = this(par._1, par._2, par._3)

  def this(data: ByteArrayDataInput) = this(data.readInt(), data.readInt(), data.readInt())

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

  def toArray = Array(x, y, z)

  def toList = List(x, y, z)

  def toIntList = List(x.toInt, y.toInt, z.toInt)

  def toTuple = (x, y, z)

  def toForgeDirection: ForgeDirection =
  {
    (ForgeDirection.VALID_DIRECTIONS find (dir => x == dir.offsetX && y == dir.offsetY && z == dir.offsetZ)) match
    {
      case Some(entry) => return entry
      case _ => return ForgeDirection.UNKNOWN
    }
  }

  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("x", x)
    nbt.setDouble("y", y)
    nbt.setDouble("z", z)
    return nbt
  }

  def toIntNBT: NBTTagCompound = writeIntNBT(new NBTTagCompound)

  def writeIntNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setInteger("x", x.toInt)
    nbt.setInteger("y", y.toInt)
    nbt.setInteger("z", z.toInt)
    return nbt
  }

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    data.writeDouble(x)
    data.writeDouble(y)
    data.writeDouble(z)
    return data
  }

  def toEulerAngle = new EulerAngle(Math.toDegrees(Math.atan2(x, z)), Math.toDegrees(-Math.atan2(y, Math.hypot(z, x))))

  def toEulerAngle(target: Vector3): EulerAngle = (clone - target).toEulerAngle

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

  def +(x: Double, y: Double, z: Double): Vector3 = new Vector3(this.x + x, this.y + y, this.z + z)

  def +=(x: Double, y: Double, z: Double): Vector3 = set(new Vector3(this.x + x, this.y + y, this.z + z))

  def add(x: Double, y: Double, z: Double): Vector3 = this +(x, y, z)

  def addEquals(x: Double, y: Double, z: Double): Vector3 = this +=(x, y, z)

  def +(amount: ForgeDirection): Vector3 = this + new Vector3(amount)

  def +=(amount: ForgeDirection): Vector3 = set(this + new Vector3(amount))

  def add(amount: ForgeDirection): Vector3 = this + amount

  def addEquals(amount: ForgeDirection): Vector3 = this += amount

  override def *(amount: Double): Vector3 = new Vector3(x * amount, y * amount, z * amount)

  override def *(amount: Vector3): Vector3 = new Vector3(x * amount.x, y * amount.y, z * amount.z)

  override def $(other: Vector3) = x * other.x + y * other.y + z * other.z

  def cross(other: Vector3) = %(other)

  def %(other: Vector3): Vector3 = new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)

  def xCross = new Vector3(0.0D, this.z, -this.y)

  def zCross = new Vector3(-this.y, this.x, 0.0D)

  /** Point between this point and another */
  def midPoint(pos: Vector3) : Vector3 =
  {
    return new Vector3((x + pos.x) / 2, (y + pos.y) / 2, (z + pos.z) / 2);
  }

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

  def transform(transformer: ITransform): Vector3 = transformer.transform(this)

  /**
   * Gets the angle between this vector and another vector.
   * @return Angle in radians
   */
  def angle(other: Vector3) = Math.acos((this $ other) / (magnitude * other.magnitude))

  def anglePreNorm(other: Vector3) = Math.acos(this $ other)

  def getAround(world: World, side: ForgeDirection, range: Int) :  java.util.List[Vector3] =
  {
    val list: java.util.List[Vector3] = new java.util.ArrayList[Vector3]()

    val dx : Int = side match{
      case ForgeDirection.EAST => 0
      case ForgeDirection.WEST => 0
      case default => range
    }
    val dy : Int = side match{
      case ForgeDirection.DOWN => 0
      case ForgeDirection.UP => 0
      case default => range
    }
    val dz : Int = side match{
    case ForgeDirection.NORTH => 0
    case ForgeDirection.SOUTH => 0
    case default => range
    }

    for (x <- (xi - dx) to (xi + dx))
    {
      for (y <- (yi - dy) to (yi + dx))
      {
        for (z <- (zi - dz) to (zi + dz))
        {
            list.add(new Vector3(x, y, z))
        }
      }
    }
    return list
  }

  def rayTrace(world: World, dir: Vector3, dist: Double): MovingObjectPosition = rayTrace(world, this + (dir * dist))

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

    val checkEntities = world.getEntitiesWithinAABB(classOf[Entity], scanRegion) map (_.asInstanceOf[Entity])

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
  def getBlock(world: IBlockAccess): Block = if(world != null) world.getBlock(xi, yi, zi) else null

  def getBlockMetadata(world: IBlockAccess) = if(world != null) world.getBlockMetadata(xi, yi, zi) else 0

  def getTileEntity(world: IBlockAccess) =  if(world != null) world.getTileEntity(xi, yi, zi) else null

  def setBlock(world: World, block: Block, metadata: Int, notify: Int): Boolean = if(world != null && block != null) world.setBlock(xi, yi, zi, block, metadata, notify) else false

  def setBlock(world: World, block: Block, metadata: Int): Boolean = setBlock(world, block, metadata, 3)

  def setBlock(world: World, block: Block): Boolean = setBlock(world, block, 0)

  def setBlockToAir(world : World) : Boolean = world.setBlockToAir(xi, yi, zi)

  override def hashCode: Int =
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
    if (o.isInstanceOf[IVector3])
    {
      val other = o.asInstanceOf[IVector3]
      return other.x == x && other.y == y && other.z == z
    }

    return false
  }

  override def clone: Vector3 = new Vector3(x, y, z)

  override def toString = "Vector3[" + x + "," + y + "," + z + "]"
}