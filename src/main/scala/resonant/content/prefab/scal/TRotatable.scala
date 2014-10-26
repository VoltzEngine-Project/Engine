package resonant.lib.content.prefab

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MathHelper
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.IRotatable
import resonant.content.spatial.block.SpatialBlock
import universalelectricity.core.transform.vector.Vector3

trait TRotatable extends SpatialBlock with IRotatable
{
  var rotationMask = Integer.parseInt("111100", 2).toByte
  var isFlipPlacement = false

  override def getDirection: ForgeDirection = ForgeDirection.getOrientation(getBlockMetadata)

  def determineRotation(entityLiving: EntityLivingBase): ForgeDirection =
  {
    if (MathHelper.abs(entityLiving.posX.asInstanceOf[Float] - xi) < 2.0F && MathHelper.abs(entityLiving.posZ.asInstanceOf[Float] - zi) < 2.0F)
    {
      val d0: Double = entityLiving.posY + 1.82D - entityLiving.yOffset
      if (canRotate(1) && d0 - y > 2.0D)
      {
        return ForgeDirection.UP
      }
      if (canRotate(0) && y - d0 > 0.0D)
      {
        return ForgeDirection.DOWN
      }
    }

    val playerSide = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3
    val returnSide = if ((playerSide == 0 && canRotate(2))) 2 else (if ((playerSide == 1 && canRotate(5))) 5 else (if ((playerSide == 2 && canRotate(3))) 3 else (if ((playerSide == 3 && canRotate(4))) 4 else 0)))

    if (isFlipPlacement)
    {
      return ForgeDirection.getOrientation(returnSide).getOpposite
    }

    return ForgeDirection.getOrientation(returnSide)
  }

  def canRotate(ord: Int): Boolean = (rotationMask & (1 << ord)) != 0

  /**
   * Rotatable Block
   */
  def rotate(side: Int, hit: Vector3): Boolean =
  {
    val result = getSideToRotate(side.asInstanceOf[Byte], hit.x, hit.y, hit.z)

    if (result != -1)
    {
      setDirection(ForgeDirection.getOrientation(result))
      return true
    }

    return false
  }

  override def setDirection(direction: ForgeDirection) = world.setBlockMetadataWithNotify(xi, yi, zi, direction.ordinal, 3)

  /**
   * @author Based of Greg (GregTech)
   */
  def getSideToRotate(hitSide: Byte, hitX: Double, hitY: Double, hitZ: Double): Byte =
  {
    val tBack: Byte = (hitSide ^ 1).asInstanceOf[Byte]
    hitSide match
    {
      case 0 =>
      case 1 =>
        if (hitX < 0.25)
        {
          if (hitZ < 0.25)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (hitZ > 0.75)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (canRotate(4))
          {
            return 4
          }
        }
        if (hitX > 0.75)
        {
          if (hitZ < 0.25)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (hitZ > 0.75)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (canRotate(5))
          {
            return 5
          }
        }
        if (hitZ < 0.25)
        {
          if (canRotate(2))
          {
            return 2
          }
        }
        if (hitZ > 0.75)
        {
          if (canRotate(3))
          {
            return 3
          }
        }
        if (canRotate(hitSide))
        {
          return hitSide
        }
      case 2 =>
      case 3 =>
        if (hitX < 0.25)
        {
          if (hitY < 0.25)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (hitY > 0.75)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (canRotate(4))
          {
            return 4
          }
        }
        if (hitX > 0.75)
        {
          if (hitY < 0.25)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (hitY > 0.75)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (canRotate(5))
          {
            return 5
          }
        }
        if (hitY < 0.25)
        {
          if (canRotate(0))
          {
            return 0
          }
        }
        if (hitY > 0.75)
        {
          if (canRotate(1))
          {
            return 1
          }
        }
        if (canRotate(hitSide))
        {
          return hitSide
        }
      case 4 =>
      case 5 =>
        if (hitZ < 0.25)
        {
          if (hitY < 0.25)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (hitY > 0.75)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (canRotate(2))
          {
            return 2
          }
        }
        if (hitZ > 0.75)
        {
          if (hitY < 0.25)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (hitY > 0.75)
          {
            if (canRotate(tBack))
            {
              return tBack
            }
          }
          if (canRotate(3))
          {
            return 3
          }
        }
        if (hitY < 0.25)
        {
          if (canRotate(0))
          {
            return 0
          }
        }
        if (hitY > 0.75)
        {
          if (canRotate(1))
          {
            return 1
          }
        }
        if (canRotate(hitSide))
        {
          return hitSide
        }
    }
    return -1
  }
}