package net.minecraft.src;

import java.util.Random;

import net.minecraft.src.forge.IGuiHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.components.UniversalComponents;

/**
 * This class is basically just a loader for Universal Components
 * @author Calclavia
 */

public class mod_UniversalElectricity extends NetworkMod implements IGuiHandler
{
	@Override
	public void load()
	{
		MinecraftForge.setGuiHandler(this, this);
		UniversalElectricity.load();
		UniversalComponents.load();
		UniversalElectricity.registerAddon(this, this.getVersion());
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
	public Object getGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		System.out.println("WORKING "+ID);
		return null;
	}
}