package resonant.lib.render.block

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.RenderBlocks
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.tile.IRotatable
import resonant.lib.prefab.tile.spatial.SpatialBlock
import resonant.lib.render.{RenderBlockAdvanced, RenderUtility}
import resonant.lib.transform.vector.Vector3
import resonant.lib.utility.RotationUtility

/**
 * A rotated texture renderer applied to blocks capable of having rotated textured.
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
trait RenderRotatedTexture extends SpatialBlock with IRotatable
{
  @SideOnly(Side.CLIENT)
  var renderBlocks: RenderBlockAdvanced = null

  @SideOnly(Side.CLIENT)
  override def renderStatic(renderer: RenderBlocks, pos: Vector3, pass: Int): Boolean =
  {
    if (renderBlocks == null)
      renderBlocks = new RenderBlockAdvanced()
    renderBlocks.setRenderBoundsFromBlock(block)
    renderBlocks.blockAccess = access

    val targetDir = getDirection()

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      renderBlocks.limitedSide = dir.ordinal

      if ((0 until 4).exists(targetDir.ordinal() == RotationUtility.rotateSide(dir.ordinal(), _)))
      {
        RenderUtility.rotateFacesOnRenderer(targetDir, renderBlocks, true)
        renderBlocks.renderStandardBlock(tile.block, xi, yi, zi)
        // RenderBlockUtility.tessellateFace(renderBlocks, tile.access, position.xi(), position.yi(), position.zi(), tile.block, null, dir.ordinal)
        RenderUtility.resetFacesOnRenderer(renderBlocks)
      }
      else
      {
        renderBlocks.renderStandardBlock(tile.block, xi, yi, zi)
      }
    }

    return true
  }

}
