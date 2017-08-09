package com.builtbroken.mc.framework.recipe.item;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * Created by robert on 1/9/2015.
 */
public class MRSmelterHandler extends MRHandlerItemStack
{
    public MRSmelterHandler()
    {
        super(MachineRecipeType.ITEM_SMELTER.INTERNAL_NAME);
    }

    @Override
    public ItemStack getRecipe(Object[] items, float extraChance, float failureChance)
    {
        if (items != null)
        {
            ItemStack result = super.getRecipe(items, extraChance, failureChance);
            if (result == null && items.length == 1 && items[0] instanceof ItemStack)
            {
                result = FurnaceRecipes.smelting().getSmeltingResult((ItemStack) items[0]);
            }
            if (result != null)
            {
                ItemStack re = result.copy();
                if (re.stackSize <= 0)
                {
                    re.stackSize = 1;
                }
                return re;
            }
            return result;
        }
        return null;
    }
}
