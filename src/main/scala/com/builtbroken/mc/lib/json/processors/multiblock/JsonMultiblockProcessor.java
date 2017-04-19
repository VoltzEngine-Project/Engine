package com.builtbroken.mc.lib.json.processors.multiblock;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.multiblock.EnumMultiblock;
import com.builtbroken.mc.framework.multiblock.structure.MultiBlockLayout;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.json.conversion.JsonConverterPos;
import com.builtbroken.mc.lib.json.processors.JsonProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Prefab for any processor that uses item/block based recipes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public class JsonMultiblockProcessor extends JsonProcessor<MultiBlockLayout>
{
    @Override
    public String getMod()
    {
        return References.DOMAIN;
    }

    @Override
    public String getJsonKey()
    {
        return "multiblock";
    }

    @Override
    public String getLoadOrder()
    {
        return null;
    }

    @Override
    public MultiBlockLayout process(JsonElement data)
    {
        JsonObject multiBlockData = data.getAsJsonObject();
        ensureValuesExist(multiBlockData, "key", "tiles");
        String key = multiBlockData.get("key").getAsString().toLowerCase();
        MultiBlockLayout layout = new MultiBlockLayout(this, key);
        JsonArray array = multiBlockData.get("tiles").getAsJsonArray();
        for (JsonElement element : array)
        {
            if (element.isJsonObject())
            {
                JsonObject object = element.getAsJsonObject();
                ensureValuesExist(object, "pos");
                Pos pos = JsonConverterPos.fromJson(object.get("pos"));
                String tile = EnumMultiblock.TILE.getTileName();
                String tileData = "";
                if (object.has("data"))
                {
                    tileData = object.get("data").getAsString();
                }
                if (object.has("tile"))
                {
                    tile = object.get("tile").getAsString(); //TODO ensure matches enum
                }
                layout.addTile(pos, tile + "#" + tileData);
            }
        }
        return layout;
    }
}
