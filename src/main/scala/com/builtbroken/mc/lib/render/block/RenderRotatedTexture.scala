package com.builtbroken.mc.lib.render.block

import com.builtbroken.mc.prefab.tile.Tile
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.RenderBlocks
import net.minecraftforge.common.util.ForgeDirection
import com.builtbroken.mc.api.tile.IRotatable
import com.builtbroken.mc.lib.render.{RenderBlockAdvanced, RenderUtility}
import com.builtbroken.mc.lib.transform.vector.Pos
import com.builtbroken.mc.lib.helper.RotationUtility

/**
 * A rotated texture renderer applied to blocks capable of having rotated textured.
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
trait RenderRotatedTexture extends Tile with IRotatable
{
  @SideOnly(Side.CLIENT)
  var renderBlocks: RenderBlockAdvanced = null

  @SideOnly(Side.CLIENT)
  override def renderStatic(renderer: RenderBlocks, pos: Pos, pass: Int): Boolean =
  {
    if (renderBlocks == null)
      renderBlocks = new RenderBlockAdvanced()
    renderBlocks.setRenderBoundsFromBlock(getBlockType)
    renderBlocks.blockAccess = getAccess

    val targetDir = getDirection()

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      renderBlocks.limitedSide = dir.ordinal

      if ((0 until 4).exists(targetDir.ordinal() == RotationUtility.rotateSide(dir.ordinal(), _)))
      {
        RenderUtility.rotateFacesOnRenderer(targetDir, renderBlocks, true)
        renderBlocks.renderStandardBlock(getBlockType, xi, yi, zi)
        // RenderBlockUtility.tessellateFace(renderBlocks, tile.access, position.xi(), position.yi(), position.zi(), tile.block, null, dir.ordinal)
        RenderUtility.resetFacesOnRenderer(renderBlocks)
      }
      else
      {
        renderBlocks.renderStandardBlock(getBlockType, xi, yi, zi)
      }
    }

    return true
  }

}
