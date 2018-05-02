package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.render.block.model.RenderStateBlockModel;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.google.gson.JsonObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2017.
 */
public class BlockModelStateJsonProcessor extends RenderJsonSubProcessor<RenderStateBlockModel>
{
    public BlockModelStateJsonProcessor()
    {
        super(TextureData.Type.BLOCK);
    }

    @Override
    public RenderStateBlockModel process(JsonObject renderStateObject, String stateID, String globalRenderType, String subRenderType)
    {
        RenderStateBlockModel renderState = new RenderStateBlockModel(stateID);
        //Load model ID, child objects may or may not contain this
        if (renderStateObject.has("modelID"))
        {
            renderState.modelID = renderStateObject.get("modelID").getAsString();
        }
        return renderState;
    }

    @Override
    protected void setMainTexture(RenderStateBlockModel state, String key)
    {

    }

    @Override
    protected boolean hasTexture(RenderStateBlockModel state)
    {
        return true;
    }

    @Override
    public boolean canProcess(String type)
    {
        return type.equalsIgnoreCase("blockModel");
    }
}
