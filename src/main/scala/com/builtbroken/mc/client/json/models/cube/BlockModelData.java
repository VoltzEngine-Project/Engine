package com.builtbroken.mc.client.json.models.cube;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/16/2017.
 */
public class BlockModelData extends JsonGenData
{
    @JsonProcessorData(value = "key", required = true)
    public String key;

    @JsonProcessorData(value = "cubes", type = "list.array", args = "model.part.cube")
    public List<BlockModelPart> parts = new ArrayList();

    public BlockModelData(IJsonProcessor processor)
    {
        super(processor);
    }

    public boolean render(IBlockAccess world, int x, int y, int z, Block block, int meta, RenderBlocks renderBlocks)
    {
        if(parts.size() > 0)
        {
            for(BlockModelPart part : parts)
            {
                part.render(world, x, y, z, block, meta, renderBlocks);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onCreated()
    {
        ClientDataHandler.INSTANCE.addBlockModel(key, this);
    }

    @Override
    public String getContentID()
    {
        return key;
    }

    @Override
    public String getUniqueID()
    {
        return key;
    }
}
