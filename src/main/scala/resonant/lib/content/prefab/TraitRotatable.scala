package resonant.lib.content.prefab

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.ForgeDirection
import resonant.api.IRotatable

trait TraitRotatable extends TileEntity with IRotatable {
  protected var rotationMask: Byte = 0x3C

  def getDirection: ForgeDirection = ForgeDirection.getOrientation(getBlockMetadata)

  def setDirection(direction: ForgeDirection) = getWorldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, getDirection.ordinal, 3)

  def canRotate(ord: Int): Boolean = (rotationMask & (1 << ord)) != 0

  /**
   * @author Based of Greg (GregTech)
   */
  def getSideToRotate(hitSide: Byte, hitX: Double, hitY: Double, hitZ: Double): Byte =
    {
      val tBack: Byte = (hitSide ^ 1).asInstanceOf[Byte]
      hitSide match {
        case 0 =>
        case 1 =>
          if (hitX < 0.25) {
            if (hitZ < 0.25) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (hitZ > 0.75) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (canRotate(4)) {
              return 4
            }
          }
          if (hitX > 0.75) {
            if (hitZ < 0.25) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (hitZ > 0.75) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (canRotate(5)) {
              return 5
            }
          }
          if (hitZ < 0.25) {
            if (canRotate(2)) {
              return 2
            }
          }
          if (hitZ > 0.75) {
            if (canRotate(3)) {
              return 3
            }
          }
          if (canRotate(hitSide)) {
            return hitSide
          }
        case 2 =>
        case 3 =>
          if (hitX < 0.25) {
            if (hitY < 0.25) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (hitY > 0.75) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (canRotate(4)) {
              return 4
            }
          }
          if (hitX > 0.75) {
            if (hitY < 0.25) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (hitY > 0.75) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (canRotate(5)) {
              return 5
            }
          }
          if (hitY < 0.25) {
            if (canRotate(0)) {
              return 0
            }
          }
          if (hitY > 0.75) {
            if (canRotate(1)) {
              return 1
            }
          }
          if (canRotate(hitSide)) {
            return hitSide
          }
        case 4 =>
        case 5 =>
          if (hitZ < 0.25) {
            if (hitY < 0.25) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (hitY > 0.75) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (canRotate(2)) {
              return 2
            }
          }
          if (hitZ > 0.75) {
            if (hitY < 0.25) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (hitY > 0.75) {
              if (canRotate(tBack)) {
                return tBack
              }
            }
            if (canRotate(3)) {
              return 3
            }
          }
          if (hitY < 0.25) {
            if (canRotate(0)) {
              return 0
            }
          }
          if (hitY > 0.75) {
            if (canRotate(1)) {
              return 1
            }
          }
          if (canRotate(hitSide)) {
            return hitSide
          }
      }
      return -1
    }
}