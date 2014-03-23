package calclavia.lib.render

import calclavia.lib.content.module.{TileBlock, TileRender}
import net.minecraft.client.renderer.RenderBlocks
import universalelectricity.api.vector.Vector3

/**
 * Created by Henry on 3/23/2014.
 */
class RotatedTextureRenderer(tile: TileBlock) extends TileRender
{
  override def renderStatic(renderer: RenderBlocks, position: Vector3): Boolean =
  {
    RenderUtility.rotateFacesOnRenderer(tile.getDirection, renderer, true)
    renderer.renderStandardBlock(tile.getBlockType, tile.xCoord, tile.yCoord, tile.zCoord)
    RenderUtility.resetFacesOnRenderer(renderer)
    return true;
  }
}
