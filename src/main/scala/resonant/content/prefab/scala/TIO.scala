package resonant.lib.content.prefab

import _root_.java.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ChatComponentText
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.IIO
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.utility.nbt.ISaveObj

import scala.collection.convert.wrapAll._

/**
 * A Trait that handles IO Traits
 *
 * @author Calclavia
 */
trait TIO extends SpatialBlock with IIO with ISaveObj
{
  /**
   * IO METHODS.
   * Default: Connect from all sides. "111111"
   * Output all sides: 728
   * 0 - Nothing
   * 1 - Input
   * 2 - Output
   */
  protected var ioMap: Short = 364
  protected var saveIOMap: Boolean = false

  def toggleIO(side: Int, entityPlayer: EntityPlayer): Boolean =
  {
    val newIO = (getIO(ForgeDirection.getOrientation(side)) + 1) % 3
    setIO(ForgeDirection.getOrientation(side), newIO)

    if (!world.isRemote)
    {
      entityPlayer.addChatMessage(new ChatComponentText("Side changed to: " + (if (newIO == 0) "None" else (if (newIO == 1) "Input" else "Output"))))
    }

    world.notifyBlocksOfNeighborChange(x, y, z, block)
    return true
  }

  /**
   * The electrical input direction.
   *
   * @return The direction that electricity is entered into the tile. Return null for no input. By
   *         default you can accept power from all sides.
   */
  override def getInputDirections(): util.HashSet[ForgeDirection] =
  {
    var dirs = new util.HashSet[ForgeDirection]()

    for (direction <- ForgeDirection.VALID_DIRECTIONS)
    {
      if (getIO(direction) == 1)
      {
        dirs += direction
      }

    }
    return dirs
  }

  /**
   * The electrical output direction.
   *
   * @return The direction that electricity is output from the tile. Return null for no output. By
   *         default it will return an empty EnumSet.
   */
  override def getOutputDirections(): util.HashSet[ForgeDirection] =
  {
    var dirs = new util.HashSet[ForgeDirection]()

    for (direction <- ForgeDirection.VALID_DIRECTIONS)
    {
      if (getIO(direction) == 2)
      {
        dirs += direction
      }
    }

    return dirs
  }

  override def setIO(dir: ForgeDirection, `type`: Int)
  {
    val currentIO: String = getIOMapBase3
    val str: StringBuilder = new StringBuilder(currentIO)
    str.setCharAt(dir.ordinal, Integer.toString(`type`).charAt(0))
    this.ioMap = Integer.parseInt(str.toString, 3).toShort
  }

  override def getIO(dir: ForgeDirection): Int =
  {
    val currentIO: String = getIOMapBase3
    return Integer.parseInt("" + currentIO.charAt(dir.ordinal))
  }

  def getIOMapBase3: String =
  {
    var currentIO: String = Integer.toString(ioMap, 3)
    while (currentIO.length < 6)
    {
      currentIO = "0" + currentIO
    }
    return currentIO

  }

  /** Saves the object to NBT */
  override def save(nbt: NBTTagCompound)
  {
    if (saveIOMap && nbt.hasKey("ioMap"))
    {
      this.ioMap = nbt.getShort("ioMap")
    }
  }

  /** Load the object from NBT */
  override def load(nbt: NBTTagCompound)
  {
    if (saveIOMap)
    {
      nbt.setShort("ioMap", this.ioMap)
    }
  }

}