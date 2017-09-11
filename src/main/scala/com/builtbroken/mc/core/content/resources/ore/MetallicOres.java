package com.builtbroken.mc.core.content.resources.ore;

import com.builtbroken.mc.lib.world.generator.OreGeneratorSettings;
import net.minecraft.block.Block;

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
}
