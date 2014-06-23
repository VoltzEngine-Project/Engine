package resonant.content.wrapper

import cpw.mods.fml.client.registry.{RenderingRegistry, ISimpleBlockRenderingHandler}
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.item.ItemStack
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11._
import resonant.content.component.render.{RenderItem, RenderStatic}
import resonant.lib.content.module.BlockDummy
import universalelectricity.core.transform.vector.Vector3

class BlockRenderingHandler extends ISimpleBlockRenderingHandler
{
  val ID = RenderingRegistry.getNextAvailableRenderId()

  def getTileEntityForBlock(block: Nothing): Nothing =
  {
    var te: Nothing = inventoryTileEntities.get(block)
    if (te == null)
    {
      te = block.createTileEntity(Minecraft.getMinecraft.thePlayer.getEntityWorld, 0)
      inventoryTileEntities.put(block, te)
    }
    return te
  }

  def renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: Nothing)
  {
    if (block.isInstanceOf[BlockDummy])
    {
      val tile = (block.asInstanceOf[BlockDummy]).dummyTile

      glEnable(GL_RESCALE_NORMAL)
      glPushAttrib(GL_TEXTURE_BIT)
      glPushMatrix
      glTranslated(-0.5, -0.5, -0.5)
      tile.components.get(classOf[RenderItem]).foreach(_.renderItem(new ItemStack(block, 1, metadata)))
      glPopMatrix
      glPopAttrib
    }
  }

  def renderWorldBlock(access: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean =
  {
    if (block.isInstanceOf[BlockDummy])
    {
      val dummy = block.asInstanceOf[BlockDummy]
      dummy.inject(access, x, y, z)
      val tile = dummy.getTile(access, x, y, z)

      if (tile != null)
      {
        tile.components.get(classOf[RenderStatic]).foreach(_.renderStatic(access, block, renderer, new Vector3(x, y, z)))
      }

      return true
    }
    return false
  }

  def shouldRender3DInInventory: Boolean =
  {
    return true
  }

  def getRenderId: Int =
  {
    return ID
  }
}