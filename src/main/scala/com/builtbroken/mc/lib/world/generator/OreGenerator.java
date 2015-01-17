package com.builtbroken.mc.lib.world.generator;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.oredict.OreDictionary;

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

	public Block oreBlock;

	public int oreMeta;

	/**
	 * What harvest level does this machine need to be acquired?
	 */
	public int harvestLevel;

	/**
	 * The predefined tool classes are "pickaxe", "shovel", "axe". You can add others for custom
	 * tools.
	 */
	public String harvestTool;

	/**
	 * @param name - The name of the ore for display
	 */
	public OreGenerator(String name, ItemStack stack, String harvestTool, int harvestLevel)
	{
		this(name, ((ItemBlock) stack.getItem()).field_150939_a, stack.getItemDamage(), harvestTool, harvestLevel);
	}

	public OreGenerator(String name, Block block, int meta, String harvestTool, int harvestLevel)
	{
		this.name = name;
		this.harvestTool = harvestTool;
		this.harvestLevel = harvestLevel;
		this.oreBlock = block;
		this.oreMeta = meta;
		OreDictionary.registerOre(name, new ItemStack(block, 1, meta));
		block.setHarvestLevel(this.harvestTool, this.harvestLevel, meta);
	}

	public abstract void generate(World world, Random random, int varX, int varZ);

	public abstract boolean isOreGeneratedInWorld(World world, IChunkProvider chunkGenerator);

	@Override
	public final void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		chunkX = chunkX << 4;
		chunkZ = chunkZ << 4;

		// Checks to make sure this is the normal world

		if (isOreGeneratedInWorld(world, chunkGenerator))
		{
			generate(world, rand, chunkX, chunkZ);
		}
	}
}
