package resonant.lib.utility.render

import net.minecraft.block.Block
import net.minecraft.client.renderer.{OpenGlHelper, RenderBlocks, Tessellator}
import net.minecraft.util.IIcon
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11
import resonant.lib.render.RenderUtility
import resonant.lib.utility.{RotationUtility, WorldUtility}

/**
 * A block rendering helper class.
 */
object RenderBlockUtility
{
  def setupLight(world: World, x: Int, y: Int, z: Int)
  {
    if (world.getBlock(x, y, z).isOpaqueCube())
    {
      return
    }
    val br: Int = world.getLightBrightnessForSkyBlocks(x, y, z, 0)
    val var11: Int = br % 65536
    val var12: Int = br / 65536
    val scale: Float = 1
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var11 * scale, var12 * scale)
  }

  /**
   * Renders a connected texture block with a bitmask
   * @param sideMap - The sides that are connected
   */
  def tessellateBlockWithConnectedTextures(sideMap: Int, blockAccess: IBlockAccess, x: Int, y: Int, z: Int, block: Block, faceOverride: IIcon, edgeOverride: IIcon)
  {
    tessellateBlockWithConnectedTextures(sideMap, RenderUtility.renderBlocks, blockAccess, x, y, z, block, faceOverride, edgeOverride)
  }

  def tessellateBlockWithConnectedTextures(sideMap: Int, renderBlocks: RenderBlocks, blockAccess: IBlockAccess, x: Int, y: Int, z: Int, block: Block, faceOverride: IIcon, edgeOverride: IIcon)
  {
    renderBlocks.blockAccess = blockAccess

    if (faceOverride != null)
      renderBlocks.setOverrideBlockTexture(faceOverride)

    renderBlocks.setRenderBoundsFromBlock(block)
    renderBlocks.renderStandardBlock(block, x, y, z)

    for (dir <- ForgeDirection.VALID_DIRECTIONS; r <- 0 until 4)
    {
      if (!WorldUtility.isEnabledSide(sideMap, dir))
      {
        val absDir = ForgeDirection.getOrientation(RotationUtility.rotateSide(dir.ordinal, r))

        if (!WorldUtility.isEnabledSide(sideMap, absDir))
        {
          RenderUtility.rotateFacesOnRenderer(absDir, renderBlocks, true)
          tessellateFace(renderBlocks, blockAccess, x, y, z, block, edgeOverride, dir.ordinal)
          RenderUtility.resetFacesOnRenderer(renderBlocks)
        }
      }
    }

    renderBlocks.clearOverrideBlockTexture()
  }

  def tessellateFace(renderBlocks: RenderBlocks, access: IBlockAccess, x: Int, y: Int, z: Int, block: Block, overrideTexture: IIcon, side: Int)
  {
    val tessellator = Tessellator.instance
    val useTexture: IIcon = if (overrideTexture != null) overrideTexture else block.getIcon(access, x, y, z, side)

    if (side == 0)
    {
      tessellator.setNormal(0.0F, -1.0F, 0.0F)
      renderBlocks.renderFaceYNeg(block, x, y, z, useTexture)
    }
    if (side == 1)
    {
      tessellator.setNormal(0.0F, 1.0F, 0.0F)
      renderBlocks.renderFaceYPos(block, x, y, z, useTexture)
    }
    if (side == 2)
    {
      tessellator.setNormal(0.0F, 0.0F, -1.0F)
      renderBlocks.renderFaceZNeg(block, x, y, z, useTexture)
    }
    if (side == 3)
    {
      tessellator.setNormal(0.0F, 0.0F, 1.0F)
      renderBlocks.renderFaceZPos(block, x, y, z, useTexture)
    }
    if (side == 4)
    {
      tessellator.setNormal(-1.0F, 0.0F, 0.0F)
      renderBlocks.renderFaceXNeg(block, x, y, z, useTexture)
    }
    if (side == 5)
    {
      tessellator.setNormal(1.0F, 0.0F, 0.0F)
      renderBlocks.renderFaceXPos(block, x, y, z, useTexture)
    }
  }

  /**
   * For rendering items.
   */
  def tessellateBlockWithConnectedTextures(metadata: Int, block: Block, faceOverride: IIcon, edgeOverride: IIcon)
  {
    GL11.glPushMatrix()
    val renderBlocks = RenderUtility.renderBlocks
    renderBlocks.setOverrideBlockTexture(faceOverride)
    GL11.glPushMatrix()
    GL11.glScaled(0.999, 0.999, 0.999)
    RenderUtility.renderNormalBlockAsItem(block, metadata, renderBlocks)
    Tessellator.instance.startDrawingQuads()
    GL11.glPopMatrix()
    GL11.glTranslated(-0.5, -0.5, -0.5)

    for (dir <- ForgeDirection.VALID_DIRECTIONS; r <- 0 until 4)
    {
      val absDir = ForgeDirection.getOrientation(RotationUtility.rotateSide(dir.ordinal, r))
      RenderUtility.rotateFacesOnRenderer(absDir, renderBlocks, true)
      tessellateFace(renderBlocks, block, edgeOverride, dir.ordinal)
      RenderUtility.resetFacesOnRenderer(renderBlocks)
    }

    Tessellator.instance.draw()
    GL11.glPopMatrix()
  }

  def tessellateFace(renderBlocks: RenderBlocks, block: Block, overrideTexture: IIcon, side: Int)
  {
    val tessellator = Tessellator.instance
    val meta = 0
    val useTexture: IIcon = if (overrideTexture != null) overrideTexture else block.getIcon(side, meta)

    if (side == 0)
    {
      tessellator.setNormal(0.0F, -1.0F, 0.0F)
      renderBlocks.renderFaceYNeg(block, 0, 0, 0, useTexture)
    }
    if (side == 1)
    {
      tessellator.setNormal(0.0F, 1.0F, 0.0F)
      renderBlocks.renderFaceYPos(block, 0, 0, 0, useTexture)
    }
    if (side == 2)
    {
      tessellator.setNormal(0.0F, 0.0F, -1.0F)
      renderBlocks.renderFaceZNeg(block, 0, 0, 0, useTexture)
    }
    if (side == 3)
    {
      tessellator.setNormal(0.0F, 0.0F, 1.0F)
      renderBlocks.renderFaceZPos(block, 0, 0, 0, useTexture)
    }
    if (side == 4)
    {
      tessellator.setNormal(-1.0F, 0.0F, 0.0F)
      renderBlocks.renderFaceXNeg(block, 0, 0, 0, useTexture)
    }
    if (side == 5)
    {
      tessellator.setNormal(1.0F, 0.0F, 0.0F)
      renderBlocks.renderFaceXPos(block, 0, 0, 0, useTexture)
    }
  }

}