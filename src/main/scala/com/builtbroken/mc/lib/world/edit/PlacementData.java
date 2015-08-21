package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.api.IWorldPosition;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/** Designed to be used as static data for a registry that will need
 * to change one block into another using data. This class is used
 * for that data but is not limited mainly to that task. It can be
 * modified to be used for anything.
 *
 * Created by robert on 2/25/2015.
 */
public class PlacementData extends Pair<Block, Integer>
{
    public PlacementData(Block block, int meta)
    {
        super(block, meta);
    }

    public Block block()
    {
        return left();
    }

    public int meta()
    {
        return right();
    }

    /**
     * Used to place a block in the location using the data provided in this class
     * @param location - location to place the block
     * @param simulate - if true the block will not be placed but conditions to place
     *                 the block will be checked. This way errors can be detected before
     *                 attempting to trigger block updates.
     * @return result of the placement
     */
    public BlockEditResult place(final IWorldPosition location, boolean simulate)
    {
        return place(location.world(), (int)location.x(), (int)location.y(), (int)location.z(), simulate);
    }

    /**
     * Used to place a block in the location using the data provided in this class
     * @param world - dim to place the block in
     * @param pos - location to place the block
     * @param simulate - if true the block will not be placed but conditions to place
     *                 the block will be checked. This way errors can be detected before
     *                 attempting to trigger block updates.
     * @return result of the placement
     */
    public BlockEditResult place(final World world, final IPos3D pos, boolean simulate)
    {
        return place(world, (int)pos.x(), (int)pos.y(), (int)pos.z(), simulate);
    }

    /**
     * Used to place a block in the location using the data provided in this class
     * @param world  - location to place the block
     * @param x  - location to place the block
     * @param y  - location to place the block
     * @param z - location to place the block
     * @param simulate - if true the block will not be placed but conditions to place
     *                 the block will be checked. This way errors can be detected before
     *                 attempting to trigger block updates.
     * @return result of the placement
     */
    public BlockEditResult place(final World world, int x, int y, int z, boolean simulate)
    {
        if(world != null)
        {
            if(world.checkChunksExist(x, y, z, x, y, z))
            {
                final Chunk chunk = world.getChunkFromBlockCoords(x, z);
                if(chunk != null && chunk.isChunkLoaded)
                {
                    final Block prev_block = world.getBlock(x, y, z);
                    int prev_meta = world.getBlockMetadata(x, y, z);
                    if(prev_block != block() && prev_meta != meta())
                    {
                        if(!simulate)
                        {
                            if(world.setBlock(x, y, z, block(), meta(), 2))
                            {
                                return BlockEditResult.PLACED;
                            }
                            else
                            {
                                return BlockEditResult.BLOCKED;
                            }
                        }
                        return BlockEditResult.PLACED;
                    }
                    return BlockEditResult.ALREADY_PLACED;
                }
            }
            return BlockEditResult.CHUNK_UNLOADED;
        }
        return BlockEditResult.NULL_WORLD;
    }
}
