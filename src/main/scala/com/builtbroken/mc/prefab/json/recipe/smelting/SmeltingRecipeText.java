package com.builtbroken.mc.prefab.json.recipe.smelting;

import com.builtbroken.mc.prefab.json.JsonContentLoader;
import net.minecraft.item.ItemStack;

/**
 * Smelting recipe that uses a string to get the output data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class SmeltingRecipeText extends SmeltingRecipe
{
    private String outputText;

    public SmeltingRecipeText(Object input, String output)
    {
        super(input);
        this.outputText = output;
    }

    @Override
    public ItemStack getOutput()
    {
        if (output == null)
        {
            output = JsonContentLoader.fromString(outputText);
        }
        return output;
    }
}
