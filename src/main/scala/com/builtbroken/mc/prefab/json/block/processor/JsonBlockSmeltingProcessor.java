package com.builtbroken.mc.prefab.json.block.processor;

import com.builtbroken.mc.lib.mod.loadable.ILoadable;
import com.builtbroken.mc.prefab.json.block.BlockJson;
import com.builtbroken.mc.prefab.json.block.meta.MetaData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class JsonBlockSmeltingProcessor extends JsonBlockSubProcessor implements ILoadable
{
    private List<RecipeData> recipes = new ArrayList();

    @Override
    public void process(BlockJson block, JsonElement element)
    {
        RecipeData data = new RecipeData();
        data.block = block;
        process(data, element.getAsJsonObject());
    }

    @Override
    public void processMeta(MetaData meta, BlockJson block, JsonElement element)
    {
        RecipeData data = new RecipeData();
        data.block = block;
        data.meta = meta.index;
        process(data, element.getAsJsonObject());
    }

    private void process(RecipeData data, JsonObject element)
    {
        if (element.has("xp"))
        {
            data.xp = (float) element.get("xp").getAsDouble();
        }
        if (element.has("output"))
        {
            data.output = element.get("xp").getAsString();
        }
    }

    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {

    }

    @Override
    public void postInit()
    {
        recipes.forEach(RecipeData::register);
    }

    protected class RecipeData
    {
        Block block;
        int meta = -1;
        String output;
        float xp;

        public void register()
        {

        }
    }
}
