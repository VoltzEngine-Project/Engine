package calclavia.lib.scala

import java.util.EnumSet
import net.minecraftforge.common.ForgeDirection
import net.minecraft.nbt.NBTTagCompound
import calclavia.lib.prefab.tile.IIO
import calclavia.lib.utility.nbt.ISaveObj

/**
 * a Trait that handles IO Traits
 *
 * @author tgame14
 */
trait TraitIO extends IIO with ISaveObj
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

  /**
   * The electrical input direction.
   *
   * @return The direction that electricity is entered into the tile. Return null for no input. By
   *         default you can accept power from all sides.
   */
  def getInputDirections: EnumSet[ForgeDirection] =
  {
    val dirs: EnumSet[ForgeDirection] = EnumSet.noneOf(classOf[ForgeDirection])
    for (direction <- ForgeDirection.VALID_DIRECTIONS)
    {
      if (getIO(direction) == 1)
      {
        dirs.add(direction)
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
  def getOutputDirections: EnumSet[ForgeDirection] =
  {
    val dirs: EnumSet[ForgeDirection] = EnumSet.noneOf(classOf[ForgeDirection])
    for (direction <- ForgeDirection.VALID_DIRECTIONS)
    {
      if (getIO(direction) == 2)
      {
        dirs.add(direction)
      }
    }
    return dirs
  }

  def setIO(dir: ForgeDirection, `type`: Int)
  {
    val currentIO: String = getIOMapBase3
    val str: StringBuilder = new StringBuilder(currentIO)
    str.setCharAt(dir.ordinal, Integer.toString(`type`).charAt(0))
    this.ioMap = Short.parseShort(str.toString, 3)
  }

  def getIO(dir: ForgeDirection): Int =
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