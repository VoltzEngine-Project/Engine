package resonant.lib.util

import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11
import resonant.lib.render.RenderUtility
import resonant.lib.util.render.RenderBlockUtility

/**
 * A generic TileEntity connected texture renderer.
 * Created by Calclavia on 3/22/2014.
 */
class ConnectedTextureRenderer(tile: TileBlock, edgeTexture: String) extends TileRender
{
  override def renderItem(itemStack: ItemStack): Boolean =
  {
    GL11.glPushMatrix()
    GL11.glTranslated(0.5, 0.5, 0.5)
    RenderBlockUtility.tessellateBlockWithConnectedTextures(itemStack.getItemDamage, tile.block, null, RenderUtility.getIcon(edgeTexture))
    GL11.glPopMatrix()
    return true
  }

  override def renderStatic(renderer: RenderBlocks, position: Vector3): Boolean =
  {
    val world = tile.getWorldObj
    val x = tile.xCoord
    val y = tile.yCoord
    val z = tile.zCoord
    var sideMap: Byte = 0

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      val check = new Vector3(tile).translate(dir)
      val checkTile = check.getTileEntity(world)

      if (checkTile != null && checkTile.getClass == tile.getClass && check.getBlockMetadata(world) == tile.getBlockMetadata)
      {
        sideMap = WorldUtility.setEnableSide(sideMap, dir, true)
      }
    }

    RenderBlockUtility.tessellateBlockWithConnectedTextures(sideMap, world, x, y, z, tile.getBlockType, null, RenderUtility.getIcon(edgeTexture))
    return true
  }
}
