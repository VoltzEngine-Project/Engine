package resonant.lib.render.block

import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11._
import resonant.lib.prefab.tile.spatial.SpatialBlock
import resonant.lib.render.{RenderBlockUtility, RenderUtility}
import resonant.lib.transform.vector.Vector3
import resonant.lib.wrapper.BitmaskWrapper._

/**
 * A generic TileEntity connected texture renderer.
 * Created by Calclavia on 3/22/2014.
 */
trait RenderConnectedTexture extends SpatialBlock
{
  var faceTexture: String = null
  var edgeTexture: String = null

  override def renderInventory(itemStack: ItemStack)
  {
    glPushMatrix()
    RenderBlockUtility.tessellateBlockWithConnectedTextures(itemStack.getItemDamage, tile.block, if(faceTexture != null) RenderUtility.getIcon(faceTexture) else null, RenderUtility.getIcon(edgeTexture))
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
      val check = toVectorWorld + dir
      val checkTile = check.getTileEntity

      if (checkTile != null && checkTile.getClass == tile.getClass && check.getBlockMetadata(world) == tile.getBlockMetadata)
      {
        sideMap = sideMap.openMask(dir)
      }
    }

    RenderBlockUtility.tessellateBlockWithConnectedTextures(sideMap, world, pos.xi, pos.yi, pos.zi, tile.getBlockType, if(faceTexture != null) RenderUtility.getIcon(faceTexture) else null, RenderUtility.getIcon(edgeTexture))
    return true
  }
}
