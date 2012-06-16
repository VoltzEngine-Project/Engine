package net.minecraft.src.universalelectricity;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

public class UERenderBlocks extends RenderBlocks
{
    /**
     * Is called to render the image of a block on an inventory, as a held item, or as a an item on the ground
     */
    public void renderBlockWithMetadata(IBlockAccess iBlockAccess, Block par1Block, int x, int y, int z)
    {
        Tessellator var4 = Tessellator.instance;
        int var10;
        par1Block.setBlockBoundsForItemRender();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        var4.startDrawingQuads();
        var4.setNormal(0.0F, -1.0F, 0.0F);
        this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTexture(iBlockAccess, x, y, z, 0));
        var4.draw();

        var4.startDrawingQuads();
        var4.setNormal(0.0F, 1.0F, 0.0F);
        this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTexture(iBlockAccess, x, y, z, 1));
        var4.draw();

        var4.startDrawingQuads();
        var4.setNormal(0.0F, 0.0F, -1.0F);
        this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTexture(iBlockAccess, x, y, z, 2));
        var4.draw();
        
        var4.startDrawingQuads();
        var4.setNormal(0.0F, 0.0F, 1.0F);
        this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTexture(iBlockAccess, x, y, z, 3));
        var4.draw();
        
        var4.startDrawingQuads();
        var4.setNormal(-1.0F, 0.0F, 0.0F);
        this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTexture(iBlockAccess, x, y, z, 4));
        var4.draw();
        
        var4.startDrawingQuads();
        var4.setNormal(1.0F, 0.0F, 0.0F);
        this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTexture(iBlockAccess, x, y, z, 5));
        var4.draw();
        
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
    
    /**
     * Is called to render the image of a block on an inventory, as a held item, or as a an item on the ground
     */
    public void renderBlockAsItemWithMetadata(Block par1Block, int metadata)
    {
        Tessellator var4 = Tessellator.instance;

        par1Block.setBlockBoundsForItemRender();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        var4.startDrawingQuads();
        var4.setNormal(0.0F, -1.0F, 0.0F);
        this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(0, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(0.0F, 1.0F, 0.0F);
        this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(1, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(0.0F, 0.0F, -1.0F);
        this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(2, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(0.0F, 0.0F, 1.0F);
        this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(3, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(-1.0F, 0.0F, 0.0F);
        this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(4, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(1.0F, 0.0F, 0.0F);
        this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(5, metadata));
        var4.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
    
    /**
     * Is called to render the image of a block on an inventory, as a held item, or as a an item on the ground
     */
    public void renderBlockAsItemWithMetadata(Block par1Block, int par2, float par3, int metadata)
    {
        Tessellator var4 = Tessellator.instance;
        boolean var5 = par1Block.blockID == Block.grass.blockID;
        int var6;
        float var7;
        float var8;
        float var9;

        if (this.useInventoryTint)
        {
            var6 = par1Block.getRenderColor(par2);

            if (var5)
            {
                var6 = 16777215;
            }

            var7 = (var6 >> 16 & 255) / 255.0F;
            var8 = (var6 >> 8 & 255) / 255.0F;
            var9 = (var6 & 255) / 255.0F;
            GL11.glColor4f(var7 * par3, var8 * par3, var9 * par3, 1.0F);
        }

        var6 = par1Block.getRenderType();
        int var10;
        par1Block.setBlockBoundsForItemRender();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        var4.startDrawingQuads();
        var4.setNormal(0.0F, -1.0F, 0.0F);
        this.renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(0, metadata));
        var4.draw();

        if (var5 && this.useInventoryTint)
        {
            var10 = par1Block.getRenderColor(par2);
            var8 = (var10 >> 16 & 255) / 255.0F;
            var9 = (var10 >> 8 & 255) / 255.0F;
            float var11 = (var10 & 255) / 255.0F;
            GL11.glColor4f(var8 * par3, var9 * par3, var11 * par3, 1.0F);
        }

        var4.startDrawingQuads();
        var4.setNormal(0.0F, 1.0F, 0.0F);
        this.renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(1, metadata));
        var4.draw();

        if (var5 && this.useInventoryTint)
        {
            GL11.glColor4f(par3, par3, par3, 1.0F);
        }

        var4.startDrawingQuads();
        var4.setNormal(0.0F, 0.0F, -1.0F);
        this.renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(2, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(0.0F, 0.0F, 1.0F);
        this.renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(3, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(-1.0F, 0.0F, 0.0F);
        this.renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(4, metadata));
        var4.draw();
        var4.startDrawingQuads();
        var4.setNormal(1.0F, 0.0F, 0.0F);
        this.renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(5, metadata));
        var4.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
}
