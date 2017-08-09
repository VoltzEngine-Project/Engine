package com.builtbroken.mc.core.content.resources.ore;

import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.world.generator.OreGenReplace;
import com.builtbroken.mc.lib.world.generator.OreGeneratorSettings;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

/**
 * List of ores that generate in Voltz Engine
 * Created by robert on 1/11/2015.
 */
@Deprecated //TODO move to JSON
public enum MetallicOres
{
    TIN(15, 120, 7, 40),
    COPPER(15, 120, 7, 60),
    SILVER(15, 60, 5, 4),
    LEAD(15, 30, 5, 4),
    ZINC(20, 40, 5, 20),
    NICKEL(15, 60, 5, 8),
    BAUXITE("oreAluminum", 15, 60, 5, 10),
    MAGNESITE("oreMagnesium", 5, 10, 3, 6),
    URANIUM(5, 30, 2, 4),
    PLATINUM(15, 60, 3, 10);

    public final OreGeneratorSettings oreGenSettings;

    private Block block;
    private String oreDictName;

    MetallicOres(String oreName, int min, int max, int amountPerBranch, int amountPerChunk)
    {
        this(min, max, amountPerBranch, amountPerChunk);
        this.oreDictName = oreName;
    }

    MetallicOres(int min, int max, int amountPerBranch, int amountPerChunk)
    {
        oreGenSettings = new OreGeneratorSettings(min, max, amountPerBranch, amountPerChunk);
    }

    public ItemStack stack()
    {
        return stack(1);
    }

    public ItemStack stack(int stackSize)
    {
        return new ItemStack(block, stackSize, ordinal());
    }

    public static void registerSet(Block block, Configuration config)
    {
        if (block != null)
        {
            for (MetallicOres ore : MetallicOres.values())
            {
                if (config.getBoolean("" + LanguageUtility.capitalizeFirst(ore.name()) + "_Ore", "WorldGen", true, "Enables generation of the ore in the world"))
                {
                    ore.block = block;
                    ItemStack stack = ore.stack();
                    GameRegistry.registerWorldGenerator(new OreGenReplace(ore.getOreName(), block, ore.ordinal(), ore.oreGenSettings, "pickaxe", 1), ore.ordinal());
                    OreDictionary.registerOre(ore.oreDictName, stack);
                }
            }
        }
    }

    public String getOreName()
    {
        if (oreDictName == null)
        {
            oreDictName = "ore" + LanguageUtility.capitalizeFirst(name().toLowerCase());
        }
        return oreDictName;
    }
}
