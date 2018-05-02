package com.builtbroken.mc.client.json.models.cube;

import com.builtbroken.mc.client.json.render.processor.BlockTexturedStateJsonProcessor;
import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.builtbroken.mc.framework.json.conversion.data.transform.JsonConverterPos;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Handles converting JSON data into cube data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/2/2018.
 */
public class JsonConverterModelCube extends JsonConverter<BlockModelPart>
{
    public JsonConverterModelCube()
    {
        super("model.part.cube");
    }

    @Override
    public BlockModelPart convert(JsonElement element, String... args)
    {
        final JsonObject jsonData = element.getAsJsonObject();
        JsonProcessor.ensureValuesExist(jsonData, "pos", "size");

        BlockModelPart modelPart = new BlockModelPart();

        modelPart.position = JsonConverterPos.fromJson(jsonData.get("pos"));
        modelPart.size = JsonConverterPos.fromJson(jsonData.get("size"));

        //Load global texture for the cube
        if (jsonData.has("texture") && jsonData.get("texture").isJsonPrimitive())
        {
            modelPart.setTexture(jsonData.get("texture").getAsString());
        }

        //Load textures in the same way a block would be loaded, as each model part can have textures
        BlockTexturedStateJsonProcessor.handleTextures(modelPart.textureID, jsonData);

        return modelPart;
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        final JsonObject jsonData = new JsonObject();
        //TODO implement
        return jsonData;
    }
}
