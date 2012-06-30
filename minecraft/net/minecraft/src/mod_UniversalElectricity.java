package net.minecraft.src;

import java.util.Random;

import net.minecraft.src.forge.IGuiHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.universalelectricity.GUIBatteryBox;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.api.UERecipes;

/**
 * This class is basically just a loader for Universal Components
 * @author Calclavia
 */

public class mod_UniversalElectricity extends NetworkMod
{
	public static mod_UniversalElectricity instance;
	
	@Override
	public void load()
	{
		this.instance = this;
		UniversalElectricity universalElectricity = new UniversalElectricity();
		MinecraftForge.setGuiHandler(this, universalElectricity);
		universalElectricity.load();
		universalElectricity.MachineRenderType = ModLoader.getUniqueBlockModelID(this, true);
		universalElectricity.registerAddon(this, this.getVersion());
	}
	
	/**
     * Finish loading your mod
     */
	@Override
    public void modsLoaded()
    {
		UERecipes.initialize();
    }
	
	@Override
	public String getVersion()
	{
		return UniversalElectricity.getVersion();
	}
	
	@Override
	public void generateSurface(World world, Random rand, int chunkX, int chunkZ)
    {
		UniversalElectricity.generateSurface(world, rand, chunkX, chunkZ);
    }
	
	@Override
	public void renderInvBlock(RenderBlocks renderBlocks, Block block, int metadata, int renderType)
	{
		UniversalElectricity.renderInvBlock(renderBlocks, block, metadata, renderType);
	}
}