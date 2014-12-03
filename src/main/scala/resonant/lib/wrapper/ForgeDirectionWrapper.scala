package resonant.lib.wrapper

import net.minecraft.util.EnumFacing
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

  implicit def ForgeDirectionEnumFacingWrapper(enumFacing : EnumFacing) : ForgeDirection= ForgeDirection.getOrientation(enumFacing.ordinal)

  implicit def EnumFacingForgeDirectionWrapper(forgeDir : ForgeDirection) : EnumFacing = EnumFacing.getFront(forgeDir.ordinal)
}
