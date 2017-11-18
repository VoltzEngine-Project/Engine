package com.builtbroken.mc.client.json.render.block.model;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/16/2017.
 */
public class BlockModelData extends JsonGenData
{
    public List<BlockModelPart> parts = new ArrayList();
    public BlockModelData(IJsonProcessor processor)
    {
        super(processor);
    }
}
