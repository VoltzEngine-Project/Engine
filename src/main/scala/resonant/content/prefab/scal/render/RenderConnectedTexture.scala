package resonant.content.prefab

import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11._
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.render.RenderUtility
import resonant.lib.transform.vector.Vector3
import resonant.lib.utility.render.RenderBlockUtility
import resonant.lib.wrapper.BitmaskWrapper._

/**
 * A generic TileEntity connected texture renderer.
 * Created by Calclavia on 3/22/2014.
 */
trait RenderConnectedTexture extends SpatialBlock
{
  val edgeTexture: String

  override def renderInventory(itemStack: ItemStack)
  {
    glPushMatrix()
    RenderBlockUtility.tessellateBlockWithConnectedTextures(itemStack.getItemDamage, tile.block, null, RenderUtility.getIcon(edgeTexture))
    glPopMatrix()
  }

  /**
   * Render the static, unmoving faces of this part into the world renderer.
   * The Tessellator is already drawing.
   * @return true if vertices were added to the tessellator
   */
  override def renderStatic(renderer: RenderBlocks, pos: Vector3, pass: Int): Boolean =
  {
    var sideMap = 0

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      val check = asVectorWorld + dir
      val checkTile = check.getTileEntity

      if (checkTile != null && checkTile.getClass == tile.getClass && check.getBlockMetadata(world) == tile.getBlockMetadata)
      {
        sideMap = sideMap.openMask(dir)
      }
    }

    RenderBlockUtility.tessellateBlockWithConnectedTextures(sideMap, world, pos.xi, pos.yi, pos.zi, tile.getBlockType, null, RenderUtility.getIcon(edgeTexture))
    return true
  }
}
