package com.builtbroken.mc.framework.multiblock;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.block.model.RenderStateBlockModel;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Dark on 8/18/2015.
 */
public class MultiBlockRenderHelper implements ISimpleBlockRenderingHandler
{
    public static final MultiBlockRenderHelper INSTANCE = new MultiBlockRenderHelper();
    public final int renderID;

    private MultiBlockRenderHelper()
    {
        renderID = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderBlocks)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileMulti)
        {
            TileMulti multi = ((TileMulti) tile);
            if (multi.shouldRenderBlock)
            {
                System.out.println("Rendering block " + x + "x " + y + "y " + z + "z ");
                block.setBlockBoundsBasedOnState(world, x, y, z);
                renderBlocks.setRenderBoundsFromBlock(block);
                return renderBlocks.renderStandardBlock(block, x, y, z);
            }
            else if (multi.renderID != null)
            {
                RenderData renderData = ClientDataHandler.INSTANCE.getRenderData(multi.renderID);
                if (renderData != null)
                {
                    IRenderState state = renderData.getState(multi.renderState);
                    if (state instanceof RenderStateBlockModel)
                    {
                        Block renderBlock = block;
                        int renderMeta = -1;
                        ItemStack itemStack = multi.getBlockToRender();

                        if(itemStack != null && itemStack.getItem() instanceof ItemBlock)
                        {
                            renderBlock = ((ItemBlock) itemStack.getItem()).field_150939_a;
                            renderMeta = itemStack.getItem().getMetadata(itemStack.getItemDamage());
                        }
                        return ((RenderStateBlockModel) state).render(world, x, y, z, renderBlock, renderMeta, renderBlocks);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return renderID;
    }
}
