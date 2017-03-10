package com.builtbroken.mc.lib.json.recipe.smelting;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Object used to temp hold data about a smelting recipe while we wait on blocks to be registered
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class JsonSmeltingRecipeData implements IJsonGenObject
{
    /** {@link net.minecraft.item.Item} or {@link net.minecraft.block.Block} */
    public final Object input;
    public int inputMeta = -1;
    public float xp;

    public ItemStack output;

    public JsonSmeltingRecipeData(Object input)
    {
        this.input = input;
        if (!(input instanceof Block || input instanceof Item))
        {
            throw new IllegalArgumentException("Input for a smelting recipe must be an Item or Block");
        }
    }

    @Override
    public void register()
    {
        ItemStack output = getOutput();
        if (output != null)
        {
            if (input instanceof Block)
            {
                if (inputMeta > -1)
                {
                    GameRegistry.addSmelting(new ItemStack((Block) input, 1, inputMeta), output, xp);
                }
                else
                {
                    GameRegistry.addSmelting((Block) input, output, xp);
                }
            }
            else if (input instanceof Item)
            {
                if (inputMeta > -1)
                {
                    GameRegistry.addSmelting(new ItemStack((Item) input, 1, inputMeta), output, xp);
                }
                else
                {
                    GameRegistry.addSmelting((Item) input, output, xp);
                }
            }
        }
        else
        {
            Engine.logger().error("JsonSmeltingRecipe: Failed to get output for " + this);
        }
    }

    /**
     * Output of the recipe, calculated on first call
     *
     * @return
     */
    public ItemStack getOutput()
    {
        return output;
    }

    @Override
    public String toString()
    {
        return "SmeltingRecipe[" + input + (inputMeta != -1 ? "@" + inputMeta : "") + " -> " + getOutput() + "]";
    }
}
