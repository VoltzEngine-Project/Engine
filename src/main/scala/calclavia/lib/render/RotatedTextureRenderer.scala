package calclavia.lib.render

import calclavia.lib.content.module.{TileBlock, TileRender}
import net.minecraft.client.renderer.RenderBlocks
import universalelectricity.api.vector.Vector3
import net.minecraftforge.common.ForgeDirection
import calclavia.lib.utility.RotationUtility
import calclavia.lib.utility.render.RenderBlockUtility

/**
 * Created by Henry on 3/23/2014.
 */
class RotatedTextureRenderer(tile: TileBlock) extends TileRender
{
  override def renderStatic(renderer: RenderBlocks, position: Vector3): Boolean =
  {
    renderer.setRenderBoundsFromBlock(tile.block)

    for (dir <- ForgeDirection.VALID_DIRECTIONS; r <- 0 until 4)
    {
      val absDir = ForgeDirection.getOrientation(RotationUtility.rotateSide(dir.ordinal, r))

      if (absDir == tile.getDirection)
      {
        RenderUtility.rotateFacesOnRenderer(absDir, renderer, true)
        RenderBlockUtility.tessellateFace(renderer, tile.access, tile.x, tile.y, tile.z, tile.block, null, dir.ordinal)
        RenderUtility.resetFacesOnRenderer(renderer)
      }
    }

    return false
  }

}
