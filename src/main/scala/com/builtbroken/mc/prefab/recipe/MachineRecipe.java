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
    protected final String type;
    protected List<I> inputs = new ArrayList();

    public MachineRecipe(String type)
    {
        this.type = type;
    }

    @Override
    public String getType()
    {
        return type;
    }

    public MachineRecipe addInputOption(I input)
    {
        if(!inputs.contains(input))
        {
            inputs.add(input);
        }
        return this;
    }


    @Override
    public List<I> getValidInputs()
    {
        return inputs;
    }

    @Override
    public String toString()
    {
        String clazzName = getClass().getSimpleName();
        clazzName = clazzName.replaceFirst("MR", "MachineRecipe");
        return clazzName + "[" + getOutput()+ "]";
    }

    @Override
    public boolean equals(Object object)
    {
        if(object instanceof IMachineRecipe)
        {
            IMachineRecipe other = (IMachineRecipe) object;
            if(other.getType() == getType())
            {
                if(isOutputEqual(other.getOutput()))
                {
                    return other.getValidInputs().equals(getValidInputs());
                }
            }
        }
        return false;
    }

    /** Used by the equals method to compare outputs, allows for the recipe to
     * override the default output instance's equals method */
    public boolean isOutputEqual(Object out)
    {
        return out == getOutput();
    }
}
