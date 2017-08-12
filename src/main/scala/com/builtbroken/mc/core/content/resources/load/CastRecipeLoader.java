package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.recipe.cast.CastingRecipe;
import com.builtbroken.mc.framework.recipe.cast.MRHandlerCast;
import com.builtbroken.mc.framework.recipe.extend.MachineRecipeLoader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark on 6/23/2015.
 */
@Deprecated //TODO transition to JSON
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
                ArrayList ores = OreDictionary.getOres(oreName);
                if (ores == null || ores.size() == 0)
                {
                    Engine.logger().error("[CastRecipeLoader]Attempted to create a recipe with an oreName[" + oreName + "] without an ItemStack.  Ores: " + ores);
                    continue;
                }
                String fluidName = oreName.replace("ingot", "").toLowerCase();
                Fluid fluid = FluidRegistry.getFluid(fluidName);
                if (fluid != null)
                {
                    recipes.add(newRecipe("ingot", fluid, OreDictionary.getOres(oreName).get(0)));
                }
                //Conversion for tinker's fluid names
                fluid = FluidRegistry.getFluid(fluidName + ".molten");
                if (fluid != null)
                {
                    recipes.add(newRecipe("ingot", fluid, OreDictionary.getOres(oreName).get(0)));
                }
            }
            else if (oreName.contains("nugget"))
            {
                ArrayList ores = OreDictionary.getOres(oreName);
                if (ores == null || ores.size() == 0)
                {
                    Engine.logger().error("[CastRecipeLoader]Attempted to create a recipe with an oreName[" + oreName + "] without an ItemStack.  Ores: " + ores);
                    continue;
                }
                String fluidName = oreName.replace("nugget", "").toLowerCase();
                Fluid fluid = FluidRegistry.getFluid(fluidName);
                if (fluid != null)
                {
                    recipes.add(newRecipe("nugget", fluid, OreDictionary.getOres(oreName).get(0)));
                }
                //Conversion for tinker's fluid names
                fluid = FluidRegistry.getFluid(fluidName + ".molten");
                if (fluid != null)
                {
                    recipes.add(newRecipe("nugget", fluid, OreDictionary.getOres(oreName).get(0)));
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
