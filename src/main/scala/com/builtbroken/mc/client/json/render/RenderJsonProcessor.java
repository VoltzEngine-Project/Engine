package com.builtbroken.mc.client.json.render;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.client.json.render.state.RenderState;
import com.builtbroken.mc.client.json.render.state.TextureState;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Collection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/22/2016.
 */
public class RenderJsonProcessor extends JsonProcessor<RenderData>
{
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
        String renderType = object.get("type").getAsString();
        RenderData data = new RenderData(this, contentID, renderType);


        JsonArray array = object.get("states").getAsJsonArray();
        if (renderType.equalsIgnoreCase("block"))
        {
            //TODO load meta or rotation render states
        }
        else
        {
            for (JsonElement e : array)
            {
                RenderState renderState;
                JsonObject renderStateObject = e.getAsJsonObject();
                ensureValuesExist(renderStateObject, "id");

                JsonPrimitive stateIDElement = renderStateObject.getAsJsonPrimitive("id");
                String stateID = stateIDElement.getAsString();

                if (renderStateObject.has("modelID") || renderStateObject.has("parent"))
                {
                    //Data
                    String modelID = null;
                    Pos offset = null;
                    Pos scale = null;
                    EulerAngle rotation = null;

                    //Load model ID, child objects may or may not contain this
                    if(renderStateObject.has("modelID"))
                    {
                        modelID = renderStateObject.get("modelID").getAsString();
                    }

                    //Loads position offset
                    if (renderStateObject.has("offset"))
                    {
                        JsonObject offsetObject = renderStateObject.get("offset").getAsJsonObject();
                        offset = new Pos(
                                offsetObject.getAsJsonPrimitive("x").getAsDouble(),
                                offsetObject.getAsJsonPrimitive("y").getAsDouble(),
                                offsetObject.getAsJsonPrimitive("z").getAsDouble());
                    }

                    //Loads scale value
                    if (renderStateObject.has("scale"))
                    {
                        JsonElement scaleElement = renderStateObject.get("scale");
                        if (scaleElement.isJsonObject())
                        {
                            JsonObject scaleObject = scaleElement.getAsJsonObject();
                            scale = new Pos(
                                    scaleObject.getAsJsonPrimitive("x").getAsDouble(),
                                    scaleObject.getAsJsonPrimitive("y").getAsDouble(),
                                    scaleObject.getAsJsonPrimitive("z").getAsDouble());
                        }
                        else if (scaleElement.isJsonPrimitive())
                        {
                            scale = new Pos(scaleElement.getAsDouble());
                        }
                        else
                        {
                            throw new IllegalArgumentException("Unknown value type for scale " + scaleElement);
                        }
                    }

                    //Loads rotations
                    if (renderStateObject.has("rotation"))
                    {
                        JsonObject rotationObject = renderStateObject.get("rotation").getAsJsonObject();
                        rotation = new EulerAngle(
                                rotationObject.getAsJsonPrimitive("yaw").getAsDouble(),
                                rotationObject.getAsJsonPrimitive("pitch").getAsDouble(),
                                rotationObject.getAsJsonPrimitive("roll").getAsDouble());
                    }
                    //Creates state object
                    renderState = new ModelState(stateID, modelID, offset, scale, rotation);

                    //Loads parts to render if all is not selected
                    if (renderStateObject.has("parts"))
                    {
                        String parts = renderStateObject.get("parts").getAsString();
                        if (!parts.equals("all"))
                        {
                            ((ModelState) renderState).parts = parts.split(",");
                        }
                    }
                    //String version, preferred as easy to understand
                    data.add(stateID, renderState);
                }
                //Texture state, basally for icons
                else
                {
                    renderState = new TextureState(stateID);
                }
                //Load texture ID
                if (renderStateObject.has("textureID"))
                {
                    ((TextureState) renderState).textureID = renderStateObject.get("textureID").getAsString();
                }
                //Load parent ID
                if (renderStateObject.has("parent"))
                {
                    ((ModelState) renderState).parent = renderStateObject.get("parent").getAsString();
                }
                data.add(stateID, renderState);
            }
        }
        //Set parent object
        for (Collection<IRenderState> l : new Collection[]{data.renderStatesByName.values()})
        {
            for (IRenderState renderState : l)
            {
                if (renderState instanceof ModelState)
                {
                    ModelState state = (ModelState) renderState;
                    if (state.parent != null)
                    {
                        IRenderState parentState = data.getState(state.parent);
                        if (parentState != null)
                        {
                            state.parentState = parentState;
                        }
                        else
                        {
                            throw new RuntimeException("Failed to locate a render state parent with name '" + state.parent + "' for render state '" + state.id);
                        }
                    }
                }
            }
        }
        //TODO validate states to ensure they fill function
        //TODO ensure modelID exists if model state
        return data;
    }
}
