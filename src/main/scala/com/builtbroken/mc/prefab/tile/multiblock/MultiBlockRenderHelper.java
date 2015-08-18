package com.builtbroken.mc.prefab.tile.multiblock;

import com.builtbroken.mc.lib.transform.region.Cube;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

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
            GL11.glPushMatrix();
            Tessellator t = Tessellator.instance;

            GL11.glColor4f(1, 1, 1, 1);

            if (((TileMulti) tile).overrideRenderBounds != null)
            {
                final Cube c = ((TileMulti) tile).overrideRenderBounds;
                renderBlocks.setRenderBounds(c.min().x(), c.min().y(), c.min().z(), c.max().x(), c.max().y(), c.max().z());
            }
            else
            {
                renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);
            }

            t.startDrawingQuads();

            IIcon useTexture = block.getIcon(world, x, y, z, 0);
            t.setNormal(0.0F, -1.0F, 0.0F);
            renderBlocks.renderFaceYNeg(block, 0, 0, 0, useTexture);

            useTexture = block.getIcon(world, x, y, z, 1);
            t.setNormal(0.0F, 1.0F, 0.0F);
            renderBlocks.renderFaceYPos(block, 0, 0, 0, useTexture);

            useTexture = block.getIcon(world, x, y, z, 2);
            t.setNormal(0.0F, 0.0F, -1.0F);
            renderBlocks.renderFaceZNeg(block, 0, 0, 0, useTexture);

            useTexture = block.getIcon(world, x, y, z, 3);
            t.setNormal(0.0F, 0.0F, 1.0F);
            renderBlocks.renderFaceZPos(block, 0, 0, 0, useTexture);

            useTexture = block.getIcon(world, x, y, z, 4);
            t.setNormal(-1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXNeg(block, 0, 0, 0, useTexture);

            useTexture = block.getIcon(world, x, y, z, 5);
            t.setNormal(1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXPos(block, 0, 0, 0, useTexture);
            t.draw();

            GL11.glPopMatrix();
            return true;
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
