package com.builtbroken.mc.framework.multiblock;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
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

        if (tile instanceof TileMulti && ((TileMulti) tile).shouldRenderBlock)
        {
            System.out.println("Rendering block " + x + "x " + y + "y " + z + "z ");
            block.setBlockBoundsBasedOnState(world, x, y, z);
            renderBlocks.setRenderBoundsFromBlock(block);
            return renderBlocks.renderStandardBlock(block, x, y, z);
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
