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
    public static final int MAX_ORE_BLOCKS = 2;
    public static final int ORE_BLOCK_ID = UniversalElectricity.getConfigID(UniversalElectricity.CONFIGURATION, "Universal Ores", 3968, true);
    public static final BlockUEOre[] blockOre = {new BlockUEOre(ORE_BLOCK_ID), new BlockUEOre(ORE_BLOCK_ID + 1)};

    /**
     * Adds and automatically generates a new ore in the ground.
     * Uses metadata so your ore will not take up any block IDs.
     * @id - The ID can not be greater than 32
     * @param A UniversalOreData class consisting of all data needed to generate this ore.
     * @return True if the ore is successfully added.
     */
    public static boolean addOre(int newID, OreData universalOre)
    {
        if (newID > 16 * MAX_ORE_BLOCKS)
        {
            throw new RuntimeException("Universal ore ID is too high!");
        }
        
        /**
         * Check each and every ore. If the ore already exists (added by another
         * UE mod or such) then it will not add the ore and return false.
         */

        for(BlockUEOre oreBlock : blockOre)
        {
        	for(OreData oreData : oreBlock.ores)
        	{
        		if(oreData != null)
        		{
	        		if(oreData.oreDictionaryName == universalOre.oreDictionaryName)
	        		{
	        			return false;
	        		}
        		}
        	}
        }
        
        int i = (int)Math.floor(newID / 16);
        
        //Change the metadata to the correct amount
        int metadata = newID - 16 * i;

        //Assign a metdata to the ore
        if (blockOre[i].ores[metadata] != null)
        {
            throw new RuntimeException("Universal Electricity ore " + newID + " ID has been taken already!");
        }

        blockOre[i].ores[metadata] = universalOre;
        ModLoader.addName(new ItemStack(blockOre[i], 1, metadata), universalOre.name);
        MinecraftForge.setBlockHarvestLevel(blockOre[i], metadata, universalOre.harvestTool, universalOre.harvestLevel);
        OreDictionary.registerOre(universalOre.oreDictionaryName, new ItemStack(blockOre[i], 1, metadata));
        
        return true;
    }

    /**
     * Gets the block class of an ore depending on the ore ID given by addOre()
     * @param oreID - The ore ID
     * @return The block class for the ore
     */
    public static BlockUEOre getOre(int oreID)
    {
        if (oreID <= 16 * MAX_ORE_BLOCKS)
        {
            int i = (int)Math.floor(oreID / 16);

            if (blockOre[i] != null)
            {
                return blockOre[i];
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
        if (id <= 16 * MAX_ORE_BLOCKS)
        {
            int i = (int)Math.floor(id / 16);

            if (blockOre[i] != null)
            {
                return id - 16 * i;
            }
        }

        return -1;
    }
    
    /**
     * Gets the item stack corresponding to your ore ID
     * @param id
     * @return
     */
    public static ItemStack getOreItemStack(int id)
    {
    	return new ItemStack(getOre(id), 1, getOreMetadata(id));
    }

    //Generate the surface of the world based on all registered universal ores.
    public static void generateSurface(World world, Random rand, int chunkX, int chunkZ)
    {
        for (int j = 0; j < blockOre.length; j ++)
        {
            if (blockOre[j] != null)
            {
                BlockUEOre oreBlock = blockOre[j];

                for (int i = 0; i < oreBlock.ores.length; i++)
                {
                    if (oreBlock.ores[i] != null)
                    {
                        if (oreBlock.ores[i].shouldGenerate)
                        {
                        	WorldGenMinable worldGenMinable = new WorldGenMinable(oreBlock.blockID, oreBlock.ores[i].amountPerBranch, i);

                            //Generate Copper
                            for (int l = 0; l < oreBlock.ores[i].amountPerChunk; l++)
                            {
                                int x = chunkX + rand.nextInt(16);
                                int y = rand.nextInt(oreBlock.ores[i].maxGenerateLevel) + oreBlock.ores[i].minGenerateLevel;
                                int z = chunkZ + rand.nextInt(16);
                            	worldGenMinable.generate(world, rand, x, y, z);
                            }
                        }
                    }
                }
            }
        }
    }
}
