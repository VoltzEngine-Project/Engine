package com.builtbroken.mc.client.json.render;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.block.BlockState;
import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.client.json.render.state.RenderState;
import com.builtbroken.mc.client.json.render.state.TextureState;
import com.builtbroken.mc.client.json.render.tile.TileRenderData;
import com.builtbroken.mc.client.json.render.tile.TileState;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

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
        RenderData data;

        if (renderType.equalsIgnoreCase("tile"))
        {
            data = new TileRenderData(this, contentID, renderType);
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
            data = new RenderData(this, contentID, renderType);
        }

        JsonArray array = object.get("states").getAsJsonArray();
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
                if (renderStateObject.has("modelID"))
                {
                    modelID = renderStateObject.get("modelID").getAsString();
                }

                //Loads position offset
                if (renderStateObject.has("offset"))
                {
                    offset = Pos.fromJsonObject(renderStateObject.get("offset").getAsJsonObject());
                }

                //Loads scale value
                if (renderStateObject.has("scale"))
                {
                    JsonElement scaleElement = renderStateObject.get("scale");
                    if (scaleElement.isJsonObject())
                    {
                        offset = Pos.fromJsonObject(renderStateObject.get("scale").getAsJsonObject());
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
                    double yaw = 0;
                    double pitch = 0;
                    double roll = 0;

                    if(rotationObject.has("yaw"))
                    {
                        yaw = rotationObject.getAsJsonPrimitive("yaw").getAsDouble();
                    }

                    if(rotationObject.has("pitch"))
                    {
                        pitch = rotationObject.getAsJsonPrimitive("pitch").getAsDouble();
                    }

                    if(rotationObject.has("roll"))
                    {
                        roll = rotationObject.getAsJsonPrimitive("roll").getAsDouble();
                    }

                    rotation = new EulerAngle(yaw, pitch, roll);
                }

                //Creates state object
                if (renderType.equalsIgnoreCase("tile"))
                {
                    renderState = new TileState(stateID, modelID, offset, scale, rotation);
                }
                else
                {
                    renderState = new ModelState(stateID, modelID, offset, scale, rotation);
                }

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
                if (renderType.equalsIgnoreCase("block"))
                {
                    renderState = new BlockState(stateID);

                    //Load global texture for state
                    if (renderStateObject.has("textureID"))
                    {
                        String textureID = renderStateObject.get("textureID").getAsString();
                        for (int i = 0; i < 6; i++)
                        {
                            ((BlockState) renderState).textureID[i] = textureID;
                        }
                    }
                    //Load sides (2-5)
                    if (renderStateObject.has("sides"))
                    {
                        String key = renderStateObject.getAsJsonPrimitive("sides").getAsString();
                        for (int i = 2; i < 6; i++)
                        {
                            ((BlockState) renderState).textureID[i] = key;
                        }
                    }

                    //Load individual sides
                    for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                    {
                        final String key1 = "side:" + direction.ordinal();
                        final String key2 = direction.name().toLowerCase();
                        if (renderStateObject.has(key1))
                        {
                            ((BlockState) renderState).textureID[direction.ordinal()] = renderStateObject.getAsJsonPrimitive(key1).getAsString();
                        }
                        else if (renderStateObject.has(key2))
                        {
                            ((BlockState) renderState).textureID[direction.ordinal()] = renderStateObject.getAsJsonPrimitive(key2).getAsString();
                        }
                    }
                }
                else
                {
                    renderState = new TextureState(stateID);
                }
            }
            if (renderState instanceof TextureState)
            {
                //Load texture ID
                if (renderStateObject.has("textureID"))
                {
                    ((TextureState) renderState).textureID = renderStateObject.get("textureID").getAsString();
                }
            }
            if (renderState instanceof ModelState)
            {
                //Load parent ID
                if (renderStateObject.has("parent"))
                {
                    ((ModelState) renderState).parent = renderStateObject.get("parent").getAsString();
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
