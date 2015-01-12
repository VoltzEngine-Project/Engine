package com.builtbroken.mc.lib.render.block

import com.builtbroken.mc.prefab.tile.Tile
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11._
import com.builtbroken.mc.lib.render.{RenderBlockUtility, RenderUtility}
import com.builtbroken.mc.lib.transform.vector.Pos
import com.builtbroken.mc.lib.helper.wrapper.BitmaskWrapper._

/**
 * A generic TileEntity connected texture renderer.
 * Created by Calclavia on 3/22/2014.
 */
trait RenderConnectedTexture extends Tile
{
  var faceTexture: String = null
  var edgeTexture: String = null

  override def renderInventory(itemStack: ItemStack)
  {
    glPushMatrix()
    RenderBlockUtility.tessellateBlockWithConnectedTextures(itemStack.getItemDamage, getBlockType, if(faceTexture != null) RenderUtility.getIcon(faceTexture) else null, RenderUtility.getIcon(edgeTexture))
    glPopMatrix()
  }

  /**
   * Render the static, unmoving faces of this part into the world renderer.
   * The Tessellator is already drawing.
   * @return true if vertices were added to the tessellator
   */
  override def renderStatic(renderer: RenderBlocks, pos: Pos, pass: Int): Boolean =
  {
    var sideMap = 0

    for (dir <- ForgeDirection.VALID_DIRECTIONS)
    {
      val check = toVectorWorld + dir
      val checkTile = check.getTileEntity

      if (checkTile != null && checkTile.getClass == getClass && check.getBlockMetadata(world) == getBlockMetadata)
      {
        sideMap = sideMap.openMask(dir)
      }
    }

    RenderBlockUtility.tessellateBlockWithConnectedTextures(sideMap, world, pos.xi, pos.yi, pos.zi, getBlockType, if(faceTexture != null) RenderUtility.getIcon(faceTexture) else null, RenderUtility.getIcon(edgeTexture))
    return true
  }
}
