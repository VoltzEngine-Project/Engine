package net.minecraft.src.universalelectricity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.mod_UniversalElectricity;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.forge.oredict.OreDictionary;

public class UniversalElectricity
{
	public static final Configuration configuration = new Configuration(new File(Minecraft.getMinecraftDir(), "config/UniversalElectricity/UniversalElectricity.cfg"));
	
	private static final List<NetworkMod> Addons = new ArrayList<NetworkMod>();
	
	//Universal Blocks
	public static final int maxOreBlocks = 2;
	public static int oreBlockID = UniversalElectricity.getConfigID(configuration, "Universal Ores", 198, true);
	public static final UEBlockOre[] BlockOre = {new UEBlockOre(oreBlockID), new UEBlockOre(oreBlockID+1)};

	public static mod_UniversalElectricity getInstance()
	{
		return mod_UniversalElectricity.instance;
	}
	
	public static void registerAddon(NetworkMod networkmod, String version)
	{
		String[] versionNumbers = getVersion().split("\\.");
		String[] addonVersionNumbers = version.split("\\.");

		if (Integer.parseInt(addonVersionNumbers[0]) != Integer.parseInt(versionNumbers[0]))
        {
            MinecraftForge.killMinecraft("Universal Electricity", "Add-on major version mismatch, expecting " + getVersion());
        }
		else if (Integer.parseInt(addonVersionNumbers[1]) > Integer.parseInt(versionNumbers[1]))
        {
            MinecraftForge.killMinecraft("Universal Electricity", "Universal Electricity too old, need at least " + version);
        }
		else if (Integer.parseInt(addonVersionNumbers[1]) < Integer.parseInt(versionNumbers[1]))
        {
            MinecraftForge.killMinecraft("Universal Electricity", "Add-on minor version mismatch, need at least " + getVersion());
        }
        else if(Integer.parseInt(addonVersionNumbers[2]) != Integer.parseInt(versionNumbers[2]))
        {
        	System.out.println("Universal Electricity add-on minor version "+version+" mismatch with version " + getVersion());
        }
		
		Addons.add(networkmod);
		System.out.println("Loaded Universal Add-On: "+networkmod.getName());
	}
	
	public static void load()
	{		
		ModLoader.registerBlock(BlockOre[0], ItemUniversalOre0.class);
		ModLoader.registerBlock(BlockOre[1], ItemUniversalOre1.class);
	}
	
	//Generate the surface of the world based on all registered universal ores.
	public static void generateSurface(World world, Random rand, int chunkX, int chunkZ)
    {
		for(int var1 = 0; var1 < BlockOre.length; var1 ++)
		{
			if(BlockOre[var1] != null)
			{
				UEBlockOre blockOre = BlockOre[var1];
				
				for(int i = 0; i < blockOre.ores.length; i++)
				{
					if(blockOre.ores[i] != null)
					{
						if(blockOre.ores[i].shouldGenerate)
				    	{
					    	//Generate Copper
					        for(int l = 0; l < blockOre.ores[i].amountPerChunk; l++)
					        {
					            int i1 = chunkX + rand.nextInt(16);
					            int j1 = rand.nextInt(blockOre.ores[i].maxGenerateLevel) + blockOre.ores[i].minGenerateLevel;
					            int k1 = chunkZ + rand.nextInt(16);
					            (new UEWorldGenMinableMetadata(blockOre.blockID, blockOre.ores[i].amountPerBranch, i)).generate(world, rand, i1, j1, k1);
					        }
				    	}
					}
				}
			}
		}
    }
	
	public static String getVersion()
	{
		return "0.3.1";
	}
	
