package com.builtbroken.mc.lib.transform.vector

import java.lang.Double.doubleToLongBits
import com.builtbroken.jlib.data.vector.IPos3D
import com.google.common.io.ByteArrayDataInput
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util._
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection
import com.builtbroken.mc.lib.transform.ITransform
import com.builtbroken.mc.lib.transform.rotation.EulerAngle

import scala.collection.convert.wrapAll._

/**
 * @author Calclavia
 */
object Pos
{
  def getLook(entity: Entity, distance: Double): Pos =
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
      return new Pos((f2 * f3), f4, (f1 * f3))
    }
    else
    {
      f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * distance
      f2 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * distance
      f3 = Math.cos(-f2 * 0.017453292F - Math.PI)
      f4 = Math.sin(-f2 * 0.017453292F - Math.PI)
      val f5 = -Math.cos(-f1 * 0.017453292F)
      val f6 = Math.sin(-f1 * 0.017453292F)
      return new Pos((f4 * f5), f6, (f3 * f5))
    }
  }

  def getLook(yaw: Double, pitch: Double, distance: Double): Pos =
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
      return new Pos((f2 * f3), f4, (f1 * f3))
    }
    else
    {
      f1 = pitch * distance
      f2 = yaw * distance
      f3 = Math.cos(-f2 * 0.017453292F - Math.PI.asInstanceOf[Float])
      f4 = Math.sin(-f2 * 0.017453292F - Math.PI.asInstanceOf[Float])
      val f5 = -Math.cos(-f1 * 0.017453292F)
      val f6 = Math.sin(-f1 * 0.017453292F)
      return new Pos((f4 * f5), f6, (f3 * f5))
    }
  }

  def zero = new Pos()

  def up = new Pos(ForgeDirection.UP)

  def down = new Pos(ForgeDirection.DOWN)

  def north = new Pos(ForgeDirection.NORTH)

  def south = new Pos(ForgeDirection.SOUTH)

  def east = new Pos(ForgeDirection.EAST)

  def west = new Pos(ForgeDirection.WEST)
}

class Pos(var x: Double = 0, var y: Double = 0, var z: Double = 0) extends IPos3D with Ordered[Pos] with Cloneable
{
  def this() = this(0, 0, 0)

  def this(amount: Double) = this(amount, amount, amount)

