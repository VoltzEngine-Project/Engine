package com.builtbroken.mc.core.content.resources.load;

import com.builtbroken.mc.api.recipe.MachineRecipeType;
import com.builtbroken.mc.prefab.recipe.fluid.MRFluidStack;
import com.builtbroken.mc.prefab.recipe.fluid.MRLoaderFluidStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.List;

/**
 * Created by Dark on 6/21/2015.
 */
public class FluidSmelterRecipeLoad extends MRLoaderFluidStack
{
    public static boolean loaded = false;

    public static Fluid moltenIron;
    public static Fluid moltenGold;

    public FluidSmelterRecipeLoad()
    {
        super(MachineRecipeType.FLUID_SMELTER.INTERNAL_NAME);
    }

    @Override
    protected void generateRecipes(List<MRFluidStack> recipes)
    {
        ///TODO add a config to not generate new fluids if they don't exist
        loaded = true;
        moltenIron = registerMoltenFluid("iron.molten");
        recipes.add(newRecipe(moltenIron, 2, new ItemStack(Blocks.iron_ore)));
        recipes.add(newRecipe(moltenIron, 1, new ItemStack(Items.iron_ingot)));


        moltenGold = registerMoltenFluid("gold.molten");
        recipes.add(newRecipe(moltenGold, 2, new ItemStack(Blocks.gold_ore)));
        recipes.add(newRecipe(moltenGold, 1, new ItemStack(Items.gold_ingot)));
    }
}
