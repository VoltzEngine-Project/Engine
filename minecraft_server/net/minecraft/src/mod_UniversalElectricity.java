package net.minecraft.src;

import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.universalelectricity.ItemUniversalOre0;
import net.minecraft.src.universalelectricity.ItemUniversalOre1;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.electricity.ElectricityManager;
import net.minecraft.src.universalelectricity.ore.UEOreManager;
import net.minecraft.src.universalelectricity.recipe.UERecipeManager;

/**
 * This class is basically just a loader for Universal Components
 * @author Calclavia
 */

public class mod_UniversalElectricity extends NetworkMod
{	
	@Override
	public void load()
	{	
		ModLoader.setInGameHook(this, true, true);
		ModLoader.registerBlock(UEOreManager.blockOre[0], ItemUniversalOre0.class);
		ModLoader.registerBlock(UEOreManager.blockOre[1], ItemUniversalOre1.class);
	}
	
	@Override
    public void modsLoaded()
    {
		UERecipeManager.initialize();
    }
	
	@Override
	public String getVersion()
	{
		return UniversalElectricity.getVersion();
	}
	
	/**
     * Ticked every game tick if you have subscribed to tick events through {@link ModLoader#setInGameHook(BaseMod, boolean, boolean)}
     * 
     * @param time the rendering subtick time (0.0-1.0)
     * @param minecraftInstance the client
     * @return true to continue receiving ticks
     */
	@Override
	public boolean onTickInGame(MinecraftServer minecraftServer)
    {
		ElectricityManager.onUpdate();
        return true;
    }
	
	@Override
	public void generateSurface(World world, Random rand, int chunkX, int chunkZ)
    {
		UEOreManager.generateSurface(world, rand, chunkX, chunkZ);
    }
	
	@Override
    public boolean clientSideRequired()
    {
        return true;
    }

    @Override
    public boolean serverSideRequired()
    {
        return false;
    }
    
    @Override
	public String getName()
    {
        return "Universal Electricity";
    }
}