package com.builtbroken.mc.framework.json.conversion.data.mc;

import com.builtbroken.mc.framework.json.conversion.JsonConverter;
import com.builtbroken.mc.lib.helper.MaterialDict;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.material.Material;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class JsonConverterMaterial extends JsonConverter<Material>
{
    public JsonConverterMaterial()
    {
        super("material");
    }

    @Override
    public Material convert(JsonElement element, String... args)
    {
        if(element.isJsonPrimitive())
        {
            return MaterialDict.get(element.getAsString().trim());
        }
        throw new IllegalArgumentException("JsonConverterMaterial: could not convert json to material object, json: " + element);
    }

    @Override
    public JsonElement build(String type, Object data, String... args)
    {
        if(data instanceof Material)
        {
            return new JsonPrimitive(MaterialDict.getName((Material) data));
        }
        return null;
    }
}
