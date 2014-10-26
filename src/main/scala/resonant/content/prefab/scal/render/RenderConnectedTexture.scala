package resonant.content.prefab

import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11._
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.render.RenderUtility
import resonant.lib.utility.WorldUtility
import resonant.lib.utility.render.RenderBlockUtility
import universalelectricity.core.transform.vector.Vector3

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
    glTranslated(0.5, 0.5, 0.5)
    RenderBlockUtility.tessellateBlockWithConnectedTextures(itemStack.getItemDamage(), tile.block, null, RenderUtility.getIcon(edgeTexture))
    glPopMatrix()
  }

  override def renderDynamic(pos: Vector3, frame: Float, pass: Int)
  {
    var sideMap = 0

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      val check = asVectorWorld + dir
      val checkTile = check.getTileEntity

      if (checkTile != null && checkTile.getClass == tile.getClass && check.getBlockMetadata(world) == tile.getBlockMetadata)
      {
        sideMap = WorldUtility.setEnableSide(sideMap, dir, true)
      }
    }

    RenderBlockUtility.tessellateBlockWithConnectedTextures(sideMap, world, xi, yi, zi, tile.getBlockType, null, RenderUtility.getIcon(edgeTexture))
  }
}
