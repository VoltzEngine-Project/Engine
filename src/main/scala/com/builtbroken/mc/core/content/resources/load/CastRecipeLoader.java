package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.prefab.recipe.cast.CastingRecipe;
import com.builtbroken.mc.prefab.recipe.cast.MRHandlerCast;
import com.builtbroken.mc.prefab.recipe.extend.MachineRecipeLoader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Map;

/**
 * Created by Dark on 6/23/2015.
 */
public class CastRecipeLoader extends MachineRecipeLoader<CastingRecipe>
{
    public CastRecipeLoader()
    {
        super(MachineRecipeType.FLUID_CAST.name());
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
        //TODO Get list of all ingots & compare against fluids to create a list of recipes
        System.out.println("Testing casting recipe generator");
        for (Map.Entry<String, Fluid> fluid : FluidRegistry.getRegisteredFluids().entrySet())
        {
            System.out.println("\tFluid:  K:" + fluid.getKey() + "  V:" + fluid.getValue());
        }
        for (String oreName : OreDictionary.getOreNames())
        {
            System.out.println("\tOreName: " + oreName);
            if (oreName.contains("ingot"))
            {
                String fluidName = oreName.replace("", "ingot").toLowerCase();
                System.out.println("\tFluidName: " + fluidName);
                Fluid fluid = FluidRegistry.getFluid(fluidName);
                System.out.println("\tFluid: " + fluid);
                if (fluid != null)
                {
                    recipes.add(newRecipe("ingot", fluid, OreDictionary.getOres(oreName).get(0)));
                    if (Engine.runningAsDev)
                        Engine.instance.logger().info("Generated casting recipe for " + oreName + " with fluid " + fluid);
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
