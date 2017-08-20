package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.state.TextureState;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.google.gson.JsonObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/13/2017.
 */
public class ItemStateJsonProcessor extends RenderJsonSubProcessor
{
    public ItemStateJsonProcessor()
    {
        super(TextureData.Type.ITEM);
    }

    @Override
    public IRenderState process(JsonObject renderStateObject, String stateID, String globalRenderType, String subRenderType)
    {
        return new TextureState(stateID);
    }

    @Override
    public boolean canProcess(String type)
    {
        return "item".equals(type);
    }
}
