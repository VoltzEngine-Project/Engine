package resonant.lib.utility

import net.minecraftforge.common.util.ForgeDirection

object RotationUtility
{
  val relativeMatrix: Array[Array[Int]] = Array(Array(3, 2, 1, 0, 5, 4), Array(4, 5, 0, 1, 2, 3), Array(0, 1, 3, 2, 5, 4), Array(0, 1, 2, 3, 4, 5), Array(0, 1, 4, 5, 3, 2), Array(0, 1, 5, 4, 2, 3))
  var sideRotMap: Array[Int] = Array[Int](3, 4, 2, 5, 3, 5, 2, 4, 1, 5, 0, 4, 1, 4, 0, 5, 1, 2, 0, 3, 1, 3, 0, 2)

  /**
   * Rototes a relative side into a ForgeDirection global size.
   *
   * @param s - The current face we are on (0-6)
   * @param r - The rotation to be applied (0-3)
   * @return The ForgeDirection ordinal from 0-5.
   */
  def rotateSide(s: Int, r: Int): Int = sideRotMap(s << 2 | r)

  def rotateSide(s: ForgeDirection, r: ForgeDirection): ForgeDirection = ForgeDirection.getOrientation(sideRotMap(s.ordinal << 2 | r.ordinal))

  /**
   * Finds the direction relative to a base direction.
   *
   * @param front - The direction in which this block is facing/front. Use a number between 0 and
   *              5. Default is 3.
   * @param side - The side you are trying to find. A number between 0 and 5.
   * @return The side relative to the facing direction.
   */
  def getRelativeSide(front: ForgeDirection, side: ForgeDirection): ForgeDirection =
  {
    if (front != ForgeDirection.UNKNOWN && side != ForgeDirection.UNKNOWN)
    {
      return ForgeDirection.getOrientation(relativeMatrix(front.ordinal)(side.ordinal))
    }
    return ForgeDirection.UNKNOWN
  }
}