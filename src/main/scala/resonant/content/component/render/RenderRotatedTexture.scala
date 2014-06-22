package resonant.content.component.render

import net.minecraft.client.renderer.RenderBlocks
import net.minecraftforge.common.util.ForgeDirection
import resonant.content.spatial.block.SpatialTile
import resonant.lib.render.{RenderBlockAdvanced, RenderUtility}
import resonant.lib.utility.RotationUtility
import universalelectricity.core.transform.vector.Vector3

/**
 * A rotated texture renderer applied to blocks capable of having rotated textured.
 * @author Calclavia
 */
class RenderRotatedTexture(tile: SpatialTile) extends RenderStatic
{
  val renderBlocks = new RenderBlockAdvanced()

  override def renderStatic(renderer: RenderBlocks, position: Vector3)
  {
    renderBlocks.setRenderBoundsFromBlock(tile.block)
    renderBlocks.blockAccess = tile.access

    val targetDir = tile.getDirection;

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      renderBlocks.limitedSide = dir.ordinal

      if ((0 until 4).exists(targetDir == ForgeDirection.getOrientation(RotationUtility.rotateSide(dir.ordinal, _))))
      {
        RenderUtility.rotateFacesOnRenderer(targetDir, renderBlocks, true)
        renderBlocks.renderStandardBlock(tile.block, position.xi, position.yi, position.zi())
        // RenderBlockUtility.tessellateFace(rendeerBlocks, tile.access, position.intX(), position.intY(), position.intZ(), tile.block, null, dir.ordinal)
        RenderUtility.resetFacesOnRenderer(renderBlocks)
      }
      else
      {
        renderBlocks.renderStandardBlock(tile.block, position.xi, position.yi, position.zi)
      }
    }
  }

}
