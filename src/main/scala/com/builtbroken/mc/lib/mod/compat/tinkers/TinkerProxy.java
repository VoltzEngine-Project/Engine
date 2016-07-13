package com.builtbroken.mc.lib.mod.compat.tinkers;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.resources.DefinedGenItems;
import com.builtbroken.mc.core.content.resources.GenMaterial;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.mod.ModProxy;
import com.builtbroken.mc.lib.mod.compat.Mods;
import mantle.utils.ItemMetaWrapper;
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

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/29/2015.
 */
public class TinkerProxy extends ModProxy
{
    public TinkerProxy()
    {
        super(Mods.TINKERS);
    }

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
                //System.out.println("Removed casting recipe for " + recipe.output);
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
                        ItemMetaWrapper item = new ItemMetaWrapper(new ItemStack(DefinedGenItems.GEAR.item, 1, mat.ordinal()));
                        try
                        {
                            Field field = Smeltery.class.getDeclaredField("smeltingList");
                            field.setAccessible(true);
                            Map<ItemMetaWrapper, FluidStack> smeltingList = (Map<ItemMetaWrapper, FluidStack>) field.get(Smeltery.instance);

                            field = Smeltery.class.getDeclaredField("temperatureList");
                            field.setAccessible(true);
                            Map<ItemMetaWrapper, Integer> temperatureList = (Map<ItemMetaWrapper, Integer>) field.get(Smeltery.instance);

                            field = Smeltery.class.getDeclaredField("renderIndex");
                            field.setAccessible(true);
                            Map<ItemMetaWrapper, ItemStack> renderIndex = (Map<ItemMetaWrapper, ItemStack>) field.get(Smeltery.instance);

                            smeltingList.remove(item);
                            temperatureList.remove(item);
                            renderIndex.remove(item);

                        } catch (Exception e)
                        {
                            Engine.instance.logger().error("Failed to reflect into tinkers to correct a duplication bug for gears.", e);
                        }
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

                    if (DefinedGenItems.SCREW.item != null && !DefinedGenItems.SCREW.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.SCREW.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 16));
                    }

                    if (DefinedGenItems.WIRE.item != null && !DefinedGenItems.WIRE.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.WIRE.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 16));
                    }

                    if (DefinedGenItems.AX_HEAD.item != null && !DefinedGenItems.AX_HEAD.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.AX_HEAD.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144 * 3));
                    }

                    if (DefinedGenItems.HOE_HEAD.item != null && !DefinedGenItems.HOE_HEAD.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.HOE_HEAD.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144 * 2));
                    }

                    if (DefinedGenItems.SHOVEL_HEAD.item != null && !DefinedGenItems.SHOVEL_HEAD.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.SHOVEL_HEAD.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144));
                    }

                    if (DefinedGenItems.SWORD_BLADE.item != null && !DefinedGenItems.SWORD_BLADE.ignoreMaterials.contains(mat))
                    {
                        //TODO get block per material
                        Smeltery.addMelting(new ItemStack(DefinedGenItems.SWORD_BLADE.item, 1, mat.ordinal()), Blocks.iron_block, 0, 600, new FluidStack(fluid, 144 * 2));
                    }
                }
            }
        }
    }
}
