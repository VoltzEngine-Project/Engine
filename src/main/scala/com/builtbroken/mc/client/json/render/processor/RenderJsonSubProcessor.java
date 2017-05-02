package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.state.RenderState;
import com.builtbroken.mc.client.json.render.state.TextureState;
import com.google.gson.JsonObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2017.
 */
public abstract class RenderJsonSubProcessor
{
    public abstract IRenderState process(JsonObject renderStateObject, String stateID, String globalRenderType, String subRenderType);

    /**
     * Called to process common data shared by a layered processor design
     *
     * @param state
     * @param renderStateObject
     */
    protected void process(IRenderState state, JsonObject renderStateObject)
    {
        if (state instanceof RenderState)
        {
            //Set parent of state
            if (renderStateObject.has("parent"))
            {
                ((RenderState) state).parent = renderStateObject.get("parent").getAsString();
            }

            if (state instanceof TextureState)
            {
                if (renderStateObject.has("textureID"))
                {
                    ((TextureState) state).textureID = renderStateObject.get("textureID").getAsString();
                }
            }
        }
    }

    /**
     * Called after all states are loaded to do
     * linking between states.
     *
     * @param state
     */
    public void postHandle(IRenderState state, RenderData data)
    {
        if (state instanceof RenderState)
        {
            if (((RenderState) state).parent != null)
            {
                IRenderState parentState = data.getState(((RenderState) state).parent);
                if (parentState != null)
                {
                    ((RenderState) state).parentState = parentState;
                }
                else
                {
                    throw new RuntimeException("Failed to locate a render state parent with name '" + ((RenderState) state).parent + "' for render state '" + ((RenderState) state).id);
                }
            }
        }
    }

    public abstract boolean canProcess(String type);
}
