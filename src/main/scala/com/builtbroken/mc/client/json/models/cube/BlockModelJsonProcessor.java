package com.builtbroken.mc.client.json.models.cube;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * Handles loading block model
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2018.
 */
public class BlockModelJsonProcessor extends JsonProcessor<BlockModelData>
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "cubeModel";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public BlockModelData process(JsonElement element)
    {
        return new BlockModelData(this);
    }
}
