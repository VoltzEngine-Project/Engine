package resonant.content.prefab

import net.minecraft.client.renderer.RenderBlocks
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.IRotatable
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.render.{RenderBlockAdvanced, RenderUtility}
import resonant.lib.utility.RotationUtility
import universalelectricity.core.transform.vector.Vector3

/**
 * A rotated texture renderer applied to blocks capable of having rotated textured.
 * @author Calclavia
 */
trait RenderRotatedTexture extends SpatialBlock with IRotatable
{
  val renderBlocks = new RenderBlockAdvanced()

  override def renderStatic(renderer: RenderBlocks, pos: Vector3, pass: Int): Boolean =
  {
    renderBlocks.setRenderBoundsFromBlock(block)
    renderBlocks.blockAccess = access

    val targetDir = getDirection()

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      renderBlocks.limitedSide = dir.ordinal

      if ((0 until 4).exists(targetDir.ordinal() == RotationUtility.rotateSide(dir.ordinal(), _)))
      {
        RenderUtility.rotateFacesOnRenderer(targetDir, renderBlocks, true)
        renderBlocks.renderStandardBlock(tile.block, position.xi, position.yi, position.zi)
        // RenderBlockUtility.tessellateFace(renderBlocks, tile.access, position.xi(), position.yi(), position.zi(), tile.block, null, dir.ordinal)
        RenderUtility.resetFacesOnRenderer(renderBlocks)
      }
      else
      {
        renderBlocks.renderStandardBlock(tile.block, position.xi, position.yi, position.zi)
      }
    }

    return true
  }

}
