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

    /* (non-Javadoc)
     * @see net.minecraft.server.BaseMod#load()
     */
    public void load()
    {
        ModLoader.setInGameHook(this, true, true);
        ModLoader.registerBlock(UEOreManager.BlockOre[0], universalelectricity.ItemUniversalOre0.class);
        ModLoader.registerBlock(UEOreManager.BlockOre[1], universalelectricity.ItemUniversalOre1.class);
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.BaseMod#modsLoaded()
     */
    public void modsLoaded()
    {
        UERecipeManager.initialize();
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.BaseMod#getVersion()
     */
    public String getVersion()
    {
        return UniversalElectricity.getVersion();
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.BaseMod#onTickInGame(net.minecraft.server.MinecraftServer)
     */
    public boolean onTickInGame(MinecraftServer minecraftserver)
    {
        ElectricityManager.onUpdate();
        return true;
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.BaseMod#generateSurface(net.minecraft.server.World, java.util.Random, int, int)
     */
    public void generateSurface(World world, Random random, int i, int j)
    {
        UEOreManager.generateSurface(world, random, i, j);
    }
}
