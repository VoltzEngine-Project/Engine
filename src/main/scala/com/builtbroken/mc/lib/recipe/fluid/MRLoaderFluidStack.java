package com.builtbroken.mc.lib.recipe.fluid;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.recipe.extend.MachineRecipeLoader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Dark on 6/21/2015.
 */
public class MRLoaderFluidStack extends MachineRecipeLoader<MRFluidStack>
{
    public MRLoaderFluidStack(String type)
    {
        super(type);
    }

    protected MRFluidStack newRecipe(Fluid fluid, int ingots, ItemStack input)
    {
        MRFluidStack recipe = new MRFluidStack(type, new FluidStack(fluid, ingots * Engine.INGOT_VOLUME));
        recipe.addInputOption(input);
        return recipe;
    }

    protected MRFluidStack newRecipe(FluidStack stack, ItemStack input)
    {
        MRFluidStack recipe = new MRFluidStack(type, stack);
        recipe.addInputOption(input);
        return recipe;
    }

    protected Fluid registerFluid(String name)
    {
        Fluid fluid = new Fluid(name);
        if(!FluidRegistry.registerFluid(fluid) && FluidRegistry.getFluid(name) == null)
        {
            Engine.instance.logger().error("Failed to register fluid " + name + " with no fluid registered to name");
        }
        return FluidRegistry.getFluid(name);
    }

    protected Fluid registerMoltenFluid(String name)
    {
        Fluid fluid = new Fluid(name);
        fluid.setViscosity(16000);
        fluid.setLuminosity(12);
        if(!FluidRegistry.registerFluid(fluid) && FluidRegistry.getFluid(name) == null)
        {
            Engine.instance.logger().error("Failed to register fluid " + name + " with no fluid registered to name");
        }

        return FluidRegistry.getFluid(name);
    }

    protected void processOreRecipes(List<MRFluidStack> recipes, Fluid output, int ingots, String input)
    {
        List<ItemStack> oreList = OreDictionary.getOres(input);
        for(ItemStack stack : oreList)
        {
            if(stack != null && stack.getItem() != null)
            {
                recipes.add(newRecipe(output, ingots, stack));
            }
        }
    }
}
