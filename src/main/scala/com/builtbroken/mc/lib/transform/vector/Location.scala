package com.builtbroken.mc.lib.transform.vector

import com.builtbroken.jlib.data.vector.IPos3D
import com.builtbroken.mc.api.IPosWorld
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{MovingObjectPosition, Vec3}
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.util.ForgeDirection

class Location(var world: World, newX: Double, newY: Double, newZ: Double) extends Pos(newX, newY, newZ)
{
  def this(nbt: NBTTagCompound) = this(DimensionManager.getWorld(nbt.getInteger("dimension")), nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))

  def this(data: ByteBuf) = this(DimensionManager.getWorld(data.readInt()), data.readDouble(), data.readDouble(), data.readDouble())

  def this(entity: Entity) = this(entity.worldObj, entity.posX, entity.posY, entity.posZ)

  def this(tile: TileEntity) = this(tile.getWorldObj, tile.xCoord, tile.yCoord, tile.zCoord)

  def this(vec: IPosWorld) = this(vec.world, vec.x, vec.y, vec.z)

  def this(world: World, vector: Pos) = this(world, vector.x, vector.y, vector.z)

  def this(world: World, vector: IPos3D) = this(world, vector.x, vector.y, vector.z)

  def this(world: World, vec: Vec3) = this(world, vec.xCoord, vec.yCoord, vec.zCoord)

  def this(world: World, target: MovingObjectPosition) = this(world, target.hitVec)

  def world(newWorld: World)
  {
    world = newWorld
  }

  override def set(vec: IPos3D): Location =
  {
    if (vec.isInstanceOf[Location])
      world = vec.asInstanceOf[Location].world
    x = vec.x
    y = vec.y
    z = vec.z
    return this
  }

  /**
   * Conversions
   */
  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setInteger("dimension", world.provider.dimensionId)
    nbt.setDouble("x", x)
    nbt.setDouble("y", y)
    nbt.setDouble("z", z)
    return nbt
  }

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    data.writeInt(world.provider.dimensionId)
    data.writeDouble(x)
    data.writeDouble(y)
    data.writeDouble(z)
    return data
  }

  def toVector3 = new Pos(x, y, z)

  /**
   * Operations
   */
  override def +(amount: Double): Location = new Location(world, x + amount, y + amount, z + amount)

  override def +(amount: IPos3D): Location = new Location(world, x + amount.x, y + amount.y, z + amount.z)

  override def +(x: Double, y: Double, z: Double): Location = new Location(world, this.x + x, this.y + y, this.z + z)

  override def +=(x: Double, y: Double, z: Double): Location = set(new Location(world, this.x + x, this.y + y, this.z + z))

  override def add(x: Double, y: Double, z: Double): Location = this +(x, y, z)

  override def addEquals(x: Double, y: Double, z: Double): Location = this +=(x, y, z)

  override def +(amount: ForgeDirection): Location = this + new Pos(amount)

  override def +=(amount: ForgeDirection): Location = set(this + new Pos(amount))

  override def add(amount: ForgeDirection): Location = this + amount

  override def addEquals(amount: ForgeDirection): Location = this += amount

  override def *(amount: Double): Location = new Location(world, x * amount, y * amount, z * amount)

  override def *(amount: IPos3D): Location = new Location(world, x * amount.x, y * amount.y, z * amount.z)

  /**
   * "Generated" method override
   */
  override def -(amount: Double): Location = new Location(world, x - amount, y - amount, z - amount)

  override def -(amount: IPos3D): Location = new Location(world, x - amount.x, y - amount.y, z - amount.z)

  override def /(amount: Double): Location = this * (1 / amount)

  override def /(amount: IPos3D): Location = new Location(world, x / amount.x, y / amount.y, z / amount.z)

  override def +=(amount: Double): Location =
  {
    x += amount
    y += amount
    z += amount
    return this
  }

  override def +=(amount: IPos3D): Location =
  {
    x += amount.x
    y += amount.y
    z += amount.z
    return this
  }

  override def -=(amount: Double): Location = this += -amount

  override def -=(amount: IPos3D): Location =
  {
    x -= amount.x
    y -= amount.y
    z -= amount.z
    return this
  }

  override def *=(amount: Double): Location =
  {
    x *= amount
    y *= amount
    z *= amount
    return this
  }

  override def *=(amount: IPos3D): Location =
  {
    x *= amount.x
    y *= amount.y
    z *= amount.z
    return this
  }

  override def /=(amount: Double): Location = this *= (1 / amount)

  override def /=(amount: IPos3D): Location =
  {
    x *= amount.x
    y *= amount.y
    z *= amount.z
    return this
  }

  /**
   * "Generated" Alias Operation Methods override
   */
  override def add(amount: Double): Location = this + amount

  override def add(amount: IPos3D): Location = this + amount

  override def subtract(amount: Double): Location = this - amount

  override def subtract(amount: IPos3D): Location = this - amount

  override def multiply(amount: Double): Location = this * amount

  override def multiply(amount: IPos3D): Location = this * amount

  override def divide(amount: Double): Location = this / amount

  override def addEquals(amount: Double): Location = this += amount

  override def addEquals(amount: IPos3D): Location = this += amount

  override def subtractEquals(amount: Double): Location = this -= amount

  override def subtractEquals(amount: IPos3D): Location = this -= amount

  override def multiplyEquals(amount: Double): Location = this *= amount

  override def multiplyEquals(amount: IPos3D): Location = this *= amount

  override def divideEquals(amount: Double): Location = this /= amount

  override def divideEquals(amount: IPos3D): Location = this /= amount

  /**
   * World Access
   */
  def getBlock: Block = if (world != null) super.getBlock(world) else null

  def getBlockMetadata: Int = if (world != null) super.getBlockMetadata(world) else -1

  def getTileEntity: TileEntity = if (world != null) super.getTileEntity(world) else null

  def getHardness() : Float = super.getHardness(world)

  def getResistance(cause: Entity, xx: Double, yy: Double, zz: Double): Float =
  {
    return getBlock(world).getExplosionResistance(cause, world, xi, yi, zi, xx, yy, zz)
  }

  def setBlock(block: Block, metadata: Int, notify: Int): Boolean = super.setBlock(world, block, metadata, notify)

  def setBlock(block: Block, metadata: Int): Boolean = super.setBlock(world, block, metadata)

  def setBlock(block: Block): Boolean = super.setBlock(world, block)

  def setBlockToAir() : Boolean = super.setBlockToAir(world)

  def isAirBlock() : Boolean = super.isAirBlock(world)

  def isBlockEqual(block: Block) = super.isBlockEqual(world, block)

  def isBlockFreezable() : Boolean = super.isBlockFreezable(world)

  def rayTraceEntities(target: Pos): MovingObjectPosition = super.rayTraceEntities(world, target)

  override def clone: Location = new Location(world, x, y, z)

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[IPosWorld])
    {
      return (super.equals(o)) && this.world == (o.asInstanceOf[IPosWorld]).world
    }
    return false
  }

  override def toString: String =
  {
    return "VectorWorld [" + this.x + "," + this.y + "," + this.z + "," + this.world + "]"
  }
}