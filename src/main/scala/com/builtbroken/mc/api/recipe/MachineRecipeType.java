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
    ITEM_SMELTER("smelter"),
    /** Item to Dust recipes, Ore -> Dust */
    ITEM_GRINDER("ore.grinder"),
    /** Item to rubble recipes, Ore -> Rubble */
    ITEM_CRUSHER("ore.crusher"),
    /** Item to clean item recipes, ore rubble -> metal rubble */
    ITEM_WASHER("ore.washer"),
    /** Item to cut up item, Logs -> Planks */
    ITEM_SAWMILL("sawmill"),
    /** Item to items used to craft it, Chest -> 8 planks. Make sure to return the least valuable version of the recipe */
    ITEM_SALVAGER("salvager"),

    /**Ingot to metal item recipes, ingot -> steel plate, steel plate -> gun part plate */
    PLATE_PRESS("metal.press"),
    /**Plate to round casing recipes  */
    PLATE_ROLLER("metal.roller"),
    /** Rivets two items together */
    PLATE_RIVETER("metal.riveter"),

    /** Fluid + Fluid -> New Fluid */
    FLUID_MIXER("fluid.mixer"),
    /** Item -> Molten Fluid */
    FLUID_SMELTER("fluid.smelter"),
    /** Fluid -> Item, for non-molten items only */
    FLUID_SOLIDIFIER("fluid.solidifier"),
    /** Fluid -> Item, for molten fluids only */
    FLUID_CAST("fluid.cast");

    private IMachineRecipeHandler handler;
    public final String INTERNAL_NAME;

    MachineRecipeType(String name)
    {
        this.INTERNAL_NAME = name;
    }

    public IMachineRecipeHandler getHandler()
    {
        return handler;
    }

    public static IMachineRecipeHandler getHandler(String name)
    {
        for(MachineRecipeType type : values())
        {
            if(type.INTERNAL_NAME.equalsIgnoreCase(name))
            {
                return type.getHandler();
            }
        }
        return null;
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
