package com.builtbroken.mc.lib.render.block;

import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.BlockTile;
import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by robert on 1/16/2015.
 */
@Deprecated
public class BlockRenderHandler implements ISimpleBlockRenderingHandler
{
    public final static int ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        if (block instanceof BlockTile)
        {
            Tile tile = ((BlockTile) block).staticTile;

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
            GL11.glPushMatrix();
            tile.renderInventory(new ItemStack(block, 1, metadata));
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess access, int x, int y, int z, Block block, int modelId, RenderBlocks renderBlocks)
    {
        /**
         * Try TileEntity rendering
         */
        TileEntity tile = access.getTileEntity(x, y, z);

        if (tile instanceof Tile)
        {
            if(((Tile) tile).renderStatic(renderBlocks, new Pos(x, y, z), 0))
            {
                return true;
            }
        }

        /**
         * Try Block rendering
         */
        if (block instanceof BlockTile)
        {
            BlockTile dummy = (BlockTile) block;
            tile = dummy.inject(access, x, y, z);
            boolean b = ((Tile) tile).renderStatic(renderBlocks, new Pos(x, y, z), 0);
            dummy.eject();
            return b;
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
        return BlockRenderHandler.ID;
    }

}