	//------------------ FUNCTIONS AND HOOKS ----------------------
	/**
	 * Adds and automatically generates a new ore in the ground.
	 * Uses metadata so your ore will not take up any block IDs.
	 * @id - The ID can not be greater than 32
	 * @param A UniversalOreData class consisting of all data needed to generate this ore.
	 */
	public static void addOre(int newID, UniversalOreData universalOre)
    {
		if(newID > 16*maxOreBlocks)
		{
			throw new RuntimeException("Universal ore ID is too high!");
		}

		int i = (int)Math.floor(newID/16);
		
		//Change the metadata to the correct amount
		int metadata = newID - 16*i;
		
		//Assign a metdata to the ore
		if(BlockOre[i].ores[metadata] != null)
		{
			throw new RuntimeException("Universal Electricity ore "+newID+" ID has been taken already!");
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
	public static UEBlockOre getOre(int oreID)
	{
		if(oreID <= 16*maxOreBlocks)
		{
			int i = (int)Math.floor(oreID/16);
			
			if(BlockOre[i] != null)
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
		if(id <= 16*maxOreBlocks)
		{
			int i = (int)Math.floor(id/16);
			
			if(BlockOre[i] != null)
			{
				return id - 16*i;
			}
		}

		return -1;
	}
	
	/**
	 * Returns the amount of amps.
	 * @param watts
 	 * @param volts
	 * @return The amount of amps
	 */
	public static double getAmps(int watts, int volts)
	{		
		return (double)watts/(double)volts;
	}
	
	/**
	 * Return a string with the amount of amps for displaying.
	 * @param amps
	 * @return The string for displaying amps
	 */
	public static String getAmpDisplay(double amps)
	{
		String displayAmps;
		
		int i = Double.compare(amps, (double)1);
		int j = Double.compare(amps, (double)1000);
		
		if(i < 0)
		{
			displayAmps = roundTwoDecimals(amps*1000)+" mA";
		}else if(j > 0)
		{
			displayAmps = roundTwoDecimals(amps/1000)+" KA";
		}
		else
		{
			displayAmps = roundTwoDecimals(amps)+" A";
		}
		
		return displayAmps;
	}
	
	public static String getAmpDisplayFull(double amps)
	{
		String displayAmps;
		
		int i = Double.compare(amps, (double)1);
		int j = Double.compare(amps, (double)1000);
		
		if(i < 0)
		{
			displayAmps = roundTwoDecimals(amps*1000)+" Milliamps";
		}else if(j > 0)
		{
			displayAmps = roundTwoDecimals(amps/1000)+" Kiloamps";
		}
		else
		{
			displayAmps = roundTwoDecimals(amps)+" Amps";
		}
		
		return displayAmps;
	}
	
	/**
	 * Return a string with the amount of volts for displaying.
	 * @param volts
	 * @return The string for displaying volts
	 */
	public static String getVoltDisplay(int volts)
	{
		String displayVolt;
		
		if(volts > 1000000)
		{
			displayVolt = roundTwoDecimals((double)volts/1000000)+" MV";
		}
		if(volts > 1000)
		{
			displayVolt = roundTwoDecimals((double)volts/1000)+" KV";
		}
		else
		{
			displayVolt = volts+" V";
		}
		
		return displayVolt;
	}
	   
	public static String getVoltDisplayFull(int volts)
	{
		String displayVolt;
		
		if(volts > 1000000)
		{
			displayVolt = roundTwoDecimals((double)volts/1000000)+" Megavolts";
		}
		if(volts > 1000)
		{
			displayVolt = roundTwoDecimals((double)volts/1000)+" Kilovolts";
		}
		else if (volts == 1)
		{
			displayVolt = volts+" volt";
		}
		else
		{
			displayVolt = volts+" Watts";
		}
		
		
		return displayVolt;
	}
	
	/**
	 * Return a string with the amount of watts for displaying.
	 * @param watts
	 * @return The string for displaying watts
	 */
	public static String getWattDisplay(int watts)
	{
		String displayWatt;
		
		if(watts > 1000000)
		{
			displayWatt = roundTwoDecimals((double)watts/1000000)+" MW";
		}
		if(watts > 1000)
		{
			displayWatt = roundTwoDecimals((double)watts/1000)+" KW";
		}
		else
		{
			displayWatt = watts+" W";
		}
		
		return displayWatt;
	}
	   
	public static String getWattDisplayFull(int watts)
	{
		String displayWatt;
		
		if(watts > 1000000)
		{
			displayWatt = roundTwoDecimals((double)watts/1000000)+" Megawatts";
		}
		if(watts > 1000)
		{
			displayWatt = roundTwoDecimals((double)watts/1000)+" Kilowatts";
		}
		else if (watts == 1)
		{
			displayWatt = watts+" Watt";
		}
		else
		{
			displayWatt = watts+" Watts";
		}
		
		
		return displayWatt;
	}
	   
	
    /**
     * Rounds a number to two decimal places
     * @param The number
     * @return The rounded number
     */
    public static float roundTwoDecimals(double d)
    {
    	try
    	{
    		DecimalFormat twoDForm = new DecimalFormat("#.##");
            return Float.valueOf(twoDForm.format(d));
    	}
    	catch (Exception e)
    	{
    		return (float)Math.floor(d);
    	}
    	
    }
    
    /**
     * Finds the side of a block depending on it's facing direction from the given side.
     * The side numbers are compatible with the function"getBlockTextureFromSideAndMetadata".
     *
	 *  Bottom: 0;
	 *  Top: 1;
	 *	Back: 2;
	 *	Front: 3;
	 *	Left: 4;
	 *	Right: 5;
	 * @param front - The direction in which this block is facing/front. Use a number between 0 and 5. Default is 3.
     * @param side - The side you are trying to find. A number between 0 and 5.
     * @return The side relative to the facing direction.
     */
    
    public static byte getOrientationFromSide(byte front, byte side)
    {
    	switch(front)
    	{
	    	case 0:
	    		switch(side)
	        	{
	    	    	case 0: return 3;
	    	    	case 1: return 4;
	    	    	case 2: return 1;
	    	    	case 3: return 0;
	    	    	case 4: return 4;
	    	    	case 5: return 5;
	        	}
	    	case 1:
	    		switch(side)
	        	{
	    	    	case 0: return 4;
	    	    	case 1: return 3;
	    	    	case 2: return 0;
	    	    	case 3: return 1;
	    	    	case 4: return 4;
	    	    	case 5: return 5;
	        	}
	    	case 2:
	    		switch(side)
	        	{
	    	    	case 0: return 0;
	    	    	case 1: return 1;
	    	    	case 2: return 3;
	    	    	case 3: return 2;
	    	    	case 4: return 5;
	    	    	case 5: return 4;
	        	}
    		case 3: return side;
    		case 4:
    			switch(side)
    	    	{
    		    	case 0: return 0;
    		    	case 1: return 1;
    		    	case 2: return 5;
    		    	case 3: return 4;
    		    	case 4: return 3;
    		    	case 5: return 2;
    	    	}
    		case 5:
    			switch(side)
    	    	{
    		    	case 0: return 0;
    		    	case 1: return 1;
    		    	case 2: return 4;
    		    	case 3: return 5;
    		    	case 4: return 2;
    		    	case 5: return 3;
    	    	}
    	}
    	
    	return -1;
    }

	/**
	 * Gets the ID of a block or item from the configuration file
	 * @param name - The name of the block or item
	 * @param defaultID - The default ID of the block or item. Any errors will restore this block/item ID
	 * @param isBlock - Is this object a block or an item?
	 * @return The block or item ID
	 */
	public static int getConfigID(Configuration configuration, String name, int defaultID, boolean isBlock)
	{
		configuration.load();
        int id = defaultID;
        
        if(isBlock)
        {
        	id = Integer.parseInt(configuration.getOrCreateIntProperty(name, Configuration.CATEGORY_BLOCK, defaultID).value);
        	if(id < 100) {return defaultID;}
        }
        else
        {
        	id = Integer.parseInt(configuration.getOrCreateIntProperty(name, Configuration.CATEGORY_ITEM, defaultID).value);
        	if(id < 256) {return defaultID;}
        }
        configuration.save();
        return id;
	}
}
