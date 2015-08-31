package com.builtbroken.mc.core.content.resources;

import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.world.generator.OreGenReplaceStone;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

/**
 * List of ores that generate in Voltz Engine
 * Created by robert on 1/11/2015.
 */
public enum Ores
{
    TIN(15, 60, 5, 15),
    COPPER(15, 60, 5, 20),
    SILVER(15, 60, 5, 4),
    LEAD(15, 30, 5, 4),
    ZINC(20, 40, 5, 20),
    NICKEL(15, 60, 5, 8),
    BAUXITE(15, 60, 5, 10),
    MAGNESITE(5, 10, 3, 6),
    URANIUM(5, 30, 2, 4),
    PLATINUM(15, 60, 3, 10);

    private int minY = 1;
    private int maxY = 100;
    private int amountPerChunk = 16;
    private int amountPerBranch = 5;

    private Block block;
    private String oreDictName;

    Ores(int min, int max, int amountPerBranch, int amountPerChunk)
    {
        this.minY = min;
        this.maxY = max;
        this.amountPerBranch = amountPerBranch;
        this.amountPerChunk = amountPerChunk;
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
        for (Ores ore : values())
        {
            if (config.getBoolean("" + LanguageUtility.capitalizeFirst(ore.name()) + "_Ore", "WorldGen", true, "Enables generation of the ore in the world"))
            {
                ore.block = block;
                ore.oreDictName = "ore" + LanguageUtility.capitalizeFirst(ore.name().toLowerCase());
                ItemStack stack = ore.stack();
                GameRegistry.registerWorldGenerator(new OreGenReplaceStone(ore.oreDictName, stack, ore.minY, ore.maxY, ore.amountPerChunk, ore.amountPerBranch, "pickaxe", 1), 1);
                OreDictionary.registerOre(ore.oreDictName, stack);
            }
        }
    }
}
