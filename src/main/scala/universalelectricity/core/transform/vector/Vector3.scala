package universalelectricity.core.transform.vector

import java.lang.Double.doubleToLongBits

import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util._
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection
import universalelectricity.core.transform.rotation.Rotation
import universalelectricity.core.transform.{AbstractVector, ITransform}

import scala.collection.convert.wrapAll._
import Array._
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

  def apply(transformer: ITransform): Vector3 = transformer.transform(this)

  def rotate(angle: Double, axis: Vector3): Vector3 = apply(axis.getRotationMatrix(angle))

  /**
   * Creates a rotation matrix for an angle-axis rotation around this vector.
   * @param rotateAngle - The angle of rotation in degrees
   * @return - A 3x3 rotation matrix
   */
  def getRotationMatrix(rotateAngle: Double): Array[Array[Double]] =
  {
    val angle = Math.toRadians(rotateAngle)
    val axis = this.normalize
    val x = axis.x
    val y = axis.y
    val z = axis.z

    /**
     * Predefine trigonometric calculation to save computation time.
     */
    val cos = Math.cos(angle)
    val oneMinusCos = 1 - cos
    val sin = Math.sin(angle)

    /**
     * Creates a 3x3 matrix using Rodrigues' rotation formula.
     */
    val matrix = ofDim[Double](3, 3)
    matrix(0)(0) = x * x * oneMinusCos + cos
    matrix(0)(1) = y * x * oneMinusCos + z * sin
    matrix(0)(2) = x * z * oneMinusCos - y * sin
    matrix(1)(0) = x * y * oneMinusCos - z * sin
    matrix(1)(1) = y * y * oneMinusCos + cos
    matrix(1)(2) = y * z * oneMinusCos + x * sin
    matrix(2)(0) = x * z * oneMinusCos + y * sin
    matrix(2)(1) = y * z * oneMinusCos - x * sin
    matrix(2)(2) = z * z * oneMinusCos + cos
    return matrix
  }

  /**
   * Multiplies this column vector with a given matrix
   * @param matrix - A 3x3 transformation matrix
   * @return The vector multiplied with the matrix.
   */
  def apply(matrix: Array[Array[Double]]): Vector3 =
  {
    val newX = x * matrix(0)(0) + y * matrix(0)(1) + z * matrix(0)(2)
    val newY = x * matrix(1)(0) + y * matrix(1)(1) + z * matrix(1)(2)
    val newZ = x * matrix(2)(0) + y * matrix(2)(1) + z * matrix(2)(2)
    return new Vector3(newX, newY, newZ)
  }

  /**
   * Multiplies two matricies. This is non-commutative.
   */
  def matrixMultiply(m1: Seq[Array[Double]], m2: Array[Array[Double]]): Array[Array[Double]] =
  {
    val res = Array.fill(m1.length, m2(0).length)(0.0)

    for (row <- (0 until m1.length).par; col <- (0 until m2(0).length).par; i <- 0 until m1(0).length)
    {
      res(row)(col) += m1(row)(i) * m2(i)(col)
    }

    return res
  }

  /**
   * Gets the angle between this vector and another vector.
   * @return Angle in radians
   */
  def angle(other: Vector3) = Math.acos((this $ other) / (magnitude * other.magnitude))

  def anglePreNorm(other: Vector3) = Math.acos(this $ other)

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
  def getBlock(world: IBlockAccess): Block = world.getBlock(xi, yi, zi)

  def getBlockMetadata(world: IBlockAccess) = world.getBlockMetadata(xi, yi, zi)

  def getTileEntity(world: IBlockAccess) = world.getTileEntity(xi, yi, zi)

  def setBlock(world: World, block: Block, metadata: Int, notify: Int): Boolean = world.setBlock(xi, yi, zi, block, metadata, notify)

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