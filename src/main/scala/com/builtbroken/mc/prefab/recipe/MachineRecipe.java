package com.builtbroken.mc.prefab.recipe;

import com.builtbroken.mc.api.recipe.IMachineRecipe;
import com.builtbroken.mc.api.recipe.MachineRecipeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by robert on 1/10/2015.
 */
public abstract class MachineRecipe<O extends Object, I extends Object> implements IMachineRecipe<O, I>
{
    private final MachineRecipeType type;
    private List<I> inputs = new ArrayList();

    public MachineRecipe(MachineRecipeType type)
    {
        this.type = type;
    }

    @Override
    public MachineRecipeType getType()
    {
        return type;
    }

    public void addInputOption(I input)
    {
        if(!inputs.contains(input))
        {
            inputs.add(input);
        }
    }


    @Override
    public List<I> getValidInputs()
    {
        return inputs;
    }
}
