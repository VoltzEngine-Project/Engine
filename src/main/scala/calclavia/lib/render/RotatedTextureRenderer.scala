package calclavia.lib.render

import calclavia.lib.content.module.{ TileBlock, TileRender }
import net.minecraft.client.renderer.RenderBlocks
import universalelectricity.api.vector.Vector3
import net.minecraftforge.common.ForgeDirection
import calclavia.lib.utility.render.RenderBlockUtility
import calclavia.lib.utility.RotationUtility

/**
 * Created by Henry on 3/23/2014.
 */
class RotatedTextureRenderer(tile: TileBlock) extends TileRender {
  val renderBlocks = new RenderBlockAdvanced()

  override def renderStatic(renderer: RenderBlocks, position: Vector3): Boolean =
    {
      renderBlocks.setRenderBoundsFromBlock(tile.block)
      renderBlocks.blockAccess = tile.access

      val targetDir = tile.getDirection;

      for (dir <- ForgeDirection.VALID_DIRECTIONS) {
        renderBlocks.limitedSide = dir.ordinal

        if ((0 until 4).exists(r => targetDir == ForgeDirection.getOrientation(RotationUtility.rotateSide(dir.ordinal, r)))) {
          RenderUtility.rotateFacesOnRenderer(targetDir, renderBlocks, true)
          renderBlocks.renderStandardBlock(tile.block, position.intX(), position.intY(), position.intZ())
          // RenderBlockUtility.tessellateFace(rendeerBlocks, tile.access, position.intX(), position.intY(), position.intZ(), tile.block, null, dir.ordinal)
          RenderUtility.resetFacesOnRenderer(renderBlocks)
        } else {
          renderBlocks.renderStandardBlock(tile.block, position.intX(), position.intY(), position.intZ())
        }
      }

      return true
    }

}
