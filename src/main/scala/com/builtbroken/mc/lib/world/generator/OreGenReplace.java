package com.builtbroken.mc.lib.world.generator;

import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

/**
 * This class is used for storing ore generation data. If you are too lazy to generate your own
 * ores, you can do add to add your ore to the list of ores
 * to generate.
 *
 * @author Calclavia
 */
public class OreGenReplace extends OreGenerator
{
    public final OreGeneratorSettings settings;
    /**
     * Dimensions to ignore ore generation
     */
    public boolean ignoreSurface = false;
    public boolean ignoreNether = true;
    public boolean ignoreEnd = true;

    public int generatorType = 1;

    /**
     * @param name         - The name of the ore dictionary
     * @param block        -block to place
     * @param meta         - meta of block to place
     * @param settings     - controls spawn conditions
     * @param harvestLevel
     * @param harvestTool
     */
    public OreGenReplace(String name, Block block, int meta, OreGeneratorSettings settings, String harvestTool, int harvestLevel)
    {
        super(name, block, meta, harvestTool, harvestLevel);
        this.settings = settings;
    }

    @Override
    public void generate(World world, Random random, int varX, int varZ)
    {
        long time = System.nanoTime();
        int blocksPlaced = 0;
        while (blocksPlaced < settings.amountPerChunk)
        {
            int x = varX + random.nextInt(16);
            int z = varZ + random.nextInt(16);
            int y = random.nextInt(Math.max(settings.maxGenerateLevel - settings.minGenerateLevel, 0)) + settings.minGenerateLevel;
            int placed = this.generateBranch(world, random, varX, varZ, x, y, z);
            if (placed <= 0)
            {
                placed = settings.amountPerBranch; //Prevents inf loop
            }
            blocksPlaced += placed;
        }
        time = System.nanoTime() - time;
        Engine.logger().info("OreGenReplace[" + name + "] >> Chunk(" + varX + ", " + +varZ + ") >> Blocks: " + blocksPlaced + " >> TotalTime: " + StringHelpers.formatNanoTime(time));
    }

    /**
     * Picks a random location in the chunk based on a random rotation and Y value
     *
     * @param world - world
     * @param rand  - random
     * @param varX  - randomX
     * @param varY  - randomY
     * @param varZ  - randomZ
     * @return true if it placed blocks
     */
    public int generateBranch(World world, Random rand, int chunkCornerX, int chunkCornerZ, int varX, int varY, int varZ)
    {
        int blocksPlaced = 0;
        //MC default ore gen code, doesn't work well.... avoid
        if (generatorType == 0)
        {
            float angle = rand.nextFloat() * (float) Math.PI;
            double rxUpper = varX + 8 + MathHelper.sin(angle) * settings.amountPerBranch / 8.0F;
            double rxLower = varX + 8 - MathHelper.sin(angle) * settings.amountPerBranch / 8.0F;
            double rzUpper = varZ + 8 + MathHelper.cos(angle) * settings.amountPerBranch / 8.0F;
            double rzLower = varZ + 8 - MathHelper.cos(angle) * settings.amountPerBranch / 8.0F;
            double randomY = varY + rand.nextInt(3) - 2;
            double randomY2 = varY + rand.nextInt(3) - 2;

            for (int b = 0; b <= settings.amountPerBranch; ++b)
            {
                double var20 = rxUpper + (rxLower - rxUpper) * b / settings.amountPerBranch;
                double var22 = randomY + (randomY2 - randomY) * b / settings.amountPerBranch;
                double var24 = rzUpper + (rzLower - rzUpper) * b / settings.amountPerBranch;

                double var26 = rand.nextDouble() * settings.amountPerBranch / 16.0D;
                double var28 = (MathHelper.sin(b * (float) Math.PI / settings.amountPerBranch) + 1.0F) * var26 + 1.0D;
                double var30 = (MathHelper.sin(b * (float) Math.PI / settings.amountPerBranch) + 1.0F) * var26 + 1.0D;

                int startX = MathHelper.floor_double(var20 - var28 / 2.0D);
                int startY = MathHelper.floor_double(var22 - var30 / 2.0D);
                int startZ = MathHelper.floor_double(var24 - var28 / 2.0D);

                int endX = MathHelper.floor_double(var20 + var28 / 2.0D);
                int endY = MathHelper.floor_double(var22 + var30 / 2.0D);
                int endZ = MathHelper.floor_double(var24 + var28 / 2.0D);

                for (int px = startX; px <= endX; ++px)
                {
                    double deltaX = (px + 0.5D - var20) / (var28 / 2.0D);

                    if (deltaX * deltaX < 1.0D)
                    {
                        for (int py = startY; py <= endY; ++py)
                        {
                            double dy = (py + 0.5D - var22) / (var30 / 2.0D);

                            if (deltaX * deltaX + dy * dy < 1.0D)
                            {
                                for (int pz = startZ; pz <= endZ; ++pz)
                                {
                                    double dz = (pz + 0.5D - var24) / (var28 / 2.0D);

                                    Block block = world.getBlock(px, py, pz);
                                    if (deltaX * deltaX + dy * dy + dz * dz < 1.0D && (settings.replaceBlock == null || block == settings.replaceBlock))
                                    {
                                        if (world.setBlock(px, py, pz, this.oreBlock, this.oreMeta, 2))
                                        {
                                            blocksPlaced++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Path finder ore gen
        else if (generatorType == 1)
        {
            //Positions already pathed
            List<Pos> pathed = new ArrayList();
            //Positions to path next
            Queue<Pos> toPath = new LinkedList();

            //First location to path
            toPath.add(new Pos(varX, varY, varZ));

            List<ForgeDirection> directions = new ArrayList();
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            {
                directions.add(dir);
            }

            //Breadth first search
            while (!toPath.isEmpty() && blocksPlaced < settings.amountPerBranch)
            {
                Pos next = toPath.poll();
                pathed.add(next);

                //Place block
                Block block = next.getBlock(world);
                if (settings.replaceBlock == null || block == settings.replaceBlock)
                {
                    if (next.setBlock(world, oreBlock, oreMeta, 2))
                    {
                        blocksPlaced += 1;
                    }
                }

                //Find new locations to place blocks
                Collections.shuffle(directions);
                for (ForgeDirection direction : directions)
                {
                    //TODO randomize next path
                    Pos pos = next.add(direction);
                    if (!pathed.contains(pos) && world.rand.nextBoolean())
                    {
                        if (pos.isInsideMap())
                        {
                            boolean insideX = pos.xi() >= chunkCornerX && pos.xi() < (chunkCornerX + 16);
                            boolean insideZ = pos.zi() >= chunkCornerZ && pos.zi() < (chunkCornerZ + 16);
                            boolean insideY = pos.yi() >= settings.minGenerateLevel && pos.yi() <= settings.maxGenerateLevel;
                            if (insideX && insideZ && insideY)
                            {
                                block = pos.getBlock(world);
                                if (settings.replaceBlock == null || block == settings.replaceBlock)
                                {
                                    toPath.add(pos);
                                }
                            }
                        }

                        if (!toPath.contains(pos))
                        {
                            pathed.add(pos);
                        }
                    }
                }
            }
        }
        //Pre-cache ore gen
        else if (generatorType == 2)
        {
            int blockToPlace = 1 + com.builtbroken.jlib.helpers.MathHelper.rand.nextInt(Math.max(1, settings.amountPerBranch - 1));
            //TODO find templates that match block count
        }
        return blocksPlaced;
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
