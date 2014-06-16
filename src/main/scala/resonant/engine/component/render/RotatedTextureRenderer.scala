package resonant.engine.component.render

import resonant.lib.utility.RotationUtility
import universalelectricity.core.transform.vector.Vector3
import resonant.lib.render.{RenderUtility, RenderBlockAdvanced}
import resonant.engine.spatial.block.TileBlock
import net.minecraft.client.renderer.RenderBlocks

/**
 *
 * @author Calclavia
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
