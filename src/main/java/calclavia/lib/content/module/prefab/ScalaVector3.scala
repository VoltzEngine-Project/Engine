package universalelectricity.api.vector

import java.util.List
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.ChunkCoordinates
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.ForgeDirection

/**
 * A Vector class used for defining objects in a 3D space.
 *
 * @author Calclavia
 */
class ScalaVector3
{
  def fromCenter(e: Entity): Vector3 =
  {
    return new Vector3(e.posX, e.posY - e.yOffset + e.height / 2, e.posZ)
  }

  def fromCenter(e: TileEntity): Vector3 =
  {
    return new Vector3(e.xCoord + 0.5, e.yCoord + 0.5, e.zCoord + 0.5)
  }

  /** Gets the distance between two vectors */
  def distance(vec1: Vector3, vec2: Vector3): Double =
  {
    return vec1.distance(vec2)
  }

  def translate(first: Vector3, second: Vector3): Vector3 =
  {
    return first.clone.translate(second)
  }

  def translate(translate: Vector3, addition: Double): Vector3 =
  {
    return translate.clone.translate(addition)
  }

  def scale(vec: Vector3, amount: Double): Vector3 =
  {
    return vec.scale(amount)
  }

  def scale(vec: Vector3, amount: Vector3): Vector3 =
  {
    return vec.scale(amount)
  }

  def translateMatrix(matrix: Array[Double], translation: Vector3): Vector3 =
  {
    val x: Double = translation.x * matrix(0) + translation.y * matrix(1) + translation.z * matrix(2) + matrix(3)
    val y: Double = translation.x * matrix(4) + translation.y * matrix(5) + translation.z * matrix(6) + matrix(7)
    val z: Double = translation.x * matrix(8) + translation.y * matrix(9) + translation.z * matrix(10) + matrix(11)
    translation.x = x
    translation.y = y
    translation.z = z
    return translation
  }

  def getRotationMatrix(angle: Float, axis: Vector3): Array[Double] =
  {
    return axis.getRotationMatrix(angle)
  }

  /**
   * Gets the delta look position based on the rotation yaw and pitch. Minecraft coordinates are
   * messed up. Y and Z are flipped. Yaw is displaced by 90 degrees. Pitch is inversed.
   *
   * @param rotationYaw
   * @param rotationPitch
   */
  @Deprecated def getDeltaPositionFromRotation(rotationYaw: Float, rotationPitch: Float): Vector3 =
  {
    return new Vector3(rotationYaw, rotationPitch)
  }

  def getAngle(vec1: Vector3, vec2: Vector3): Double =
  {
    return vec1.getAngle(vec2)
  }

  def anglePreNorm(vec1: Vector3, vec2: Vector3): Double =
  {
    return Math.acos(vec1.clone.dotProduct(vec2))
  }

  def UP: Vector3 =
  {
    return new Vector3(0, 1, 0)
  }

  def DOWN: Vector3 =
  {
    return new Vector3(0, -1, 0)
  }

  def NORTH: Vector3 =
  {
    return new Vector3(0, 0, -1)
  }

  def SOUTH: Vector3 =
  {
    return new Vector3(0, 0, 1)
  }

  def WEST: Vector3 =
  {
    return new Vector3(-1, 0, 0)
  }

  def EAST: Vector3 =
  {
    return new Vector3(1, 0, 0)
  }

  def ZERO: Vector3 =
  {
    return new Vector3(0, 0, 0)
  }

  def CENTER: Vector3 =
  {
    return new Vector3(0.5, 0.5, 0.5)
  }
}

class ScalaVector3 extends Cloneable
{
  def this(x: Double, y: Double, z: Double)
  {
    this()
    this.x = x
    this.y = y
    this.z = z
  }

  def this()
  {
    this()
    `this`(0, 0, 0)
  }

  def this(vector: Vector3)
  {
    this()
    `this`(vector.x, vector.y, vector.z)
  }

