package com.builtbroken.mc.prefab.json.recipe.smelting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

/**
 * Smelting recipe that uses a json object to get the output
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class SmeltingRecipeJson extends SmeltingRecipe
{
    private JsonObject json;

    public SmeltingRecipeJson(Object input, JsonObject json)
    {
        super(input);
        this.json = json;
        if(!json.has("type"))
        {
            throw new IllegalArgumentException(this + " json data is missing a type entry used to figure out what to load.");
        }
    }

    @Override
    public ItemStack getOutput()
    {
        if(output == null)
        {
            String type = json.get("type").getAsString();
            if(type.equalsIgnoreCase("block"))
            {

            }
            else if(type.equalsIgnoreCase("item"))
            {

            }
            else if(type.equalsIgnoreCase("dict"))
            {

            }
        }
        return output;
    }
}
