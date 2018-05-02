package com.builtbroken.mc.client.json.render.block.model;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.models.cube.BlockModelData;
import com.builtbroken.mc.client.json.render.state.RenderState;
import com.builtbroken.mc.client.json.texture.TextureData;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class RenderStateBlockModel extends RenderState
{
    public String modelID;
    protected BlockModelData model;

    public RenderStateBlockModel(String id)
    {
        super(id);
    }

    @Override
    public IIcon getIcon(int side)
    {
        TextureData textureData = getTextureData(side);
        if (textureData != null && textureData.getIcon() != null)
        {
            return textureData.getIcon();
        }
        return null;
    }

    @Override
    public TextureData getTextureData(int side)
    {
        return null;
    }

    @Override
    public void addDebugLines(List<String> lines)
    {
        super.addDebugLines(lines);
        lines.add("ModelID = " + modelID);
        lines.add("Model = " + getModel());
    }

    @Override
    public String toString()
    {
        return "RenderStateBlock[" + id + "]@" + hashCode();
    }

    public BlockModelData getModel()
    {
        if (model == null)
        {
            model = ClientDataHandler.INSTANCE.getBlockModel(modelID);
        }
        return model;
    }

    public boolean render(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderBlocks)
    {
        BlockModelData modelData = getModel();
        if (modelData != null)
        {
            return modelData.render(world, x, y, z, block, renderBlocks);
        }
        return false;
    }
}
