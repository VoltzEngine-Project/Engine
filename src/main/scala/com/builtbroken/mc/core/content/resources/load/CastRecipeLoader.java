package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.prefab.recipe.cast.CastingRecipe;
import com.builtbroken.mc.prefab.recipe.cast.MRHandlerCast;
import com.builtbroken.mc.prefab.recipe.extend.MachineRecipeLoader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Dark on 6/23/2015.
 */
public class CastRecipeLoader extends MachineRecipeLoader<CastingRecipe>
{
    public CastRecipeLoader()
    {
        super(MachineRecipeType.FLUID_CAST.INTERNAL_NAME);
    }

    @Override
    public void init()
    {

    }

    @Override
    public void postInit()
    {
        super.init();
    }

    @Override
    protected void generateRecipes(List<CastingRecipe> recipes)
    {
        for (String oreName : OreDictionary.getOreNames())
        {
            if (oreName.contains("ingot"))
            {
                String fluidName = oreName.replace("ingot", "").toLowerCase();
                Fluid fluid = FluidRegistry.getFluid(fluidName);
                if (fluid != null)
                {
                    recipes.add(newRecipe("ingot", fluid, OreDictionary.getOres(oreName).get(0)));
                }
            }
        }
    }

    public CastingRecipe newRecipe(String type, Fluid fluid, ItemStack output)
    {
        CastingRecipe recipe = new CastingRecipe(type, output);
        recipe.getValidInputs().add(new FluidStack(fluid, MRHandlerCast.INSTANCE.getVolumeForCast(type)));
        return recipe;
    }
}
