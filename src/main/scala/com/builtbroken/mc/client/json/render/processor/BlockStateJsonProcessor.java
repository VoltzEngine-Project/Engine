package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.block.BlockState;
import com.builtbroken.mc.data.Direction;
import com.google.gson.JsonObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2017.
 */
public class BlockStateJsonProcessor extends RenderJsonSubProcessor
{
    @Override
    public IRenderState process(JsonObject renderStateObject, String stateID, String globalRenderType, String subRenderType)
    {
        BlockState renderState = new BlockState(stateID);

        //Load global texture for state
        if (renderStateObject.has("textureID"))
        {
            String textureID = renderStateObject.get("textureID").getAsString();
            for (int i = 0; i < 6; i++)
            {
                renderState.textureID[i] = textureID;
            }
        }
        //Load sides (2-5)
        if (renderStateObject.has("sides"))
        {
            String key = renderStateObject.getAsJsonPrimitive("sides").getAsString();
            for (int i = 2; i < 6; i++)
            {
                renderState.textureID[i] = key;
            }
        }

        //Top aka UP
        if (renderStateObject.has("top"))
        {
            String key = renderStateObject.getAsJsonPrimitive("top").getAsString();
            renderState.textureID[1] = key;
        }

        //Bot aka DOWN
        if (renderStateObject.has("bot"))
        {
            String key = renderStateObject.getAsJsonPrimitive("bot").getAsString();
            renderState.textureID[0] = key;
        }

        //Bottom aka DOWN
        if (renderStateObject.has("bottom"))
        {
            String key = renderStateObject.getAsJsonPrimitive("bottom").getAsString();
            renderState.textureID[0] = key;
        }

        //Load individual sides
        for (Direction direction : Direction.DIRECTIONS)
        {
            final String key1 = "side:" + direction.ordinal();
            final String key2 = direction.name().toLowerCase();
            if (renderStateObject.has(key1))
            {
                renderState.textureID[direction.ordinal()] = renderStateObject.getAsJsonPrimitive(key1).getAsString();
            }
            else if (renderStateObject.has(key2))
            {
                renderState.textureID[direction.ordinal()] = renderStateObject.getAsJsonPrimitive(key2).getAsString();
            }
        }
        return renderState;
    }

    @Override
    public boolean canProcess(String type)
    {
        return type.equalsIgnoreCase("block");
    }
}
