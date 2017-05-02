package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.state.RenderState;
import com.builtbroken.mc.client.json.render.state.TextureState;
import com.builtbroken.mc.client.json.render.tile.TileRenderData;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/22/2016.
 */
public class RenderJsonProcessor extends JsonProcessor<RenderData>
{
    public static final List<RenderJsonSubProcessor> stateProcessors = new ArrayList();

    private final HashMap<RenderState, RenderJsonSubProcessor> stateToProcessor = new HashMap();

    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "render";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public RenderData process(JsonElement element)
    {
        final JsonObject object = element.getAsJsonObject();
        ensureValuesExist(object, "contentID", "states", "type");

        String contentID = object.get("contentID").getAsString();
        String overAllRenderType = object.get("type").getAsString();
        RenderData data;

        if (overAllRenderType.equalsIgnoreCase("tile"))
        {
            data = new TileRenderData(this, contentID, overAllRenderType);
            if (object.has("tileClass"))
            {
                try
                {
                    ((TileRenderData) data).tileClass = (Class<? extends TileEntity>) Class.forName(object.get("tileClass").getAsString());
                }
                catch (Exception e)
                {
                    throw new IllegalArgumentException("Failed to load class for name '" + object.get("tileClass").getAsString() + "'");
                }
            }
        }
        else
        {
            data = new RenderData(this, contentID, overAllRenderType);
        }

        JsonArray array = object.get("states").getAsJsonArray();
        for (JsonElement e : array)
        {
            IRenderState renderState = null;

            //Data
            JsonObject renderStateObject = e.getAsJsonObject();
            ensureValuesExist(renderStateObject, "id");

            //State ID
            JsonPrimitive stateIDElement = renderStateObject.getAsJsonPrimitive("id");
            String stateID = stateIDElement.getAsString();

            //Type override
            String subType = overAllRenderType;
            if (renderStateObject.has("renderType"))
            {
                subType = renderStateObject.get("renderType").getAsString();
            }

            //Check sub processors in order to get state
            for (RenderJsonSubProcessor processor : stateProcessors)
            {
                if (processor.canProcess(subType))
                {
                    renderState = processor.process(renderStateObject, stateID, overAllRenderType, subType);
                    if (renderState != null)
                    {
                        break;
                    }
                }
            }

            //Backup state / Default state
            if (renderState == null)
            {
                //Texture state, basally for icons
                renderState = new TextureState(stateID);
                if (renderStateObject.has("textureID"))
                {
                    ((TextureState) renderState).textureID = renderStateObject.get("textureID").getAsString();
                }
            }

            //Add state to map
            data.add(stateID, renderState);
        }
        //Set parent object
        for (Collection<IRenderState> l : new Collection[]{data.renderStatesByName.values()})
        {
            for (IRenderState renderState : l)
            {
                if (stateToProcessor.containsKey(renderState))
                {
                    stateToProcessor.get(renderState).postHandle(renderState, data);
                }
            }
        }

        //Clear run data
        stateProcessors.clear();

        //TODO validate states to ensure they fill function
        //TODO ensure modelID exists if model state
        return data;
    }
}
