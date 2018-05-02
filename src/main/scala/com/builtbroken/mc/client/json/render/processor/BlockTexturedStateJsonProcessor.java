package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.render.block.RenderStateTexturedBlock;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.builtbroken.mc.data.Direction;
import com.google.gson.JsonObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2017.
 */
public class BlockTexturedStateJsonProcessor extends RenderJsonSubProcessor<RenderStateTexturedBlock>
{
    public BlockTexturedStateJsonProcessor()
    {
        super(TextureData.Type.BLOCK);
    }

    @Override
    public RenderStateTexturedBlock process(JsonObject renderStateObject, String stateID, String globalRenderType, String subRenderType)
    {
        RenderStateTexturedBlock renderState = new RenderStateTexturedBlock(stateID);
        handleTextures(renderState.textureIDs, renderStateObject);
        return renderState;
    }

    public static void handleTextures(String[] textureID, JsonObject renderStateObject)
    {
        //Load sides (2-5)
        if (renderStateObject.has("sides"))
        {
            String key = renderStateObject.getAsJsonPrimitive("sides").getAsString();
            for (int i = 2; i < 6; i++)
            {
                textureID[i] = key;
            }
        }

        //Top aka UP
        if (renderStateObject.has("top"))
        {
            String key = renderStateObject.getAsJsonPrimitive("top").getAsString();
            textureID[1] = key;
        }

        //Bot aka DOWN
        if (renderStateObject.has("bot"))
        {
            String key = renderStateObject.getAsJsonPrimitive("bot").getAsString();
            textureID[0] = key;
        }

        //Bottom aka DOWN
        if (renderStateObject.has("bottom"))
        {
            String key = renderStateObject.getAsJsonPrimitive("bottom").getAsString();
            textureID[0] = key;
        }

        //Load individual sides
        for (Direction direction : Direction.DIRECTIONS)
        {
            final String key1 = "side:" + direction.ordinal();
            final String key2 = direction.name().toLowerCase();
            if (renderStateObject.has(key1))
            {
                textureID[direction.ordinal()] = renderStateObject.getAsJsonPrimitive(key1).getAsString();
            }
            else if (renderStateObject.has(key2))
            {
                textureID[direction.ordinal()] = renderStateObject.getAsJsonPrimitive(key2).getAsString();
            }
        }
    }

    @Override
    protected void setMainTexture(RenderStateTexturedBlock state, String key)
    {
        for (int i = 0; i < 6; i++)
        {
            if (state.textureIDs[i] == null)
            {
                state.textureIDs[i] = key;
            }
        }
    }

    @Override
    protected boolean hasTexture(RenderStateTexturedBlock state)
    {
        for (int i = 0; i < 6; i++)
        {
            if (state.textureIDs[i] == null)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canProcess(String type)
    {
        return type.equalsIgnoreCase("block");
    }
}
