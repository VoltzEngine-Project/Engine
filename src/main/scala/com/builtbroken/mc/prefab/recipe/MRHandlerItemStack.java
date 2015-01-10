package com.builtbroken.mc.prefab.recipe;

import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.api.recipe.RecipeRegisterResult;
import com.google.common.collect.BiMap;
import li.cil.oc.api.driver.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robert on 1/9/2015.
 */
public class MRHandlerItemStack extends MRHandler<ItemStack>
{
    public MRHandlerItemStack(MachineRecipeType type)
    {
        super(type);
    }

    @Override
    protected boolean isValidInput(Object object)
    {
        return object instanceof ItemStack;
    }

    @Override
    protected boolean isValidOutput(Object object)
    {
        return object instanceof ItemStack;
    }

    @Override
    public String getKeyFor(Object input)
    {
        ItemStack stack = toOutputType(input);
        if(stack != null)
        {
            //Trick to make the equals method function correctly
            return stack.getItem().getUnlocalizedName() + "@" + stack.getItemDamage();
        }
        return null;
    }

    @Override
    protected ItemStack toOutputType(Object result)
    {
        return MachineRecipeType.toItemStack(result);
    }
}
