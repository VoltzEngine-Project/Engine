package com.builtbroken.mc.core.content.resources;

import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.world.generator.OreGenReplaceStone;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A list of gem ores that can be requested in Voltz Engine.
 * @author - Kolatra
 */
public enum GemOres
{
    Amazonite(5, 20, 2, 15),
    SmokeyQuartz(5, 20, 2, 15),
    OnyxBlack(1, 5, 3, 10),
    OnyxRed(15, 60, 5, 20),
    GarnetGreen(15, 60, 5, 20),
    GarnetOrange(10, 50, 3, 18),
    GarnetRed(10, 50, 3, 18),
    GarnetYellow(10, 50, 3, 18);

    private int minY = 1;
    private int maxY = 100;
    private int amountPerChunk = 16;
    private int amountPerBranch = 5;

    private Block block;
    private String oreDictName;

    GemOres(int min, int max, int amountPerBranch, int amountPerChunk)
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
        for (GemOres ore : GemOres.values())
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
