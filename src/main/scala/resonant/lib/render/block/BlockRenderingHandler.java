package resonant.lib.render.block;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import resonant.api.items.ISimpleItemRenderer;
import resonant.lib.content.module.BlockDummy;
import resonant.lib.content.module.TileBlock;
import resonant.lib.render.RenderUtility;
import universalelectricity.api.vector.Vector3;

import com.google.common.collect.Maps;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockRenderingHandler implements ISimpleBlockRenderingHandler
{
    public static final BlockRenderingHandler INSTANCE = new BlockRenderingHandler();
    public static final int ID = RenderingRegistry.getNextAvailableRenderId();

    /** Maps fake TileEntities */
    public static final Map<Block, TileEntity> inventoryTileEntities = Maps.newIdentityHashMap();

    public TileEntity getTileEntityForBlock(Block block)
    {
        TileEntity te = inventoryTileEntities.get(block);
        if (te == null)
        {
            te = block.createTileEntity(Minecraft.getMinecraft().thePlayer.getEntityWorld(), 0);
            inventoryTileEntities.put(block, te);
        }
        return te;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        if (block instanceof BlockDummy)
        {
            TileBlock tile = ((BlockDummy) block).dummyTile;

            if (tile != null)
            {
                boolean didRender = true;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
                GL11.glPushMatrix();
                GL11.glTranslated(-0.5, -0.5, -0.5);

                if (tile.getRenderer() != null)
                {
                    if (!tile.getRenderer().renderItem(new ItemStack(block, 1, metadata)))
                    {
                        if (!tile.getRenderer().renderDynamic(new Vector3(), true, 0))
                        {
                            if (!tile.customItemRender)
                            {
                                GL11.glTranslated(0.5, 0.5, 0.5);
                                RenderUtility.renderNormalBlockAsItem(block, metadata, renderer);
                            }
                            else
                            {
                                didRender = false;
                            }
                        }
                    }
                }
                else if (!tile.customItemRender)
                {
                    GL11.glTranslated(0.5, 0.5, 0.5);
                    RenderUtility.renderNormalBlockAsItem(block, metadata, renderer);
                }
                else
                {
                    didRender = false;
                }

                GL11.glPopMatrix();
                GL11.glPopAttrib();

                if (didRender)
                {
                    return;
                }
            }
        }

        TileEntity renderTile = null;

        if (block.hasTileEntity(metadata))
        {
            renderTile = getTileEntityForBlock(block);
        }

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        if (renderTile != null)
        {
            GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
            GL11.glPushMatrix();
            GL11.glTranslated(-0.5, -0.5, -0.5);
            TileEntitySpecialRenderer tesr = TileEntityRenderer.instance.getSpecialRendererForEntity(renderTile);

            try
            {
                if (tesr instanceof ISimpleItemRenderer)
                {
                    ((ISimpleItemRenderer) tesr).renderInventoryItem(new ItemStack(block, 1, metadata));
                }
                else if (tesr != null)
                {
                    tesr.renderTileEntityAt(renderTile, 0, 0, 0, 0);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess access, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (block instanceof BlockDummy)
        {
            BlockDummy dummy = (BlockDummy) block;
            dummy.inject(access, x, y, z);
            TileBlock tileBlock = dummy.getTile(access, x, y, z);

            if (tileBlock != null)
            {
                tileBlock.access = access;

                if (tileBlock.getRenderer() != null)
                {
                    if (!tileBlock.getRenderer().renderStatic(renderer, new Vector3(x, y, z)))
                    {
                        renderer.renderStandardBlock(block, x, y, z);
                    }
                }
                else if (tileBlock.normalRender || tileBlock.forceStandardRender)
                {
                    renderer.renderStandardBlock(block, x, y, z);
                }

            }

            //dummy.eject();
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return ID;
    }
}