package com.builtbroken.mc.lib.json.processors.recipe.smelting;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.lib.json.processors.recipe.JsonRecipeData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

/**
 * Object used to temp hold data about a smelting recipe while we wait on blocks to be registered
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class JsonFurnaceRecipeData extends JsonRecipeData implements IJsonGenObject, IPostInit
{
    /** Input for the recipe */
    public final Object input;
    /** XP recipe of the recipe*/
    public float xp;

    public JsonFurnaceRecipeData(IJsonProcessor processor, Object input, Object output, float xp)
    {
        super(processor, output);
        this.input = input;
        this.xp = xp;
    }

    @Override
    public void onPostInit()
    {
        ItemStack outputStack = toStack(output);
        ItemStack inputStack = toStack(input);
        if (outputStack != null)
        {
            if (inputStack != null)
            {
                GameRegistry.addSmelting(inputStack, outputStack, xp);
            }
            else
            {
                Engine.logger().error("JsonSmeltingRecipe: Failed to parse input for " + this);
            }
        }
        else

        {
            Engine.logger().error("JsonSmeltingRecipe: Failed to parse output for " + this);
        }
    }

    @Override
    public String toString()
    {
        return "JsonFurnaceRecipe[" + input + " -> " + output + "]";
    }
}
