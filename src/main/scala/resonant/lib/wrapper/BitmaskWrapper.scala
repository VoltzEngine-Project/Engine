package resonant.lib.wrapper

import net.minecraftforge.common.util.ForgeDirection

/**
 * Wraps bytes/short/integers to be used for masks
 * @author Calclavia
 */
object BitmaskWrapper
{

  /**
   * Java forwarding
   */
  def mask(sideMap: Int, direction: ForgeDirection): Boolean =
  {
    return sideMap.mask(direction)
  }

  def mask(sideMap: Int, direction: ForgeDirection, doEnable: Boolean): Int =
  {
    return sideMap.mask(direction, doEnable)
  }

  implicit class BitmaskInt(val underlying: Int) extends AnyVal
  {
    def openMask(i: Int) = mask(i, true)

    /**
     * Sets the bitmask index to be either open or closed
     * @param i - Index
     * @param value - True for open
     * @return - The new bitmask
     */
    def mask(i: Int, value: Boolean): Int =
    {
      if (value)
        return underlying | (1 << i)
      else
        return underlying & ~(1 << i)
    }

    def closeMask(i: Int) = mask(i, false)

    /**
     * Forge Direction Alternatives
     */
    def mask(dir: ForgeDirection): Boolean = mask(dir.ordinal())

    /**
     * Checks if the bitmask is open
     * @param i - The index of the bitmask
     * @return True if open
     */
    def mask(i: Int): Boolean = (underlying & (1 << i)) != 0

    def mask(dir: ForgeDirection, value: Boolean): Int = mask(dir.ordinal(), value)

    def openMask(dir: ForgeDirection) = mask(dir.ordinal(), true)

    def closeMask(dir: ForgeDirection) = mask(dir.ordinal(), false)
  }
}
