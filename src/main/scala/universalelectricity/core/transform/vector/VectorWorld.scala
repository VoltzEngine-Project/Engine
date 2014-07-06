package universalelectricity.core.transform.vector

import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.util.ForgeDirection

class VectorWorld(var world: World, newX: Double, newY: Double, newZ: Double) extends Vector3(newX, newY, newZ) with IVectorWorld
{
  def this(nbt: NBTTagCompound) = this(DimensionManager.getWorld(nbt.getInteger("dimension")), nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))

  def this(data: ByteBuf) = this(DimensionManager.getWorld(data.readInt()), data.readDouble(), data.readDouble(), data.readDouble())

  def this(entity: Entity) = this(entity.worldObj, entity.posX, entity.posY, entity.posZ)

  def this(tile: TileEntity) = this(tile.getWorldObj, tile.xCoord, tile.yCoord, tile.zCoord)

  def this(world: World, vector: IVector3) = this(world, vector.x, vector.y, vector.z)

  def world(newWorld: World)
  {
    world = newWorld
  }

  override def set(vec: Vector3): VectorWorld =
  {
    if (vec.isInstanceOf[VectorWorld])
      world = vec.asInstanceOf[VectorWorld].world
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

  /**
   * Operations
   */
  override def +(amount: Double): VectorWorld = new VectorWorld(world, x + amount, y + amount, z + amount)

  override def +(amount: Vector3): VectorWorld = new VectorWorld(world, x + amount.x, y + amount.y, z + amount.z)

  override def +(x: Double, y: Double, z: Double): VectorWorld = new VectorWorld(world, this.x + x, this.y + y, this.z + z)

  override def +=(x: Double, y: Double, z: Double): VectorWorld = set(new VectorWorld(world, this.x + x, this.y + y, this.z + z))

  override def add(x: Double, y: Double, z: Double): VectorWorld = this +(x, y, z)

  override def addEquals(x: Double, y: Double, z: Double): VectorWorld = this +=(x, y, z)

  override def +(amount: ForgeDirection): VectorWorld = this + new Vector3(amount)

  override def +=(amount: ForgeDirection): VectorWorld = set(this + new Vector3(amount))

  override def add(amount: ForgeDirection): VectorWorld = this + amount

  override def addEquals(amount: ForgeDirection): VectorWorld = this += amount

  override def *(amount: Double): VectorWorld = new VectorWorld(world, x * amount, y * amount, z * amount)

  override def *(amount: Vector3): VectorWorld = new VectorWorld(world, x * amount.x, y * amount.y, z * amount.z)

  /**
   * "Generated" method override
    */
  override def -(amount: Double): VectorWorld = this + -amount

  override def -(amount: Vector3): VectorWorld = this + (-amount)

  override def /(amount: Double): VectorWorld = this * (1 / amount)

  override def /(amount: Vector3): VectorWorld = this * (amount * -1)

  override def +=(amount: Double): VectorWorld = set(this + amount)

  override def +=(amount: Vector3): VectorWorld = set(amount)

  override def -=(amount: Double): VectorWorld = this += -amount

  override def -=(amount: Vector3): VectorWorld = this += (amount * -1)

  override def *=(amount: Double): VectorWorld = set(this * amount)

  override def *=(amount: Vector3): VectorWorld = set(this * amount)

  override def /=(amount: Double): VectorWorld = this *= (1 / amount)

  override def /=(amount: Vector3): VectorWorld = this *= (amount.reciprocal)
          
  /**
   * "Generated" Alias Operation Methods override
   */
  override def add(amount: Double): VectorWorld = this + amount

  override def add(amount: Vector3): VectorWorld = this + amount

  override def subtract(amount: Double): VectorWorld = this - amount

  override def subtract(amount: Vector3): VectorWorld = this - amount

  override def multiply(amount: Double): VectorWorld = this * amount

  override def multiply(amount: Vector3): VectorWorld = this * amount

  override def divide(amount: Double): VectorWorld = this / amount

  override def addEquals(amount: Double): VectorWorld = this += amount

  override def addEquals(amount: Vector3): VectorWorld = this += amount

  override def subtractEquals(amount: Double): VectorWorld = this -= amount

  override def subtractEquals(amount: Vector3): VectorWorld = this -= amount

  override def multiplyEquals(amount: Double): VectorWorld = this *= amount

  override def multiplyEquals(amount: Vector3): VectorWorld = this *= amount

  override def divideEquals(amount: Double): VectorWorld = this /= amount

  override def divideEquals(amount: Vector3): VectorWorld = this /= amount

  /**
   * World Access
   */
  def getBlock: Block = if (world != null) super.getBlock(world) else null

  def getBlockMetadata = if (world != null) super.getBlockMetadata(world) else null

  def getTileEntity = if (world != null) super.getTileEntity(world) else null

  def setBlock(block: Block, metadata: Int, notify: Int): Boolean = super.setBlock(world, block, metadata, notify)

  def setBlock(block: Block, metadata: Int): Boolean = super.setBlock(world, block, metadata)

  def setBlock(block: Block): Boolean = super.setBlock(world, block)

  def rayTraceEntities(target: Vector3): MovingObjectPosition = super.rayTraceEntities(world, target)

  override def clone: VectorWorld = new VectorWorld(world, x, y, z)

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[VectorWorld])
    {
      return (super.equals(o)) && this.world == (o.asInstanceOf[VectorWorld]).world
    }
    return false
  }

  override def toString: String =
  {
    return "VectorWorld [" + this.x + "," + this.y + "," + this.z + "," + this.world + "]"
  }
}