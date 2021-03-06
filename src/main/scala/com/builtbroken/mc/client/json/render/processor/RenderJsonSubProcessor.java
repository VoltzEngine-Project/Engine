package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.state.RenderState;
import com.builtbroken.mc.client.json.render.state.RenderStateTexture;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2017.
 */
public abstract class RenderJsonSubProcessor<S extends IRenderState>
{
    public final TextureData.Type textureType;

    public RenderJsonSubProcessor(TextureData.Type textureType)
    {
        this.textureType = textureType;
    }

    public abstract S process(JsonObject renderStateObject, String stateID, String globalRenderType, String subRenderType);

    /**
     * Called to process common data shared by a layered processor design
     *
     * @param state
     * @param renderStateObject
     */
    protected void process(S state, JsonObject renderStateObject)
    {
        if (state instanceof RenderState)
        {
            //Set parent of state
            if (renderStateObject.has("parent"))
            {
                ((RenderState) state).parent = renderStateObject.get("parent").getAsString();
            }

            if (renderStateObject.has("color"))
            {
                ((RenderState) state).color = renderStateObject.get("color").getAsString();
            }
            //Legacy way to register textures
            if (renderStateObject.has("textureID"))
            {
                setMainTexture(state, renderStateObject.get("textureID").getAsString());
            }
            //Load global texture for state
            if (renderStateObject.has("texture") && renderStateObject.get("texture").isJsonPrimitive())
            {
                String textureID = renderStateObject.get("texture").getAsString();
                setMainTexture(state, textureID);
            }
            else
            {
                //TODO add error state if textures are being loaded without being used
                //Load all textures (mainly for blocks)
                for (Map.Entry<String, JsonElement> elementEntry : renderStateObject.entrySet())
                {
                    //Lazy way to register textures
                    if (elementEntry.getKey().equalsIgnoreCase("texture")
                            || elementEntry.getKey().contains(":") && elementEntry.getKey().split(":")[0].equalsIgnoreCase("texture"))
                    {
                        //Get data
                        JsonObject textureData = elementEntry.getValue().getAsJsonObject();

                        //Enforce minimal values
                        JsonProcessor.ensureValuesExist(textureData, "domain", "name");

                        //Read minimal values
                        String domain = textureData.getAsJsonPrimitive("domain").getAsString();
                        String name = textureData.getAsJsonPrimitive("name").getAsString();

                        //Get key
                        String key;

                        //Get key in case it doesn't match texture name
                        if (textureData.has("key"))
                        {
                            key = textureData.getAsJsonPrimitive("key").getAsString();
                        }
                        else
                        {
                            key = domain + ":" + name;
                        }

                        //Init texture ID
                        if (!hasTexture(state))
                        {
                            setMainTexture(state, key);
                        }

                        //Create and register texture
                        TextureData data = new TextureData(null, key, domain, name, textureType);
                        List<IJsonGenObject> list = JsonContentLoader.INSTANCE.generatedObjects.get("texture");
                        if (list == null)
                        {
                            list = new ArrayList();
                        }
                        list.add(data);
                        JsonContentLoader.INSTANCE.generatedObjects.put("texture", list);
                        data.onCreated();
                    }
                }
            }
        }
    }

    protected void setMainTexture(S state, String key)
    {
        if (state instanceof RenderStateTexture)
        {
            ((RenderStateTexture) state).setTextureID(key);
        }
    }

    protected boolean hasTexture(S state)
    {
        if (state instanceof RenderStateTexture)
        {
            return ((RenderStateTexture) state).getTextureID() != null;
        }
        return false;
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


    /**
     * Quick way to check that required fields exist in the json file
     *
     * @param object
     * @param values
     */
    public static void ensureValuesExist(JsonObject object, String... values)
    {
        for (String value : values)
        {
            if (!object.has(value))
            {
                throw new IllegalArgumentException("File is missing " + value + " value " + object);
            }
        }
    }

    public abstract boolean canProcess(String type);
}