  def this(yaw: Double, pitch: Double) = this(-Math.sin(Math.toRadians(yaw)), Math.sin(Math.toRadians(pitch)), -Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)))

  def this(tile: TileEntity) = this(tile.xCoord, tile.yCoord, tile.zCoord)

  def this(entity: Entity) = this(entity.posX, entity.posY, entity.posZ)

  def this(vec: IPos3D) = this(vec.x, vec.y, vec.z)

  def this(nbt: NBTTagCompound) = this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))

  def this(data: ByteBuf) = this(data.readDouble(), data.readDouble(), data.readDouble())

  def this(par1: MovingObjectPosition) = this(par1.blockX, par1.blockY, par1.blockZ)

  def this(par1: ChunkCoordinates) = this(par1.posX, par1.posY, par1.posZ)

  def this(par: Seq[Double]) = this(par(0), par(1), par(2))

  def this(par: (Double, Double, Double)) = this(par._1, par._2, par._3)

  def this(data: ByteArrayDataInput) = this(data.readInt(), data.readInt(), data.readInt())

  def this(dir: ForgeDirection) = this(dir.offsetX, dir.offsetY, dir.offsetZ)

  def this(dir: EnumFacing) = this(dir.getFrontOffsetX, dir.getFrontOffsetY, dir.getFrontOffsetZ)

  def this(vec: Vec3) = this(vec.xCoord, vec.yCoord, vec.zCoord)

  //=========================
  //========Setters==========
  //=========================

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


  def set(x: Double, y: Double, z: Double): Pos =
  {
    this.x = x
    this.y = y
    this.z = z
    return this
  }

  def set(n: Double): Pos = set(n, n, n)

  def set(vec: IPos3D): Pos = set(vec.x, vec.y, vec.z)

  //=========================
  //========Accessors========
  //=========================

  def xf: Float = x.toFloat

  def yf: Float = y.toFloat

  def zf: Float = z.toFloat

  def xi = x.toInt

  def yi = y.toInt

  def zi = z.toInt

  //=========================
  //========Converters=======
  //=========================

  def toVec3 = Vec3.createVectorHelper(x, y, z)

  def toVector2: Point = new Point(x, z)

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

  def toEulerAngle(target: Pos): EulerAngle = (clone - target).toEulerAngle

  def toEulerAngle = new EulerAngle(Math.toDegrees(Math.atan2(x, z)), Math.toDegrees(-Math.atan2(y, Math.hypot(z, x))))

  def toIntNBT: NBTTagCompound = writeIntNBT(new NBTTagCompound)

  def unary_+ : Pos = this

  def unary_- : Pos = this * -1

  //=========================
  //========NBT==============
  //=========================

  final def toNBT: NBTTagCompound = writeNBT(new NBTTagCompound())

  def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("x", x)
    nbt.setDouble("y", y)
    nbt.setDouble("z", z)
    return nbt
  }


  def writeIntNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setInteger("x", x.toInt)
    nbt.setInteger("y", y.toInt)
    nbt.setInteger("z", z.toInt)
    return nbt
  }

  def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    data.writeDouble(x)
    data.writeDouble(y)
    data.writeDouble(z)
    return data
  }


  //=========================
  //========Operators========
  //=========================

  def magnitudeSquared: Double = this $ this;

  def magnitude = Math.sqrt(magnitudeSquared)

  def normalize = this / magnitude

  def round: Pos = new Pos(Math.round(x), Math.round(y), Math.round(z))

  def ceil: Pos = new Pos(Math.ceil(x), Math.ceil(y), Math.ceil(z))

  def floor: Pos = new Pos(Math.floor(x), Math.floor(y), Math.floor(z))

  def max(other: IPos3D): Pos = new Pos(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z))

  def min(other: IPos3D): Pos = new Pos(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z))

  def reciprocal: Pos = new Pos(1 / x, 1 / y, 1 / z)

  //=========================
  //==Double Handling========
  //=========================

  def sub(amount: Double): Pos = this - amount

  def subtract(amount: Double): Pos = this - amount

  def -(amount: Double): Pos = new Pos(x - amount, y - amount, z - amount)

  def subEquals(amount: Double): Pos = this - amount

  def subtractEquals(amount: Double): Pos = this - amount

  def -=(amount: Double): Pos =
  {
    x -= amount
    y -= amount
    z -= amount
    return this
  }

  def sub(x: Double, y: Double, z: Double): Pos = new Pos(this.x - x, this.y - y, this.z - z)

  def subtract(x: Double, y: Double, z: Double): Pos = new Pos(this.x - x, this.y - y, this.z - z)

  def -(x: Double, y: Double, z: Double): Pos = new Pos(this.x - x, this.y - y, this.z - z)

  def subEquals(x: Double, y: Double, z: Double): Pos = this -=(x, y, z)

  def subtractEquals(x: Double, y: Double, z: Double): Pos = this -=(x, y, z)

  def -=(x: Double, y: Double, z: Double): Pos = set(new Pos(this.x - x, this.y - y, this.z - z))

  def add(amount: Double): Pos = this + amount

  def +(amount: Double): Pos = new Pos(x + amount, y + amount, z + amount)

  def addEquals(amount: Double): Pos = this + amount

  def +=(amount: Double): Pos =
  {
    x += amount
    y += amount
    z += amount
    return this
  }

  def add(ax: Double, ay: Double, az: Double): Pos = this +(ax, ay, az)

  def +(ax: Double, ay: Double, az: Double): Pos = new Pos(this.x + ax, this.y + ay, this.z + az)

  def addEquals(x: Double, y: Double, z: Double): Pos = this +=(x, y, z)

  def +=(x: Double, y: Double, z: Double): Pos = set(new Pos(this.x + x, this.y + y, this.z + z))

  def multi(amount: Double): Pos = this * amount

  def multiply(amount: Double): Pos = this * amount

  def *(amount: Double): Pos = new Pos(x * amount, y * amount, z * amount)

  def divide(amount: Double): Pos = this / amount

  def /(amount: Double): Pos = new Pos(x / amount, y / amount, z / amount)

  def multiplyEquals(amount: Double): Pos = this *= amount

  def *=(amount: Double): Pos =
  {
    this.x *= amount
    this.y *= amount
    this.z *= amount
    return this
  }

  def divideEquals(amount: Double): Pos = this /= amount

  def /=(amount: Double): Pos =
  {
    this.x /= amount
    this.y /= amount
    this.z /= amount
    return this
  }

  //====================
  // Vec3 handling
  //====================

  def subtract(amount: Vec3): Pos = this - amount

  def -(amount: Vec3): Pos = new Pos(x - amount.xCoord, y - amount.yCoord, z - amount.zCoord)

  def distance(other: Vec3): Double = (this - other).magnitude

  def subEquals(amount: Vec3): Pos = this -= amount

  def subtractEquals(amount: Vec3): Pos = this -= amount

  def -=(amount: Vec3): Pos =
  {
    x -= amount.xCoord
    y -= amount.yCoord
    z -= amount.zCoord
    return this
  }

  def add(amount: Vec3): Pos = this + amount

  def +(amount: Vec3): Pos = new Pos(x + amount.xCoord, y + amount.yCoord, z + amount.zCoord)

  def addEquals(amount: Vec3): Pos = this += amount

  def +=(amount: Vec3): Pos =
  {
    x = amount.xCoord + x
    y = amount.yCoord + y
    z = amount.zCoord + z
    return this
  }

  def multiply(amount: Vec3): Pos = this * amount

  def *(amount: Vec3): Pos = new Pos(x * amount.xCoord, y * amount.yCoord, z * amount.zCoord)

  def multiplyEquals(amount: Vec3): Pos = this *= amount

  def *=(amount: Vec3): Pos =
  {
    x *= amount.xCoord
    y *= amount.yCoord
    z *= amount.zCoord
    return this;
  }

  def divide(amount: Vec3): Pos = this * amount

  def /(amount: Vec3): Pos = new Pos(x / amount.xCoord, y / amount.yCoord, z / amount.zCoord)

  def divideEquals(amount: Vec3): Pos = this /= amount

  def /=(amount: Vec3): Pos =
  {
    x /= amount.xCoord
    y /= amount.yCoord
    z /= amount.zCoord
    return this;
  }

  def midPoint(pos: Vec3): Pos = new Pos((x + pos.xCoord) / 2, (y + pos.yCoord) / 2, (z + pos.zCoord) / 2)

  def dot(other: Vec3) = $(other)

  def $(other: Vec3) = x * other.xCoord + y * other.yCoord + z * other.zCoord

  def cross(other: Vec3) = %(other)

  def %(other: Vec3): Pos = new Pos(y * other.zCoord - z * other.yCoord, z * other.xCoord - x * other.zCoord, x * other.yCoord - y * other.xCoord)



  //====================
  // IVector3 handling
  //====================

  def subtract(amount: IPos3D): Pos = this - amount

  def -(amount: IPos3D): Pos = new Pos(x - amount.x, y - amount.y, z - amount.z)

  def distance(other: IPos3D): Double = (this - other).magnitude

  def subEquals(amount: IPos3D): Pos = this -= amount

  def subtractEquals(amount: IPos3D): Pos = this -= amount

  def -=(amount: IPos3D): Pos =
  {
    x -= amount.x
    y -= amount.y
    z -= amount.z
    return this
  }

  def add(amount: IPos3D): Pos = this + amount

  def +(amount: IPos3D): Pos = new Pos(x + amount.x, y + amount.y, z + amount.z)

  def addEquals(amount: IPos3D): Pos = this += amount

  def +=(amount: IPos3D): Pos =
  {
    x = amount.x + x
    y = amount.y + y
    z = amount.z + z
    return this
  }

  def multiply(amount: IPos3D): Pos = this * amount

  def *(amount: IPos3D): Pos = new Pos(x * amount.x, y * amount.y, z * amount.z)

  def multiplyEquals(amount: IPos3D): Pos = this *= amount

  def *=(amount: IPos3D): Pos =
  {
    x *= amount.x
    y *= amount.y
    z *= amount.z
    return this;
  }

  def divide(amount: IPos3D): Pos = this * amount

  def /(amount: IPos3D): Pos = new Pos(x / amount.x, y / amount.y, z / amount.z)

  def divideEquals(amount: IPos3D): Pos = this /= amount

  def /=(amount: IPos3D): Pos =
  {
    x /= amount.x
    y /= amount.y
    z /= amount.z
    return this;
  }

  /** Point between this point and another */
  @Deprecated
  def midpoint(pos: IPos3D): Pos = midPoint(pos)

  def midPoint(pos: IPos3D): Pos = new Pos((x + pos.x) / 2, (y + pos.y) / 2, (z + pos.z) / 2)

  def dot(other: IPos3D) = $(other)

  def $(other: IPos3D) = x * other.x + y * other.y + z * other.z

  def cross(other: IPos3D) = %(other)

  def %(other: IPos3D): Pos = new Pos(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)


  //=========================
  //ForgeDirection handling
  //=========================
  def add(amount: ForgeDirection): Pos = this + amount

  def +(amount: ForgeDirection): Pos = this + new Pos(amount)

  def addEquals(amount: ForgeDirection): Pos = this += amount

  def +=(amount: ForgeDirection): Pos = set(this + new Pos(amount))


  def subtract(amount: ForgeDirection): Pos = this - amount

  def -(amount: ForgeDirection): Pos = this - new Pos(amount)

  def subEquals(amount: ForgeDirection): Pos = this -= amount

  def -=(amount: ForgeDirection): Pos = set(this - new Pos(amount))

  //=========================
  //EnumFacing handling
  //=========================
  def add(amount: EnumFacing): Pos = this + amount

  def +(amount: EnumFacing): Pos = this + new Pos(amount)

  def addEquals(amount: EnumFacing): Pos = this += amount

  def +=(amount: EnumFacing): Pos = set(this + new Pos(amount))


  def subtract(amount: EnumFacing): Pos = this - amount

  def -(amount: EnumFacing): Pos = this - new Pos(amount)

  def subEquals(amount: EnumFacing): Pos = this -= amount

  def -=(amount: EnumFacing): Pos = set(this - new Pos(amount))


  /** @return The perpendicular vector to the axis. */
  def perpendicular: Pos =
  {
    if (this.z == 0.0F)
    {
      return this.zCross
    }
    return this.xCross
  }

  def xCross = new Pos(0.0D, this.z, -this.y)

  def zCross = new Pos(-this.y, this.x, 0.0D)

  def isZero = x == 0 && y == 0 && z == 0

  def transform(transformer: ITransform): Pos = transformer.transform(this)

  /**
   * Gets the angle between this vector and another vector.
   * @return Angle in radians
   */
  def angle(other: IPos3D) = Math.acos((this $ other) / (magnitude * new Pos(other).magnitude))

  def anglePreNorm(other: IPos3D) = Math.acos(this $ other)

  def getAround(world: World, side: ForgeDirection, range: Int): java.util.List[Pos] =
  {
    val list: java.util.List[Pos] = new java.util.ArrayList[Pos]()

    val dx: Int = side match
    {
      case ForgeDirection.EAST => 0
      case ForgeDirection.WEST => 0
      case default => range
    }
    val dy: Int = side match
    {
      case ForgeDirection.DOWN => 0
      case ForgeDirection.UP => 0
      case default => range
    }
    val dz: Int = side match
    {
      case ForgeDirection.NORTH => 0
      case ForgeDirection.SOUTH => 0
      case default => range
    }

    for (x <- (xi - dx) to (xi + dx))
    {
      for (y <- (yi - dy) to (yi + dy))
      {
        for (z <- (zi - dz) to (zi + dz))
        {
          list.add(new Pos(x, y, z))
        }
      }
    }
    return list
  }

  def rayTrace(world: World, dir: Pos, dist: Double): MovingObjectPosition = rayTrace(world, this + (dir * dist))


  def rayTrace(world: World, end: Pos): MovingObjectPosition =
  {
    val block = rayTraceBlocks(world, end)
    val entity = rayTraceEntities(world, end)

    if (block == null)
      return entity
    if (entity == null)
      return block

    if (distance(new Pos(block.hitVec)) < distance(new Pos(entity.hitVec)))
      return block

    return entity
  }


  def rayTraceBlocks(world: World, end: Pos): MovingObjectPosition = world.rayTraceBlocks(toVec3, end.toVec3)

  def rayTraceEntities(world: World, end: Pos): MovingObjectPosition =
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
              val dist = distance(new Pos(hit.hitVec))

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


  //===================
  //===World Setters===
  //===================
  def setBlock(world: World, block: Block): Boolean = setBlock(world, block, 0)

  def setBlock(world: World, block: Block, metadata: Int): Boolean = setBlock(world, block, metadata, 3)

  def setBlock(world: World, block: Block, metadata: Int, notify: Int): Boolean = if (world != null && block != null) world.setBlock(xi, yi, zi, block, metadata, notify) else false

  def setBlockToAir(world: World): Boolean = world.setBlockToAir(xi, yi, zi)

  //===================
  //==World Accessors==
  //===================
  def isAirBlock(world: World): Boolean = world.isAirBlock(xi, yi, zi)

  def isBlockFreezable(world: World): Boolean = world.isBlockFreezable(xi, yi, zi)

  def isBlockEqual(world: World, block: Block): Boolean =
  {
    val b = getBlock(world)
    if (b != null && b == block)
    {
      return true;
    }
    return false
  }

  def getBlock(world: IBlockAccess): Block = if (world != null) world.getBlock(xi, yi, zi) else null

  def getBlockMetadata(world: IBlockAccess) = if (world != null) world.getBlockMetadata(xi, yi, zi) else 0

  def getTileEntity(world: IBlockAccess) = if (world != null) world.getTileEntity(xi, yi, zi) else null

  def getHardness(world: World): java.lang.Float =
  {
    val block = getBlock(world)
    if (block != null)
      return block.getBlockHardness(world, xi, yi, zi)
    else
      return 0
  }

  /**
   * Gets the resistance of a block using block.getResistance method
   * @param cause - entity that triggered/is the explosion
   */
  def getResistance(cause: Entity): java.lang.Float =
  {
    return getResistance(cause.worldObj, cause, x, y, z)
  }

  /**
   * Gets the resistance of a block using block.getResistance method
   * @param cause - entity that triggered/is the explosion
   */
  def getResistanceToEntity(cause: Entity): java.lang.Float =
  {
    return getBlock(cause.worldObj).getExplosionResistance(cause)
  }

  /**
   * Gets the resistance of a block using block.getResistance method
   * @param cause - entity that triggered/is the explosion
   */
  def getResistanceToEntity(world: World, cause: Entity): java.lang.Float =
  {
    return getBlock(world).getExplosionResistance(cause)
  }

  /**
   * Gets the resistance of a block using block.getResistance method
   * @param world - world to check in
   * @param cause - entity that triggered/is the explosion
   */
  def getResistance(world: World, cause: Entity): Float =
  {
    return getResistance(world, cause, cause.posX, cause.posY, cause.posZ)
  }

  /**
   * Gets the resistance of a block using block.getResistance method
   * @param world - world to check in
   * @param cause - entity that triggered/is the explosion
   * @param xx - xPos location of the explosion
   * @param yy - xPos location of the explosion
   * @param zz - xPos location of the explosion
   */
  def getResistance(world: World, cause: Entity, xx: Double, yy: Double, zz: Double): Float =
  {
    return getBlock(world).getExplosionResistance(cause, world, xi, yi, zi, xx, yy, zz)
  }

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
    if (o.isInstanceOf[IPos3D])
    {
      val other = o.asInstanceOf[IPos3D]
      return other.x == x && other.y == y && other.z == z
    }

    return false
  }

  override def compare(that: Pos): Int =
  {
    if (x < that.x || y < that.y || z < that.z)
      return -1

    if (x > that.x || y > that.y || z > that.z)
      return 1

    return 0
  }

  override def clone: Pos = new Pos(x, y, z)

  override def toString = "Vector3[" + x + "," + y + "," + z + "]"
}