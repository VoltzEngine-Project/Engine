package universalelectricity.core.vector

import java.util.List
import net.minecraft.world.World
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity


class VectorWorld(var world: World) extends Vector3 with IVectorWorld
{
  def this(world: World, x: Double, y: Double, z: Double)
  {
    this(world)
    this.x = x
    this.y = x
    this.z = x
  }

  def this(nbt: NBTTagCompound)
  {
    this(world)
    `super`(nbt)
  }

  def this(entity: Entity)
  {
    this(entity.worldObj, entity.posX, entity.posY, entity.posZ)
  }

  def this(tile: TileEntity)
  {
    this(tile.getWorldObj, tile.x, tile.y, tile.z)
  }

  def this(world: World, vector: IVector3)
  {
    this(world, vector.x, vector.y, vector.z)
  }

  /**
   * Operations
   */
  override def +(amount: Double): Vector3 = new Vector3(x + amount, y + amount, z + amount)

  def +(amount: Vector3): Vector3 = new Vector3(x + amount.x, y + amount.y, z + amount.z)

  override def -(amount: Double): Vector3 = this + -amount

  def -(amount: Vector3): Vector3 = this + (amount * -1)

  override def *(amount: Double): Vector3 = new Vector3(x * amount, y * amount, z * amount)

  override def /(amount: Double): Vector3 = new Vector3(x / amount, y / amount, z / amount)

  override def +=(amount: Double): Vector3 =
  {
    x += amount
    y += amount
    z += amount
    return this
  }

  def +=(amount: Vector3): Vector3 =
  {
    x += amount.x
    y += amount.y
    z += amount.z
    return this
  }

  override def /=(amount: Double): Vector3 =
  {
    x /= amount
    y /= amount
    z /= amount
    return this
  }

  override def *=(amount: Double): Vector3 =
  {
    x *= amount
    y *= amount
    z *= amount
    return this
  }

  override def clone: VectorWorld =
  {
    return new VectorWorld(world, x, y, z)
  }

  def writeToNBT(nbt: Nothing): Nothing =
  {
    super.writeToNBT(nbt)
    nbt.setInteger("d", this.world.provider.dimensionId)
    return nbt
  }

  def getBlock: Nothing =
  {
    return super.getBlock(this.world)
  }

  def getBlockMetadata: Int =
  {
    return super.getBlockMetadata(this.world)
  }

  def getTileEntity: Nothing =
  {
    return super.getTileEntity(this.world)
  }

  def setBlock(block: Nothing, metadata: Int, notify: Int): Boolean =
  {
    return super.setBlock(this.world, block, metadata, notify)
  }

  def setBlock(block: Nothing, metadata: Int): Boolean =
  {
    return this.setBlock(block, metadata, 3)
  }

  def setBlock(block: Nothing): Boolean =
  {
    return this.setBlock(block, 0)
  }

  def getEntitiesWithin(par1Class: Class[_ <: Nothing]): List[Nothing] =
  {
    return super.getEntitiesWithin(this.world, par1Class)
  }

  def rayTraceEntities(target: VectorWorld): Nothing =
  {
    return super.rayTraceEntities(target.world, target)
  }

  def rayTraceEntities(target: Nothing): Nothing =
  {
    return super.rayTraceEntities(world, target)
  }

  def rayTraceEntities(target: Vector3): Nothing =
  {
    return super.rayTraceEntities(world, target)
  }

  override def equals(o: AnyRef): Boolean =
  {
    if (o.isInstanceOf[VectorWorld])
    {
      return (super.equals(o)) && this.world eq (o.asInstanceOf[VectorWorld]).world
    }
    return false
  }

  override def toString: String =
  {
    return "VectorWorld [" + this.x + "," + this.y + "," + this.z + "," + this.world + "]"
  }
}