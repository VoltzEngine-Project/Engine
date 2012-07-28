package net.minecraft.src.universalelectricity.ore;

import java.util.Random;

import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenMinable;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.oredict.OreDictionary;
import net.minecraft.src.universalelectricity.UniversalElectricity;

public class UEOreManager
{
    public static final int maxOreBlocks = 2;
    public static int oreBlockID = UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Universal Ores", 3968, true);
    public static final BlockUEOre[] BlockOre = {new BlockUEOre(oreBlockID), new BlockUEOre(oreBlockID + 1)};

    /**
     * Adds and automatically generates a new ore in the ground.
     * Uses metadata so your ore will not take up any block IDs.
     * @id - The ID can not be greater than 32
     * @param A UniversalOreData class consisting of all data needed to generate this ore.
     */
    public static void addOre(int newID, OreData universalOre) throws RuntimeException
    {
        if (newID > 16 * maxOreBlocks)
        {
            throw new RuntimeException("Universal ore ID is too high!");
        }

        int i = (int)Math.floor(newID / 16);
        //Change the metadata to the correct amount
        int metadata = newID - 16 * i;

        //Assign a metdata to the ore
        if (BlockOre[i].ores[metadata] != null)
        {
            throw new RuntimeException("Universal Electricity ore " + newID + " ID has been taken already!");
        }

        BlockOre[i].ores[metadata] = universalOre;
        ModLoader.addName(new ItemStack(BlockOre[i], 1, metadata), universalOre.name);
        MinecraftForge.setBlockHarvestLevel(BlockOre[i], metadata, universalOre.harvestTool, universalOre.harvestLevel);
        OreDictionary.registerOre(universalOre.oreDiectionaryName, new ItemStack(BlockOre[i], 1, metadata));
    }

    /**
     * Gets the block class of an ore depending on the ore ID given by addOre()
     * @param oreID - The ore ID
     * @return The block class for the ore
     */
    public static BlockUEOre getOre(int oreID)
    {
        if (oreID <= 16 * maxOreBlocks)
        {
            int i = (int)Math.floor(oreID / 16);

            if (BlockOre[i] != null)
            {
                return BlockOre[i];
            }
        }

        return null;
    }

    /**
     * Gets the metadata of a specified ore ID. Use this metadata for crafting recipes and
     * NOT your ore ID or it will not work.
     * @param id - The ID of the Universal Electricity ore
     * @return The id of that ore
     */
    public static int getOreMetadata(int id)
    {
        if (id <= 16 * maxOreBlocks)
        {
            int i = (int)Math.floor(id / 16);

            if (BlockOre[i] != null)
            {
                return id - 16 * i;
            }
        }

        return -1;
    }

    //Generate the surface of the world based on all registered universal ores.
    public static void generateSurface(World world, Random rand, int chunkX, int chunkZ)
    {
        for (int var1 = 0; var1 < BlockOre.length; var1 ++)
        {
            if (BlockOre[var1] != null)
            {
                BlockUEOre blockOre = BlockOre[var1];

                for (int i = 0; i < blockOre.ores.length; i++)
                {
                    if (blockOre.ores[i] != null)
                    {
                        if (blockOre.ores[i].shouldGenerate)
                        {
                            //Generate Copper
                            for (int l = 0; l < blockOre.ores[i].amountPerChunk; l++)
                            {
                                int i1 = chunkX + rand.nextInt(16);
                                int j1 = rand.nextInt(blockOre.ores[i].maxGenerateLevel) + blockOre.ores[i].minGenerateLevel;
                                int k1 = chunkZ + rand.nextInt(16);
                                (new WorldGenMinable(blockOre.blockID, blockOre.ores[i].amountPerBranch, i)).generate(world, rand, i1, j1, k1);
                            }
                        }
                    }
                }
            }
        }
    }
}
