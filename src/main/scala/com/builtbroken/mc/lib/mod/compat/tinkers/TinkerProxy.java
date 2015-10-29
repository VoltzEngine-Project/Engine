package com.builtbroken.mc.lib.mod.compat.tinkers;

import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;

import java.util.Iterator;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
public class TinkerProxy extends AbstractLoadable
{
    @Override
    public void postInit()
    {
        //Remove existing gear recipes
        LiquidCasting castingTableRecipes = TConstructRegistry.getTableCasting();
        Iterator<CastingRecipe> it = castingTableRecipes.getCastingRecipes().iterator();
        while (it.hasNext())
        {
            CastingRecipe recipe = it.next();
            if (recipe.output.getItem() == DefinedGenItems.GEAR.item)
            {
                it.remove();
                System.out.println("Removed casting recipe for " + recipe.output);
            }
        }

        //Add smelting values
        for (GenMaterial mat : GenMaterial.values())
        {
            if (OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(mat.name().toLowerCase()), false).size() > 0)
            {
                Fluid fluid = FluidRegistry.getFluid(mat.name().toLowerCase() + ".molten");
                if (fluid != null)
                {
                    if (DefinedGenItems.GEAR.item != null && !DefinedGenItems.GEAR.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.GEAR.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144));
                    }

                    if (DefinedGenItems.ROD.item != null && !DefinedGenItems.ROD.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.ROD.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 72));
                    }

                    if (DefinedGenItems.PLATE.item != null && !DefinedGenItems.PLATE.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.PLATE.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144));
                    }

                    if (DefinedGenItems.RUBBLE.item != null && !DefinedGenItems.RUBBLE.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.RUBBLE.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144));
                    }

                    if (DefinedGenItems.DUST.item != null && !DefinedGenItems.DUST.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.DUST.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144));
                    }

                    if (DefinedGenItems.DUST_IMPURE.item != null && !DefinedGenItems.DUST_IMPURE.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.DUST_IMPURE.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144));
                    }
                }
            }
        }
    }
}
