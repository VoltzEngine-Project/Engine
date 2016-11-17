package com.builtbroken.mc.lib.json.recipe.smelting;

import com.builtbroken.mc.lib.json.JsonContentLoader;
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
        //Error checking/data validation is done by loader
    }

    @Override
    public ItemStack getOutput()
    {
        if (output == null)
        {
            //Built late in case items are not loaded yet
            output = JsonContentLoader.fromJson(json);
        }
        return output;
    }
}
