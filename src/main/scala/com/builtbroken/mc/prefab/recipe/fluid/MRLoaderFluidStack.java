package com.builtbroken.mc.prefab.recipe.fluid;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.prefab.recipe.extend.MachineRecipeLoader;
import com.builtbroken.mc.prefab.recipe.item.MRItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
        if(FluidRegistry.registerFluid(fluid))
        {
            return fluid;
        }
        return FluidRegistry.getFluid(name);
    }

    protected Fluid registerMoltenFluid(String name)
    {
        Fluid fluid = new Fluid(name);
        if(FluidRegistry.registerFluid(fluid))
        {
            fluid.setViscosity(16000);
            fluid.setLuminosity(12);
            return fluid;
        }
        return FluidRegistry.getFluid(name);
    }
}
