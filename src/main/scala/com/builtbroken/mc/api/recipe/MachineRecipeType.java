package com.builtbroken.mc.api.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by robert on 1/9/2015.
 */
public enum MachineRecipeType
{
    SMELTER,
    GRINDER,
    CRUSHER,
    WASHER,
    MIXER,
    SAWMILL;

    private IMachineRecipeHandler handler;

    public IMachineRecipeHandler getHandler()
    {
        return handler;
    }

    /**
     * Used to set the handler for the recipe type. If you
     * plan to override Voltz-Engine's default handler
     * make sure to inform the user that it is your handler.
     * As well make sure to copy over any recipes already
     * registered to avoid compatibility issues.
     *
     * @param handler - new handler instance
     */
    public void setHandler(IMachineRecipeHandler handler)
    {
        this.handler = handler;
    }

    public RecipeRegisterResult registerRecipe(IMachineRecipe recipe)
    {
        if (handler != null)
        {
            return handler.registerRecipe(recipe);
        }
        return RecipeRegisterResult.NO_HANDLER;
    }

    public Object getRecipe(Object... items)
    {
        return getRecipe(0, 0, items);
    }

    public Object getRecipe(float extra_chance, float fail_chance, Object... items)
    {
        if (getHandler() != null)
        {
            return getHandler().getRecipe(items, extra_chance, fail_chance);
        }
        return null;
    }

    public ItemStack getItemStackRecipe(float extra_chance, float fail_chance, Object... items)
    {
        return toItemStack(getRecipe(items, extra_chance, items));
    }

    /**
     * Helper method to convert the output into an itemstack
     * @param result - single object result, doesn't handle collections
     * @return ItemStack from the result
     */
    public static ItemStack toItemStack(Object result)
    {
        if (result instanceof ItemStack)
        {
            return (ItemStack) result;
        }
        else if (result instanceof Block)
        {
            return new ItemStack((Block) result);
        }
        else if (result instanceof Item)
        {
            return new ItemStack((Item) result);
        }
        return null;
    }

    @Override
    public String toString()
    {
        return name().toLowerCase();
    }
}
