package com.builtbroken.mc.lib.world.generator;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;

import java.util.Random;

/**
 * This class is used for storing ore generation data. If you are too lazy to generate your own
 * ores, you can do add to add your ore to the list of ores
 * to generate.
 *
 * @author Calclavia
 */
public class OreGenReplace extends OreGenerator
{

	public int minGenerateLevel;
	public int maxGenerateLevel;
	public int amountPerChunk;
	public int amountPerBranch;
	public Block replaceBlock;

	/**
	 * Dimensions to ignore ore generation
	 */
	public boolean ignoreSurface = false;
	public boolean ignoreNether = true;
	public boolean ignoreEnd = true;

	/**
	 * @param name             - The name of the ore dictionary
	 * @param minGenerateLevel - The highest generation level of your ore
	 * @param maxGenerateLevel - The lowest generation level of your ore
	 * @param amountPerChunk   - The amount of ores to generate per chunk
	 * @param amountPerBranch  - The amount of ores to generate in a clutter. E.g coal generates with
	 *                         a lot of other coal next to it. How much do you want?
	 */
	public OreGenReplace(String name, ItemStack stack, Block replaceBlock, int minGenerateLevel, int maxGenerateLevel, int amountPerChunk, int amountPerBranch, String harvestTool, int harvestLevel)
	{
		super(name, stack, harvestTool, harvestLevel);
		this.minGenerateLevel = minGenerateLevel;
		this.maxGenerateLevel = maxGenerateLevel;
		this.amountPerChunk = amountPerChunk;
		this.amountPerBranch = amountPerBranch;
		this.replaceBlock = replaceBlock;
	}

	@Override
	public void generate(World world, Random random, int varX, int varZ)
	{
        for (int i = 0; i < this.amountPerChunk; i++)
        {
            int x = varX + random.nextInt(16);
            int z = varZ + random.nextInt(16);
            int y = random.nextInt(Math.max(this.maxGenerateLevel - this.minGenerateLevel, 0)) + this.minGenerateLevel;
            this.generateReplace(world, random, x, y, z);
        }
	}

    /**
     * Picks a random location in the chunk based on a random rotation and Y value
     * @param world - world
     * @param rand - random
     * @param varX - randomX
     * @param varY - randomY
     * @param varZ - randomZ
     * @return true if it placed blocks
     */
	public boolean generateReplace(World world, Random rand, int varX, int varY, int varZ)
	{
        /*TODO recode this as this is causing extra gen time during chunk generation
            possibly change it to use a simple pathfinding logic to place blocks next to center block
            In which we use ForgeDirection in combination with a range limiter of 2
             AS well make sure that only the first iteration uses UP & DOWN*/
		float angle = rand.nextFloat() * (float) Math.PI;
		double rxUpper = varX + 8 + MathHelper.sin(angle) * this.amountPerBranch / 8.0F;
		double rxLower = varX + 8 - MathHelper.sin(angle) * this.amountPerBranch / 8.0F;
		double rzUpper = varZ + 8 + MathHelper.cos(angle) * this.amountPerBranch / 8.0F;
		double rzLower = varZ + 8 - MathHelper.cos(angle) * this.amountPerBranch / 8.0F;
		double randomY = varY + rand.nextInt(3) - 2;
		double randomY2 = varY + rand.nextInt(3) - 2;

		for (int i = 0; i <= this.amountPerBranch; ++i)
		{
			double var20 = rxUpper + (rxLower - rxUpper) * i / this.amountPerBranch;
			double var22 = randomY + (randomY2 - randomY) * i / this.amountPerBranch;
			double var24 = rzUpper + (rzLower - rzUpper) * i / this.amountPerBranch;
			double var26 = rand.nextDouble() * this.amountPerBranch / 16.0D;
			double var28 = (MathHelper.sin(i * (float) Math.PI / this.amountPerBranch) + 1.0F) * var26 + 1.0D;
			double var30 = (MathHelper.sin(i * (float) Math.PI / this.amountPerBranch) + 1.0F) * var26 + 1.0D;

            int startX = MathHelper.floor_double(var20 - var28 / 2.0D);
			int startY = MathHelper.floor_double(var22 - var30 / 2.0D);
			int startZ = MathHelper.floor_double(var24 - var28 / 2.0D);
			int endX = MathHelper.floor_double(var20 + var28 / 2.0D);
			int endY = MathHelper.floor_double(var22 + var30 / 2.0D);
			int endZ = MathHelper.floor_double(var24 + var28 / 2.0D);

			for (int px = startX; px <= endX; ++px)
			{
				double dx = (px + 0.5D - var20) / (var28 / 2.0D);

				if (dx * dx < 1.0D)
				{
					for (int py = startY; py <= endY; ++py)
					{
						double dy = (py + 0.5D - var22) / (var30 / 2.0D);

						if (dx * dx + dy * dy < 1.0D)
						{
							for (int pz = startZ; pz <= endZ; ++pz)
							{
								double dz = (pz + 0.5D - var24) / (var28 / 2.0D);

								Block block = world.getBlock(px, py, pz);
								if (dx * dx + dy * dy + dz * dz < 1.0D && (this.replaceBlock == Blocks.air || block == this.replaceBlock))
								{
									world.setBlock(px, py, pz, this.oreBlock, this.oreMeta, 2);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public boolean isOreGeneratedInWorld(World world, IChunkProvider chunkGenerator)
	{
		if (this.ignoreSurface && chunkGenerator instanceof ChunkProviderGenerate)
		{
			return false;
		}
		if (this.ignoreNether && chunkGenerator instanceof ChunkProviderHell)
		{
			return false;
		}
		return !(this.ignoreEnd && chunkGenerator instanceof ChunkProviderEnd);
	}
}
