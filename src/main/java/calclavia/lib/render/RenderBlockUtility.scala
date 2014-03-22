package calclavia.lib.render

import calclavia.lib.utility.{RotationUtility, WorldUtility}
import net.minecraft.client.renderer.{RenderBlocks, Tessellator, OpenGlHelper}
import net.minecraftforge.common.ForgeDirection
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.block.Block
import net.minecraft.util.Icon

object RenderBlockUtility
{
  def setupLight(world: World, x: Int, y: Int, z: Int)
  {
    if (world.isBlockOpaqueCube(x, y, z))
    {
      return
    }
    val br: Int = world.getLightBrightnessForSkyBlocks(x, y, z, 0)
    val var11: Int = br % 65536
    val var12: Int = br / 65536
    val scale: Float = 1
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var11 * scale, var12 * scale)
  }

  def tessellateFace(renderBlocks: RenderBlocks, x: Int, y: Int, z: Int, block: Block, overrideTexture: Icon, side: Int)
  {
    val t: Tessellator = Tessellator.instance
    var useTexture: Icon = null
    val meta: Int = 0
    if (side == 0)
    {
      useTexture = if (overrideTexture != null) overrideTexture else block.getIcon(0, meta)
      t.setNormal(0.0F, -1.0F, 0.0F)
      renderBlocks.renderFaceYNeg(block, x, y, z, useTexture)
    }
    if (side == 1)
    {
      useTexture = if (overrideTexture != null) overrideTexture else block.getIcon(1, meta)
      t.setNormal(0.0F, 1.0F, 0.0F)
      renderBlocks.renderFaceYPos(block, x, y, z, useTexture)
    }
    if (side == 2)
    {
      useTexture = if (overrideTexture != null) overrideTexture else block.getIcon(2, meta)
      t.setNormal(0.0F, 0.0F, -1.0F)
      renderBlocks.renderFaceZNeg(block, x, y, z, useTexture)
    }
    if (side == 3)
    {
      useTexture = if (overrideTexture != null) overrideTexture else block.getIcon(3, meta)
      t.setNormal(0.0F, 0.0F, 1.0F)
      renderBlocks.renderFaceZPos(block, x, y, z, useTexture)
    }
    if (side == 4)
    {
      useTexture = if (overrideTexture != null) overrideTexture else block.getIcon(4, meta)
      t.setNormal(-1.0F, 0.0F, 0.0F)
      renderBlocks.renderFaceXNeg(block, x, y, z, useTexture)
    }
    if (side == 5)
    {
      useTexture = if (overrideTexture != null) overrideTexture else block.getIcon(5, meta)
      t.setNormal(1.0F, 0.0F, 0.0F)
      renderBlocks.renderFaceXPos(block, x, y, z, useTexture)
    }
  }

  def tessellateBlockWithConnectedTextures(sideMap: Byte, blockAccess: IBlockAccess, x: Int, y: Int, z: Int, block: Block, faceOverride: Icon, edgeOverride: Icon)
  {
    val renderBlocks = RenderUtility.renderBlocks
    renderBlocks.blockAccess = blockAccess
    renderBlocks.overrideBlockTexture = faceOverride
    renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1)
    renderBlocks.renderStandardBlock(block, x, y, z)

    for (dir <- ForgeDirection.VALID_DIRECTIONS; r <- 0 until 4)
    {
      val absDir = ForgeDirection.getOrientation(RotationUtility.rotateSide(dir.ordinal, r))

      if (!WorldUtility.isEnabledSide(sideMap, absDir))
      {
        RenderUtility.rotateFacesOnRenderer(absDir, renderBlocks, true);
        tessellateFace(renderBlocks, x, y, z, block, edgeOverride, dir.ordinal)
        RenderUtility.resetFacesOnRenderer(renderBlocks);
      }
    }
  }
}