package resonant.lib.utility

object RotationUtility {
  var sideRotMap: Array[Int] = Array[Int](3, 4, 2, 5, 3, 5, 2, 4, 1, 5, 0, 4, 1, 4, 0, 5, 1, 2, 0, 3, 1, 3, 0, 2)

  /**
   * Rototes a relative side into a ForgeDirection global size.
   *
   * @param s - The current face we are on (0-6)
   * @param r - The rotation to be applied (0-3)
   * @return The ForgeDirection ordinal from 0-5.
   */
  def rotateSide(s: Int, r: Int): Int =
    {
      return sideRotMap(s << 2 | r)
    }
}