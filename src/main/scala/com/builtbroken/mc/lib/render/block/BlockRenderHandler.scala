package com.builtbroken.mc.lib.render.block

import com.builtbroken.mc.prefab.tile.{Tile, BlockTile}
import com.builtbroken.mc.lib.transform.vector.Pos
import cpw.mods.fml.client.registry.{ISimpleBlockRenderingHandler, RenderingRegistry}
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.item.ItemStack
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._

/**
 * Created by robert on 1/5/2015.
 */
object BlockRenderHandler extends ISimpleBlockRenderingHandler
{
  val ID = RenderingRegistry.getNextAvailableRenderId()

  def renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks)
  {
    if (block.isInstanceOf[BlockTile])
    {
      val tile = (block.asInstanceOf[BlockTile]).staticTile

      glEnable(GL_RESCALE_NORMAL)
      glPushAttrib(GL_TEXTURE_BIT)
      glPushMatrix()
      tile.renderInventory(new ItemStack(block, 1, metadata))
      glPopMatrix()
      glPopAttrib()
    }
  }

  def renderWorldBlock(access: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderBlocks: RenderBlocks): Boolean =
  {
    var renderer: Tile = null

    /**
     * Try TileEntity rendering
     */
    val tile = access.getTileEntity(x, y, z)

    if (tile != null && tile.isInstanceOf[Tile])
    {
      renderer = tile.asInstanceOf[Tile]
    }

    /**
     * Try Block rendering
     */
    if (renderer == null && block.isInstanceOf[BlockTile])
    {
      val dummy = block.asInstanceOf[BlockTile]
      dummy.inject(access, x, y, z)
      renderer = dummy.getTile(access, x, y, z)
      dummy.eject()
    }

    if (renderer != null)
    {
      return renderer.renderStatic(renderBlocks, new Pos(x, y, z), 0);
    }

    return false
  }

  def shouldRender3DInInventory(modelId: Int) = true

  def getRenderId() = BlockRenderHandler.ID

}
