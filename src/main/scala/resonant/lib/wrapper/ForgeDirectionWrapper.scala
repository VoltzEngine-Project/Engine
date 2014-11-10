package resonant.lib.wrapper

import net.minecraftforge.common.util.ForgeDirection
import resonant.lib.transform.vector.Vector3

/**
 * Wraps ForgeDirection an adds some extension methods
 * @author Calclavia
 */
object ForgeDirectionWrapper
{

  implicit class ForgeDirectionWrap(val underlying: ForgeDirection)
  {
    def offset: Vector3 = new Vector3(underlying.offsetX, underlying.offsetY, underlying.offsetZ)
  }

}
