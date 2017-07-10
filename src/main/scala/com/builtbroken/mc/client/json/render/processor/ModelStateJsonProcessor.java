package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.client.json.render.tile.TileState;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.json.conversion.JsonConverterPos;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2017.
 */
public class ModelStateJsonProcessor extends RenderJsonSubProcessor
{
    @Override
    public IRenderState process(JsonObject renderStateObject, String stateID, String globalRenderType, String subRenderType)
    {
        ModelState renderState;
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
            offset = JsonConverterPos.fromJson(renderStateObject.get("offset"));
            if (offset == null)
            {
                throw new IllegalArgumentException("Unknown value type for offset " + renderStateObject.get("offset"));
            }
        }

        //Loads scale value
        if (renderStateObject.has("scale"))
        {
            scale = JsonConverterPos.fromJson(renderStateObject.get("scale"));
            if (scale == null)
            {
                throw new IllegalArgumentException("Unknown value type for scale " + renderStateObject.get("scale"));
            }
        }

        //Loads rotations
        if (renderStateObject.has("rotation"))
        {
            JsonObject rotationObject = renderStateObject.get("rotation").getAsJsonObject();
            double yaw = 0;
            double pitch = 0;
            double roll = 0;

            if (rotationObject.has("yaw"))
            {
                yaw = rotationObject.getAsJsonPrimitive("yaw").getAsDouble();
            }

            if (rotationObject.has("pitch"))
            {
                pitch = rotationObject.getAsJsonPrimitive("pitch").getAsDouble();
            }

            if (rotationObject.has("roll"))
            {
                roll = rotationObject.getAsJsonPrimitive("roll").getAsDouble();
            }

            rotation = new EulerAngle(yaw, pitch, roll);
        }

        //Creates state object
        if (globalRenderType.equalsIgnoreCase("tile"))
        {
            renderState = new TileState(stateID, modelID, offset, scale, rotation);
        }
        else
        {
            renderState = new ModelState(stateID, modelID, offset, scale, rotation);
        }


        if (renderStateObject.has("rotationOrder"))
        {
            List<String> rotations = new ArrayList();

            JsonArray array = renderStateObject.getAsJsonArray("rotationOrder");
            for (int i = 0; i < array.size(); i++)
            {
                JsonElement e = array.get(i);
                if (e.isJsonPrimitive())
                {
                    String value = e.getAsString().toLowerCase();
                    if (!(value.equalsIgnoreCase("yaw") || value.equalsIgnoreCase("pitch") || value.equalsIgnoreCase("roll")
                            || value.equalsIgnoreCase("-yaw") || value.equalsIgnoreCase("-pitch") || value.equalsIgnoreCase("-roll")))
                    {
                        throw new IllegalArgumentException("Rotations order values can only be one of the follow {yaw, pitch, roll}");
                    }
                    rotations.add(value);
                }
                else
                {
                    throw new IllegalArgumentException("Rotations value must be a string containing yaw, pitch, or roll.");
                }
            }

            if (rotations.size() > 3)
            {
                throw new IllegalArgumentException("Only 3 rotations can be applied, used parented objects to apply more.");
            }
            renderState.rotationOrder = new String[rotations.size()];
            for (int i = 0; i < rotations.size(); i++)
            {
                renderState.rotationOrder[i] = rotations.get(i);
            }
        }

        if (renderStateObject.has("renderOnlyParts"))
        {
            renderState.renderOnlyParts = renderStateObject.get("renderOnlyParts").getAsBoolean();
        }

        if (renderStateObject.has("renderParent"))
        {
            renderState.renderParent = renderStateObject.get("renderParent").getAsBoolean();
        }

        //Loads parts to render if all is not selected
        if (renderStateObject.has("parts"))
        {
            JsonElement partsElement = renderStateObject.get("parts");
            if (partsElement.isJsonArray())
            {
                JsonArray array = partsElement.getAsJsonArray();
                List<String> parts = new ArrayList();
                for (JsonElement element : array)
                {
                    if (element.isJsonPrimitive())
                    {
                        parts.add(element.getAsString()); //TODO validate //TODO check if model contains (give warning if not)
                    }
                    //For loop handling to simplify loading of repeat values with minor number changes
                    else if (element.isJsonObject() && element.getAsJsonObject().has("for"))
                    {
                        JsonObject object = element.getAsJsonObject().getAsJsonObject("for");
                        ensureValuesExist(object, "start", "end", "part");
                        int start = object.getAsJsonPrimitive("start").getAsInt();
                        int end = object.getAsJsonPrimitive("end").getAsInt();

                        if (start >= end)
                        {
                            throw new IllegalArgumentException("Start can not be greater than or equal to end for a for loop.");
                        }

                        JsonPrimitive template = object.getAsJsonPrimitive("part");
                        for (int i = start; i <= end; i++)
                        {
                            parts.add(template.getAsString().replace("%number%", "" + i));
                        }
                    }
                    else
                    {
                        throw new IllegalArgumentException("Invalid json data for parts array, input must be a string or for loop object.");
                    }
                }

                renderState.parts = new String[parts.size()];
                for (int i = 0; i < parts.size(); i++)
                {
                    renderState.parts[i] = parts.get(i);
                }
            }
            else if (partsElement.isJsonPrimitive())
            {
                String parts = partsElement.getAsString();
                if (!parts.equals("all"))
                {
                    renderState.parts = parts.split(",");
                }
            }
            else
            {
                throw new IllegalArgumentException("Invalid json type for model parts, support types are string and string array");
            }
        }
        return renderState;
    }

    @Override
    public boolean canProcess(String type)
    {
        return type.equalsIgnoreCase("model");
    }
}
