package com.builtbroken.mc.lib.world.generator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * This class is used for storing ore generation data. If you are too lazy to generate your own
 * ores, you can do to add your ore to the list of ores to generate.
 *
 * @author Calclavia
 */
public abstract class OreGenerator implements IWorldGenerator
{
	/**
	 * The ore dictionary name of the ore.
	 */
	public String name;

	public IBlockState oreBlock;

	/**
	 * What harvest level does this machine need to be acquired?
	 */
	public int harvestLevel;

	/**
	 * The predefined tool classes are "pickaxe", "shovel", "axe". You can add others for custom
	 * tools.
	 */
	public String harvestTool;

	public OreGenerator(String name, IBlockState state, String harvestTool, int harvestLevel)
	{
		this.name = name;
		this.harvestTool = harvestTool;
		this.harvestLevel = harvestLevel;
		this.oreBlock = state;
		state.getBlock().setHarvestLevel(this.harvestTool, this.harvestLevel, state);
	}

	public abstract void generate(World world, Random random, int varX, int varZ);

	public abstract boolean isOreGeneratedInWorld(World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider);

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		chunkX = chunkX << 4;
		chunkZ = chunkZ << 4;

		// Checks to make sure this is the normal world

		if (isOreGeneratedInWorld(world, chunkGenerator, chunkProvider))
		{
			generate(world, random, chunkX, chunkZ);
		}
	}
}
