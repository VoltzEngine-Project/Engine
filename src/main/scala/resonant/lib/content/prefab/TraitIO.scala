package resonant.lib.content.prefab

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.ForgeDirection
import resonant.lib.content.module.TileBase
import resonant.lib.utility.nbt.ISaveObj

/**
 * a Trait that handles IO Traits
 *
 * @author tgame14
 */
trait TraitIO extends TileBase with ISaveObj {
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
  def getInputDirections: Set[ForgeDirection] =
    {
      var dirs = Set[ForgeDirection]()

      for (direction <- ForgeDirection.VALID_DIRECTIONS) {
        if (getIO(direction) == 1) {
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
  def getOutputDirections: Set[ForgeDirection] =
    {
      var dirs = Set[ForgeDirection]()

      for (direction <- ForgeDirection.VALID_DIRECTIONS) {
        if (getIO(direction) == 2) {
          dirs += direction
        }
      }

      return dirs
    }

  def setIO(dir: ForgeDirection, `type`: Int) {
    val currentIO: String = getIOMapBase3
    val str: StringBuilder = new StringBuilder(currentIO)
    str.setCharAt(dir.ordinal, Integer.toString(`type`).charAt(0))
    this.ioMap = Integer.parseInt(str.toString, 3).toShort
  }

  def getIO(dir: ForgeDirection): Int =
    {
      val currentIO: String = getIOMapBase3
      return Integer.parseInt("" + currentIO.charAt(dir.ordinal))
    }

  def getIOMapBase3: String =
    {
      var currentIO: String = Integer.toString(ioMap, 3)
      while (currentIO.length < 6) {
        currentIO = "0" + currentIO
      }
      return currentIO

    }

  /** Saves the object to NBT */
  override def save(nbt: NBTTagCompound) {
    if (saveIOMap && nbt.hasKey("ioMap")) {
      this.ioMap = nbt.getShort("ioMap")
    }
  }

  /** Load the object from NBT */
  override def load(nbt: NBTTagCompound) {
    if (saveIOMap) {
      nbt.setShort("ioMap", this.ioMap)
    }
  }

}