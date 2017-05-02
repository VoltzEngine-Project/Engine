package com.builtbroken.mc.client.json.render.processor;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.client.json.render.tile.TileState;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.json.conversion.JsonConverterPos;
import com.google.gson.JsonObject;

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
            String parts = renderStateObject.get("parts").getAsString();
            if (!parts.equals("all"))
            {
                renderState.parts = parts.split(",");
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
