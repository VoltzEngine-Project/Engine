package com.builtbroken.mc.core.resources;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import com.builtbroken.mc.prefab.ore.OreGenReplaceStone;
import com.builtbroken.mc.prefab.ore.OreGenerator;
import com.builtbroken.mc.lib.helper.LanguageUtility;

/**
 * Generic set of ore types that are commonly found in most mods, and MC itself
 *
 * @author Darkguardsman
 */
public enum DefinedResources
{
	COPPER(15, 60, 5, 20),
	TIN(15, 60, 5, 15),
    BRONZE(),
	IRON(),
    STEEL(),
	SILVER(15, 60, 5, 4),
	GOLD(),
    LEAD(15, 30, 5, 4),
    ZINC(20, 40, 5, 20),
    NICKEL(15, 60, 5, 8),
    ALUMINIUM(15, 60, 5, 10), //bauxite, TODO implement to generate in deserts
    MAGNESIUM(5, 10, 3, 6), //Magnesite, TODO implement as crystal on cave walls
    URANIUM(5, 30, 2, 4), //uraninite, UO2, TODO implement custom generator to create rare pockets,
    BRASS();

    public final boolean generateOres;
    public boolean requested = false;
    public Block block = null;
    public int meta = -1;

    //TODO add config for the following to test alternate settings
    //TODO add world creation option for each
    //TODO add easy world creation config to generation amount(NONE, REDUCED, NORMAL, INCREASED, ABUNDANT)



    private int minY = 1;
    private int maxY = 100;
    private int amountPerChunk = 16;
    private int amountPerBranch = 5;

	private DefinedResources()
	{
        this.generateOres = false;
	}

    private DefinedResources(int min, int max, int amountPerBranch, int amountPerChunk)
    {
        this(min, max, amountPerBranch, amountPerChunk, new DefinedGenItems[0]);
    }
    private DefinedResources(int min, int max, int amountPerBranch, int amountPerChunk, DefinedGenItems... itemsToGen)
    {
        this.generateOres = true;
        this.minY = min;
        this.maxY = max;
        this.amountPerBranch = amountPerBranch;
        this.amountPerChunk = amountPerChunk;
    }

    /** Registers first 16 ores
     *
     * @param block - block to use for the generate, uses metadata
     * @param config - config to check if the generate is allowed to generate
     */
    public static boolean registerSet(int set, Block block, Configuration config)
    {
        int meta = 0;
        for(int i = set * 16; i < (set * 16) + 16 && i < values().length; i++)
        {
            DefinedResources resource = values()[i];
            resource.block = block;
            resource.meta = meta;
            if (resource.requested && resource.generateOres && config.getBoolean("ore_" + resource.name().toLowerCase(), "ORE_GENERATOR", true, ""))
            {
                OreGenerator generator = new OreGenReplaceStone("ore" + LanguageUtility.capitalizeFirst(resource.name().toLowerCase()), new ItemStack(block, 1, meta), resource.minY, resource.maxY, resource.amountPerChunk, resource.amountPerBranch, "pickaxe", 1);
                GameRegistry.registerWorldGenerator(generator, meta);
            }
            meta++;
        }
        return meta >= 15;
    }
}
