package com.builtbroken.mc.api.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/** Enum of common recipes type shared by mods that extend the engine.
 * Also a useful helper to get access to the handlers for this types
 * quickly.
 *
 * Created by robert on 1/9/2015.
 */
public enum MachineRecipeType
{
    /** MC furnace recipes, Ore -> Ingot */
    ITEM_SMELTER,
    /** Item to Dust recipes, Ore -> Dust */
    ITEM_GRINDER,
    /** Item to rubble recipes, Ore -> Rubble */
    ITEM_CRUSHER,
    /** Item to clean item recipes, ore rubble -> metal rubble */
    ITEM_WASHER,
    /** Item to cut up item, Logs -> Planks */
    ITEM_SAWMILL,
    /** Item to items used to craft it, Chest -> 8 planks. Make sure to return the least valuable version of the recipe */
    ITEM_SALVAGER,
    /** Fluid + Fluid -> New Fluid */
    FLUID_MIXER,
    /** Item -> Molten Fluid */
    FLUID_SMELTER,
    /** Fluid -> Item */
    FLUID_SOLIDIFIER;

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
        return toItemStack(getRecipe(extra_chance, fail_chance, items));
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
