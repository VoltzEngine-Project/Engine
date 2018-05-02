package com.builtbroken.mc.client.json.models.cube;

import com.builtbroken.mc.client.BlockRenderWrapper;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.render.RenderUtility;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/16/2017.
 */
public class BlockModelPart
{
    public final static double pixel = 1.0 / 16.0;
    public static BlockRenderWrapper wrapper;

    public Pos position;
    public Pos size;

    public final String[] textureID = new String[6];

    public void render(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer)
    {
        bounds(renderer,
                pixel * position.xf(), pixel * position.yf(), pixel * position.zf(),
                pixel * size.xf(), pixel * size.yf(), pixel * size.zf());
        renderBlock(renderer, block, x, y, z, null);
    }

    protected void bounds(RenderBlocks renderer, double x, double y, double z, double xx, double yy, double zz)
    {
        renderer.setRenderBounds(x, y, z, x + xx, y + yy, z + zz);
    }

    public void renderBlock(RenderBlocks renderer, Block block, int x, int y, int z, IIcon icon)
    {
        if (y == -1)
        {
            RenderUtility.renderCube(renderer.renderMinX, renderer.renderMinY, renderer.renderMinZ, renderer.renderMaxX, renderer.renderMaxY, renderer.renderMaxZ, getWrapper(block), icon, 0);
        }
        else
        {

            renderer.setOverrideBlockTexture(icon);
            renderer.renderStandardBlock(getWrapper(block), x, y, z);
            renderer.setOverrideBlockTexture(null);
        }
    }

    protected BlockRenderWrapper getWrapper(Block block)
    {
        if (wrapper == null)
        {
            wrapper = new BlockRenderWrapper(block);
        }
        else
        {
            wrapper.realBlock = block;
            wrapper.clearRenderSides();
        }
        return wrapper;
    }

    public IIcon getIcon(int side)
    {
        TextureData textureData = getTextureData(side);
        if (textureData != null && textureData.getIcon() != null)
        {
            return textureData.getIcon();
        }
        return null;
    }

    public TextureData getTextureData(int side)
    {
        if (side >= 0 && side < 6)
        {
            return textureID[side] != null ? ClientDataHandler.INSTANCE.getTexture(textureID[side]) : null;
        }
        return getTextureData(0);
    }

    protected void setTexture(String key)
    {
        for (int i = 0; i < 6; i++)
        {
            if (textureID[i] == null)
            {
                textureID[i] = key;
            }
        }
    }
}
