package com.builtbroken.mc.framework.recipe.cast;

import com.builtbroken.mc.api.items.crafting.ICastItem;
import com.builtbroken.mc.api.recipe.*;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.data.item.ItemStackWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

/**
 * Recipe handler for casting recipes
 * Created by robert on 1/9/2015.
 */
public class MRHandlerCast implements IMachineRecipeHandler<ItemStack, ICastingRecipe>
{
    /** Type to ItemStacks that are casts */
    public Map<String, HashMap<Fluid, List<ICastingRecipe>>> cast_map = new HashMap();
    public Map<String, Integer> cast_volume_map = new HashMap();
    public Map<ItemStackWrapper, String> castToTypeMap = new HashMap();

    public static MRHandlerCast INSTANCE = new MRHandlerCast();

    @Override
    public RecipeRegisterResult registerRecipe(ICastingRecipe recipe)
    {
        RecipeRegisterResult result = isValidRecipe(recipe);
        if (result == RecipeRegisterResult.REGISTERED)
        {
            if (!cast_map.containsKey(recipe.getCastType()))
            {
                cast_map.put(recipe.getCastType(), new HashMap());
            }
            for (FluidStack fluidStack : recipe.getValidInputs())
            {
                if (!cast_map.get(recipe.getCastType()).containsKey(fluidStack.getFluid()))
                {
                    cast_map.get(recipe.getCastType()).put(fluidStack.getFluid(), new ArrayList());
                }
                cast_map.get(recipe.getCastType()).get(fluidStack.getFluid()).add(recipe);
            }
        }
        return result;
    }

    public RecipeRegisterResult addType(String type, int volume)
    {
        if (!cast_volume_map.containsKey(type))
        {
            cast_volume_map.put(type, volume);
        }
        return RecipeRegisterResult.ALREADY_EXISTS;
    }

    public RecipeRegisterResult registerCast(ItemStack stack)
    {
        ItemStackWrapper wrap = new ItemStackWrapper(stack);
        if (!castToTypeMap.containsKey(wrap))
        {
            if (stack.getItem() instanceof ICastItem)
            {
                castToTypeMap.put(wrap, ((ICastItem) stack.getItem()).getCastType(stack));
                return RecipeRegisterResult.REGISTERED;
            }
            return RecipeRegisterResult.INVALID_TYPE;
        }
        return RecipeRegisterResult.ALREADY_EXISTS;
    }

    public int getVolumeForCast(String type)
    {
        return cast_volume_map.containsKey(type) ? cast_volume_map.get(type) : Engine.INGOT_VOLUME;
    }

    protected RecipeRegisterResult isValidRecipe(ICastingRecipe recipe)
    {
        if (recipe != null)
        {
            //TODO set up to be used for any recipe type that requires an ItemStack mold & fluid stack fill
            if (recipe.getType() != MachineRecipeType.FLUID_CAST.name())
            {
                return RecipeRegisterResult.INVALID_TYPE;
            }
            else if (recipe.getValidInputs() != null && recipe.getOutput() != null)
            {
                if (!(recipe.getOutput() instanceof ItemStack))
                {
                    return RecipeRegisterResult.INVALID_OUTPUT;
                }
                for (Object object : recipe.getValidInputs())
                {
                    if (!(object instanceof Fluid || object instanceof FluidStack))
                        return RecipeRegisterResult.INVALID_INPUT;
                }
                return RecipeRegisterResult.REGISTERED;
            }
        }
        return RecipeRegisterResult.FAILED;
    }

    @Override
    public ItemStack getRecipe(Object[] items, float extraChance, float failureChance)
    {
        if (items != null)
        {
            for (IMachineRecipe handler : getRecipes(items))
            {
                if (handler.shouldHandleRecipe(items))
                {
                    Object result = handler.handleRecipe(items, extraChance, failureChance);
                    if (result instanceof ItemStack)
                    {
                        return (ItemStack) result;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public final Collection<ICastingRecipe> getRecipes(Object[] items)
    {
        if (items != null && items.length > 0 && items[0] instanceof ItemStack && ((ItemStack) items[0]).getItem() instanceof ICastItem)
        {
            ItemStackWrapper stack = new ItemStackWrapper((ItemStack) items[0]);
            if (castToTypeMap.containsKey(stack))
            {
                String type = castToTypeMap.get(stack);
                if (cast_map.containsKey(type))
                {
                    HashMap<Fluid, List<ICastingRecipe>> map = cast_map.get(type);
                    if (items.length == 1)
                    {
                        List<ICastingRecipe> recipes = new ArrayList();
                        for (List<ICastingRecipe> l : map.values())
                        {
                            recipes.addAll(l);
                        }
                        return recipes;
                    }
                    else if (items.length == 2)
                    {
                        if (items[1] instanceof Fluid && map.containsKey(items[1]))
                        {
                            return map.get(items[1]);
                        }
                        else if (items[1] instanceof FluidStack && map.containsKey(((FluidStack) items[1]).getFluid()))
                        {
                            return map.get(((FluidStack) items[1]).getFluid());
                        }
                    }
                }
                return null;
            }
        }
        return null;
    }

    @Override
    public List<ICastingRecipe> getRecipes()
    {
        List<ICastingRecipe> list = new ArrayList();
        for (HashMap<Fluid, List<ICastingRecipe>> map : cast_map.values())
        {
            for (List<ICastingRecipe> l : map.values())
            {
                list.addAll(l);
            }
        }
        return list;
    }
}