  def this(amount: Double)
  {
    this()
    `this`(amount, amount, amount)
  }

  def this(par1: Entity)
  {
    this()
    `this`(par1.posX, par1.posY, par1.posZ)
  }

  def this(par1: TileEntity)
  {
    this()
    `this`(par1.xCoord, par1.yCoord, par1.zCoord)
  }

  def this(par1: Vec3)
  {
    this()
    `this`(par1.xCoord, par1.yCoord, par1.zCoord)
  }

  def this(par1: MovingObjectPosition)
  {
    this()
    `this`(par1.blockX, par1.blockY, par1.blockZ)
  }

  def this(par1: ChunkCoordinates)
  {
    this()
    `this`(par1.posX, par1.posY, par1.posZ)
  }

  def this(direction: ForgeDirection)
  {
    this()
    `this`(direction.offsetX, direction.offsetY, direction.offsetZ)
  }

  /** Loads a Vector3 from an NBT compound. */
  def this(nbt: NBTTagCompound)
  {
    this()
    `this`(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))
  }

  /**
   * Get a relative Vector3 based on the rotationYaw and rotationPitch. Note that ROTATION PTICH
   * is inversed because of the way Minecraft handles things in the world.
   *
   * @param yaw - Degree
   * @param pitch- Degree
   */
  def this(yaw: Double, pitch: Double)
  {
    this()
    this(new EulerAngle(yaw, pitch))
  }

  def this(angle: EulerAngle)
  {
    this()
    `this`(angle.toVector)
  }

  /** Returns the coordinates as integers, ideal for block placement. */
  def intX: Int =
  {
    return Math.floor(this.x).asInstanceOf[Int]
  }

  def intY: Int =
  {
    return Math.floor(this.y).asInstanceOf[Int]
  }

  def intZ: Int =
  {
    return Math.floor(this.z).asInstanceOf[Int]
  }

  def floatX: Float =
  {
    return this.x.asInstanceOf[Float]
  }

  def floatY: Float =
  {
    return this.y.asInstanceOf[Float]
  }

  def floatZ: Float =
  {
    return this.z.asInstanceOf[Float]
  }

  /** Makes a new copy of this Vector. Prevents variable referencing problems. */
  override def clone: Vector3 =
  {
    return new Vector3(this)
  }

  /**
   * Easy block access functions.
   *
   * @param world
   * @return
   */
  def getBlockID(world: IBlockAccess): Int =
  {
    return world.getBlockId(this.intX, this.intY, this.intZ)
  }

  def getBlockMetadata(world: IBlockAccess): Int =
  {
    return world.getBlockMetadata(this.intX, this.intY, this.intZ)
  }

  def getTileEntity(world: IBlockAccess): TileEntity =
  {
    return world.getBlockTileEntity(this.intX, this.intY, this.intZ)
  }

  def setBlock(world: World, id: Int, metadata: Int, notify: Int): Boolean =
  {
    return world.setBlock(this.intX, this.intY, this.intZ, id, metadata, notify)
  }

  def setBlock(world: World, id: Int, metadata: Int): Boolean =
  {
    return this.setBlock(world, id, metadata, 3)
  }

  def setBlock(world: World, id: Int): Boolean =
  {
    return this.setBlock(world, id, 0)
  }

  /** Converts this Vector3 into a Vector2 by dropping the Y axis. */
  def toVector2: Vector2 =
  {
    return new Vector2(this.x, this.z)
  }

  /** Converts this vector three into a Minecraft Vec3 object */
  def toVec3: Vec3 =
  {
    return Vec3.createVectorHelper(this.x, this.y, this.z)
  }

  def toAngle: EulerAngle =
  {
    return new EulerAngle(Math.toDegrees(Math.atan2(x, z)), Math.toDegrees(-Math.atan2(y, Math.hypot(z, x))))
  }

  def toAngle(target: Vector3): EulerAngle =
  {
    return clone.difference(target).toAngle
  }

  /**
   * Saves this Vector3 to disk
   * @param nbt - The NBT compound object to save the data in
   */
  def writeToNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setDouble("x", this.x)
    nbt.setDouble("y", this.y)
    nbt.setDouble("z", this.z)
    return nbt
  }

  /** Converts Vector3 into a ForgeDirection. */
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

  /** ---------------------- MAGNITUDE FUNCTIONS ---------------------------- */
  def getMagnitude: Double =
  {
    return Math.sqrt(this.getMagnitudeSquared)
  }

  def getMagnitudeSquared: Double =
  {
    return this.x * this.x + this.y * this.y + this.z * this.z
  }

  def normalize: Vector3 =
  {
    val d: Double = this.getMagnitude
    if (d != 0)
    {
      this.scale(1 / d)
    }
    return this
  }

  def distance(x: Double, y: Double, z: Double): Double =
  {
    val difference: Vector3 = this.clone.difference(x, y, z)
    return difference.getMagnitude
  }

  def distance(compare: Vector3): Double =
  {
    return this.distance(compare.x, compare.y, compare.z)
  }

  def distance(entity: Entity): Double =
  {
    return this.distance(entity.posX, entity.posY, entity.posZ)
  }

  /** Multiplies the vector by negative one. */
  def invert: Vector3 =
  {
    this.scale(-1)
    return this
  }

  /**
   * ************************************************ OPERATIONS ******************************************************/
  */

  /**
   * Gets a position relative to a position's side
   *
   * @param side - The side. 0-5
   * @return The position relative to the original position's side
   */
  @Deprecated def translate(side: ForgeDirection, amount: Double): Vector3 =
  {
    return this.translate(new Vector3(side).scale(amount))
  }

  @Deprecated def translate(side: ForgeDirection): Vector3 =
  {
    return this.translate(side, 1)
  }

  @Deprecated def translate(addition: Vector3): Vector3 =
  {
    this.x += addition.x
    this.y += addition.y
    this.z += addition.z
    return this
  }

  @Deprecated def translate(x: Double, y: Double, z: Double): Vector3 =
  {
    this.x += x
    this.y += y
    this.z += z
    return this
  }

  def translate(addition: Double): Vector3 =
  {
    this.x += addition
    this.y += addition
    this.z += addition
    return this
  }

  def +(other: Vector)
  {
    x += other.x
    y += other.y
    z += other.z
    return this
  }

  def -(other: Vector)
  {
    x -= other.x
    y -= other.y
    z -= other.z
    return this
  }

  def *(other: Vector)
  {
    x *= other.x
    y *= other.y
    z *= other.z
    return this
  }

  /**
   * Alias methods.
   */
  def add(amount: Vector3): Vector3 =
  {
    return this + amount
  }

  def add(amount: Double): Vector3 =
  {
    return this + new Vector3(amount)
  }

  def add(x: Double, y: Double, z: Double): Vector3 =
  {
    return this + new Vector3(x, y, z)
  }

  def subtract(amount: Vector3): Vector3 =
  {
    return this - amount
  }

  def subtract(amount: Double): Vector3 =
  {
    return this - new Vector3(amount)
  }

  def subtract(x: Double, y: Double, z: Double): Vector3 =
  {
    return this - new Vector3(x, y, z)
  }

  def scale(x: Double, y: Double, z: Double): Vector3 =
  {
    this.x *= x
    this.y *= y
    this.z *= z
    return this
  }

  def scale(amount: Vector3): Vector3 =
  {
    return this * amount
  }

  /**
   * Advanced Operations
   */
  def max(other: Vector3): Vector3 =
  {
    return new Vector3(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z))
  }

  def min(other: Vector3): Vector3 =
  {
    return new Vector3(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z))
  }

  def round: Vector3 =
  {
    return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z))
  }

  def ceil: Vector3 =
  {
    return new Vector3(Math.ceil(this.x), Math.ceil(this.y), Math.ceil(this.z))
  }

  def floor: Vector3 =
  {
    return new Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z))
  }

  def midPoint(pos: Vector3): Vector3 =
  {
    return new Vector3((x + pos.x) / 2, (y + pos.y) / 2, (z + pos.z) / 2)
  }

  /**
   * Vector3 Products
   */
  def x(other: Vector3)
  {
    val newX: Double = this.y * other.z - this.z * other.y
    val newY: Double = this.z * other.x - this.x * other.z
    val newZ: Double = this.x * other.y - this.y * other.x
    this.x = newX
    this.y = newY
    this.z = newZ
    return this
  }

  def toCrossProduct(compare: Vector3): Vector3 =
  {
    return x(compare);
  }

  def crossProduct(compare: Vector3): Vector3 =
  {
    return this.clone.toCrossProduct(compare)
  }

  def xCrossProduct: Vector3 =
  {
    return new Vector3(0.0D, this.z, -this.y)
  }

  def zCrossProduct: Vector3 =
  {
    return new Vector3(-this.y, this.x, 0.0D)
  }

  def dot(other: Vector3): Double =
  {
    return x * other.x + y * other.y + z * other.z
  }

  /** @return The perpendicular vector. */
  def getPerpendicular: Vector3 =
  {
    if (this.z == 0.0F)
    {
      return this.zCrossProduct
    }
    return this.xCrossProduct
  }

  /** @return True if this Vector3 is zero. */
  def isZero: Boolean =
  {
    return equals(new Vector3())
  }

  /**
   * Rotate by a this vector around an axis.
   *
   * @return The new Vector3 rotation.
   */
  def rotate(angle: Float, axis: Vector3): Vector3 =
  {
    return translateMatrix(getRotationMatrix(angle, axis), this)
  }

  def getRotationMatrix(angle: Float): Array[Double] =
  {
    val matrix: Array[Double] = new Array[Double](16)
    val axis: Vector3 = this.clone.normalize
    val x: Double = axis.x
    val y: Double = axis.y
    val z: Double = axis.z
    angle *= 0.0174532925D
    val cos: Float = Math.cos(angle).asInstanceOf[Float]
    val ocos: Float = 1.0F - cos
    val sin: Float = Math.sin(angle).asInstanceOf[Float]
    matrix(0) = (x * x * ocos + cos)
    matrix(1) = (y * x * ocos + z * sin)
    matrix(2) = (x * z * ocos - y * sin)
    matrix(4) = (x * y * ocos - z * sin)
    matrix(5) = (y * y * ocos + cos)
    matrix(6) = (y * z * ocos + x * sin)
    matrix(8) = (x * z * ocos + y * sin)
    matrix(9) = (y * z * ocos - x * sin)
    matrix(10) = (z * z * ocos + cos)
    matrix(15) = 1.0F
    return matrix
  }

  @Deprecated def rotate(yaw: Double, pitch: Double, roll: Double)
  {
    rotate(new EulerAngle(yaw, roll))
  }

  /** Rotates this Vector by a yaw, pitch and roll value. */
  def rotate(angle: EulerAngle)
  {
    val yawRadians: Double = angle.yaw
    val pitchRadians: Double = angle.pitch
    val rollRadians: Double = angle.roll
    val x: Double = this.x
    val y: Double = this.y
    val z: Double = this.z
    this.x = x * Math.cos(yawRadians) * Math.cos(pitchRadians) + z * (Math.cos(yawRadians) * Math.sin(pitchRadians) * Math.sin(rollRadians) - Math.sin(yawRadians) * Math.cos(rollRadians)) + y * (Math.cos(yawRadians) * Math.sin(pitchRadians) * Math.cos(rollRadians) + Math.sin(yawRadians) * Math.sin(rollRadians))
    this.z = x * Math.sin(yawRadians) * Math.cos(pitchRadians) + z * (Math.sin(yawRadians) * Math.sin(pitchRadians) * Math.sin(rollRadians) + Math.cos(yawRadians) * Math.cos(rollRadians)) + y * (Math.sin(yawRadians) * Math.sin(pitchRadians) * Math.cos(rollRadians) - Math.cos(yawRadians) * Math.sin(rollRadians))
    this.y = -x * Math.sin(pitchRadians) + z * Math.cos(pitchRadians) * Math.sin(rollRadians) + y * Math.cos(pitchRadians) * Math.cos(rollRadians)
  }

  /** Rotates a point by a yaw and pitch around the anchor 0,0 by a specific angle. */
  def rotate(yaw: Double, pitch: Double)
  {
    rotate(new EulerAngle(yaw, pitch))
  }

  def rotate(yaw: Double)
  {
    val yawRadians: Double = Math.toRadians(yaw)
    val x: Double = this.x
    val z: Double = this.z
    if (yaw != 0)
    {
      this.x = x * Math.cos(yawRadians) - z * Math.sin(yawRadians)
      this.z = x * Math.sin(yawRadians) + z * Math.cos(yawRadians)
    }
  }

  def rotate(rotator: Quaternion): Vector3 =
  {
    rotator.rotate(this)
    return this
  }

  /**
   * Gets the angle between this vector and another vector.
   *
   * @return Angle in degrees
   */
  def getAngle(vec2: Vector3): Double =
  {
    return anglePreNorm(this.clone.normalize, vec2.clone.normalize)
  }

  def anglePreNorm(vec2: Vector3): Double =
  {
    return Math.acos(this.dotProduct(vec2))
  }

  /**
   * RayTrace Code, retrieved from MachineMuse.
   *
   * @author MachineMuse
   */
  def rayTrace(world: World, rotationYaw: Float, rotationPitch: Float, collisionFlag: Boolean, reachDistance: Double): MovingObjectPosition =
  {
    val lookVector: Vector3 = Vector3.getDeltaPositionFromRotation(rotationYaw, rotationPitch)
    val reachPoint: Vector3 = this.clone.translate(lookVector.clone.scale(reachDistance))
    return rayTrace(world, reachPoint, collisionFlag)
  }

  def rayTrace(world: World, reachPoint: Vector3, collisionFlag: Boolean): MovingObjectPosition =
  {
    val pickedBlock: MovingObjectPosition = this.rayTraceBlocks(world, reachPoint.clone, collisionFlag)
    val pickedEntity: MovingObjectPosition = this.rayTraceEntities(world, reachPoint.clone)
    if (pickedBlock == null)
    {
      return pickedEntity
    }
    else if (pickedEntity == null)
    {
      return pickedBlock
    }
    else
    {
      val dBlock: Double = this.distance(new Vector3(pickedBlock.hitVec))
      val dEntity: Double = this.distance(new Vector3(pickedEntity.hitVec))
      if (dEntity < dBlock)
      {
        return pickedEntity
      }
      else
      {
        return pickedBlock
      }
    }
  }

  def rayTrace(world: World, collisionFlag: Boolean, reachDistance: Double): MovingObjectPosition =
  {
    return rayTrace(world, 0, 0, collisionFlag, reachDistance)
  }

  def rayTraceBlocks(world: World, rotationYaw: Float, rotationPitch: Float, collisionFlag: Boolean, reachDistance: Double): MovingObjectPosition =
  {
    val lookVector: Vector3 = Vector3.getDeltaPositionFromRotation(rotationYaw, rotationPitch)
    val reachPoint: Vector3 = this.clone.translate(lookVector.clone.scale(reachDistance))
    return rayTraceBlocks(world, reachPoint, collisionFlag)
  }

  def rayTraceBlocks(world: World, vec: Vector3, collisionFlag: Boolean): MovingObjectPosition =
  {
    return world.rayTraceBlocks_do_do(this.toVec3, vec.toVec3, collisionFlag, !collisionFlag)
  }

  @Deprecated def rayTraceEntities(world: World, rotationYaw: Float, rotationPitch: Float, collisionFlag: Boolean, reachDistance: Double): MovingObjectPosition =
  {
    return this.rayTraceEntities(world, rotationYaw, rotationPitch, reachDistance)
  }

  def rayTraceEntities(world: World, rotationYaw: Float, rotationPitch: Float, reachDistance: Double): MovingObjectPosition =
  {
    return this.rayTraceEntities(world, getDeltaPositionFromRotation(rotationYaw, rotationPitch).scale(reachDistance))
  }

  /**
   * Does an entity raytrace.
   *
   * @param world - The world object.
   * @param target - The rotation in terms of Vector3. Convert using
   *               getDeltaPositionFromRotation()
   * @return The target hit.
   */
  def rayTraceEntities(world: World, target: Vector3): MovingObjectPosition =
  {
    var pickedEntity: MovingObjectPosition = null
    val startingPosition: Vec3 = toVec3
    val look: Vec3 = target.toVec3
    val reachDistance: Double = distance(target)
    val checkBorder: Double = 1.1 * reachDistance
    val boxToScan: AxisAlignedBB = AxisAlignedBB.getAABBPool.getAABB(-checkBorder, -checkBorder, -checkBorder, checkBorder, checkBorder, checkBorder).offset(this.x, this.y, this.z)
    @SuppressWarnings(Array("unchecked")) val entitiesInBounds: List[Entity] = world.getEntitiesWithinAABBExcludingEntity(null, boxToScan)
    var closestEntity: Double = reachDistance
    if (entitiesInBounds == null || entitiesInBounds.isEmpty)
    {
      return null
    }
    import scala.collection.JavaConversions._
    for (possibleHits <- entitiesInBounds)
    {
      if (possibleHits != null && possibleHits.canBeCollidedWith && possibleHits.boundingBox != null)
      {
        val border: Float = possibleHits.getCollisionBorderSize
        val aabb: AxisAlignedBB = possibleHits.boundingBox.expand(border, border, border)
        val hitMOP: MovingObjectPosition = aabb.calculateIntercept(startingPosition, target.toVec3)
        if (hitMOP != null)
        {
          if (aabb.isVecInside(startingPosition))
          {
            if (0.0D < closestEntity || closestEntity == 0.0D)
            {
              pickedEntity = new MovingObjectPosition(possibleHits)
              if (pickedEntity != null)
              {
                pickedEntity.hitVec = hitMOP.hitVec
                closestEntity = 0.0D
              }
            }
          }
          else
          {
            val distance: Double = startingPosition.distanceTo(hitMOP.hitVec)
            if (distance < closestEntity || closestEntity == 0.0D)
            {
              pickedEntity = new MovingObjectPosition(possibleHits)
              pickedEntity.hitVec = hitMOP.hitVec
              closestEntity = distance
            }
          }
        }
      }
    }
    return pickedEntity
  }

  def rayTraceEntities(world: World, target: Entity): MovingObjectPosition =
  {
    return rayTraceEntities(world, new Vector3(target))
  }

  override def hashCode: Int =
  {
    val x: Long = Double.doubleToLongBits(this.x)
    val y: Long = Double.doubleToLongBits(this.y)
    val z: Long = Double.doubleToLongBits(this.z)
    var hash: Int = (x ^ (x >>> 32)).asInstanceOf[Int]
    hash = 31 * hash + (y ^ (y >>> 32)).asInstanceOf[Int]
    hash = 31 * hash + (z ^ (z >>> 32)).asInstanceOf[Int]
    return hash
  }

  override def equals(o: AnyRef): Boolean =
  {
    if (o.isInstanceOf[Vector3])
    {
      val vector3: Vector3 = o.asInstanceOf[Vector3]
      return this.x == vector3.x && this.y == vector3.y && this.z == vector3.z
    }
    return false
  }

  override def toString: String =
  {
    return "Vector3 [" + this.x + "," + this.y + "," + this.z + "]"
  }

  var x: Double = .0
  var y: Double = .0
  var z: Double = .0
}