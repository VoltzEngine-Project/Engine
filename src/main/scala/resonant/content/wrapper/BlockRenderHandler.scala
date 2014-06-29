package resonant.content.wrapper

import cpw.mods.fml.client.registry.{ISimpleBlockRenderingHandler, RenderingRegistry}
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.item.ItemStack
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import resonant.content.spatial.block.SpatialBlock
import universalelectricity.core.transform.vector.Vector3

object BlockRenderHandler extends ISimpleBlockRenderingHandler
{
  val ID = RenderingRegistry.getNextAvailableRenderId()

  def renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks)
  {
    if (block.isInstanceOf[BlockDummy])
    {
      val tile = (block.asInstanceOf[BlockDummy]).dummyTile

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
    var renderer: SpatialBlock = null

    /**
     * Try TileEntity rendering
     */
    val tile = access.getTileEntity(x, y, z)

    if (tile.isInstanceOf[SpatialBlock])
    {
      val spatial = tile.asInstanceOf[SpatialBlock]
      renderer = spatial.tile
    }

    /**
     * Try Block rendering
     */
    if (renderer == null && block.isInstanceOf[BlockDummy])
    {
      val dummy = block.asInstanceOf[BlockDummy]
      dummy.inject(access, x, y, z)
      renderer = dummy.getTile(access, x, y, z)
    }

    if (renderer != null)
    {
      return renderer.renderStatic(renderBlocks, new Vector3(x, y, z), 0);
    }

    return false
  }

  def shouldRender3DInInventory(modelId: Int) = true

  def getRenderId() = BlockRenderHandler.ID

}