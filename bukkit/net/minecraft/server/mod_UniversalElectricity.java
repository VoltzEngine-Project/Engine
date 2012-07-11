package net.minecraft.server;

import java.util.Random;

import universalelectricity.UniversalElectricity;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.ore.UEOreManager;
import universalelectricity.recipe.UERecipeManager;
import forge.NetworkMod;


/**
 * The Class mod_UniversalElectricity.
 */
public class mod_UniversalElectricity extends NetworkMod
{
    
    /**
     * Instantiates a new mod_ universal electricity.
     */
    public mod_UniversalElectricity()
    {
    }

    /**
     * Load.
     */
    public void load()
    {
        ModLoader.setInGameHook(this, true, true);
        ModLoader.registerBlock(UEOreManager.BlockOre[0], universalelectricity.ItemUniversalOre0.class);
        ModLoader.registerBlock(UEOreManager.BlockOre[1], universalelectricity.ItemUniversalOre1.class);
    }

    /**
     * Mods loaded.
     */
    public void modsLoaded()
    {
        UERecipeManager.initialize();
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion()
    {
        return UniversalElectricity.getVersion();
    }

    /**
     * On tick in game.
     *
     * @param minecraftserver the minecraftserver
     * @return true, if successful
     */
    public boolean onTickInGame(MinecraftServer minecraftserver)
    {
        ElectricityManager.onUpdate();
        return true;
    }

    /**
     * Generate surface.
     *
     * @param world the world
     * @param random the random
     * @param i the i
     * @param j the j
     */
    public void generateSurface(World world, Random random, int i, int j)
    {
        UEOreManager.generateSurface(world, random, i, j);
    }
 	
 	/**
	  * Client side required.
	  *
	  * @return true, if successful
	  */
	 @Override
    public boolean clientSideRequired()
    {
            return true;
    }

    /**
     * Server side required.
     *
     * @return true, if successful
     */
    @Override
    public boolean serverSideRequired()
    {
            return false;
    }
}
