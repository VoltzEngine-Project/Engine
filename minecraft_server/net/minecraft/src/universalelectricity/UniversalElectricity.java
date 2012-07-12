package net.minecraft.src.universalelectricity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.universalelectricity.electricity.IElectricUnit;
import net.minecraft.src.universalelectricity.extend.TileEntityConductor;

public class UniversalElectricity
{
	public static final Configuration configuration = new Configuration(new File("config/UniversalElectricity/UniversalElectricity.cfg"));
	
	public static final List<NetworkMod> addons = new ArrayList<NetworkMod>();
	
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
		
		addons.add(networkmod);
		System.out.println("Loaded Universal Add-On: "+networkmod.getName());
	}

	public static String getVersion()
	{
		return "0.4.3";
	}
	
	/*------------------ FUNCTIONS AND HOOKS ----------------------
		Some formulas to note:
	*   Wattage = Voltage x Amps (W = V X I)
	*   Voltage = Amperage x Resistance (V = I x R)
	*/
	/**
	 * Returns the amount of amps.
	 * @param watts
 	 * @param volts
	 * @return The amount of amps
	 */
	public static double getAmps(double watts, int volts)
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
		
		if(amps < 1.0)
		{
			displayAmps = (int)(amps*1000)+" mA";
		}else if(amps > 1000)
		{
			displayAmps = roundOneDecimal(amps/1000)+" KA";
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
		
		if(amps < 1.0)
		{
			displayAmps = (int)(amps*1000)+" Milliamps";
		}else if(amps > 1000)
		{
			displayAmps = roundOneDecimal(amps/1000)+" Kiloamps";
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
			displayVolt = roundOneDecimal(volts/1000000)+" MV";
		}
		if(volts > 1000)
		{
			displayVolt = roundOneDecimal(volts/1000)+" KV";
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
			displayVolt = roundOneDecimal(volts/1000000)+" Megavolts";
		}
		if(volts > 1000)
		{
			displayVolt = roundOneDecimal(volts/1000)+" Kilovolts";
		}
		else if (volts == 1)
		{
			displayVolt = volts+" volt";
		}
		else
		{
			displayVolt = volts+" volts";
		}
		
		return displayVolt;
	}
	
	/**
	 * Return a string with the amount of watts for displaying.
	 * @param watts
	 * @return The string for displaying watts
	 */
	public static String getWattDisplay(double watts)
	{
		String displayWatt;
		
		if(watts > 1000000)
		{
			displayWatt = roundOneDecimal(watts/1000000)+" MW";
		}
		if(watts > 1000)
		{
			displayWatt = roundOneDecimal(watts/1000)+" KW";
		}
		else
		{
			displayWatt = (int)watts+" W";
		}
		
		return displayWatt;
	}
	   
	public static String getWattDisplayFull(double watts)
	{
		String displayWatt;
		
		if(watts > 1000000)
		{
			displayWatt = roundOneDecimal(watts/1000000)+" Megawatts";
		}
		if(watts > 1000)
		{
			displayWatt = roundOneDecimal(watts/1000)+" Kilowatts";
		}
		else if (watts == 1)
		{
			displayWatt = (int)watts+" Watt";
		}
		else
		{
			displayWatt = (int)watts+" Watts";
		}
		
		
		return displayWatt;
	}
	   
	
    /**
     * Rounds a number to two decimal places
     * @param The number
     * @return The rounded number
     */
    public static double roundTwoDecimals(double d)
    {
    	int j = (int) (d * 100);
    	return j / 100.0;
    }
    
    /**
     * Rounds a number to one decimal place
     * @param The number
     * @return The rounded number
     */
    public static double roundOneDecimal(double d)
    {
    	int j = (int) (d * 10);
    	return j / 10.0;
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
	 * Checks if the block is being connected to a conductor
	 * @param world - The world in which this conductor block is in
	 * @param x - The X axis of the conductor
	 * @param y - The Y axis of the conductor
	 * @param z - The Z axis of the conductor
	 * @return Returns the tile entity for the block on the designated side. Returns null if not a UE Unit
	 */
	public static TileEntity getUEUnitFromSide(World world, Vector3 position, byte side)
	{
		position = getPositionFromSide(position, side);
		
		//Check if the designated block is a UE Unit - producer, consumer or a conductor
		TileEntity tileEntity = world.getBlockTileEntity(position.intX(), position.intY(), position.intZ());
		
		if(tileEntity instanceof TileEntityConductor)
		{
			return tileEntity;
		}
		
		if(tileEntity instanceof IElectricUnit)
		{
			if(((IElectricUnit)tileEntity).canConnect(UniversalElectricity.getOrientationFromSide(side, (byte)2)))
			{
				return tileEntity;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets a position relative to another position's side
	 * @param position - The position
	 * @param side - The side. 0-5
	 * @return The position relative to the original position's side
	 */
	public static Vector3 getPositionFromSide(Vector3 position, byte side)
	{
		switch(side)
		{
			case 0: position.y -= 1; break;
			case 1: position.y += 1; break;
			case 2: position.z += 1; break;
			case 3: position.z -= 1; break;
			case 4: position.x += 1; break;
			case 5: position.x -= 1; break;
		}
		
		return position;
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
