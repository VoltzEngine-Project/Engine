package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.sorting.Vector3DistanceComparator;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Cube selection in the world
 *
 * @author Darkguardsman
 */
public class Selection extends Cube
{
    protected World world;

    public Selection(World world, Cube cube)
    {
        super(cube);
        this.world = world;
    }

    public Selection(World world, IPos3D min, IPos3D max)
    {
        super(min, max);
        this.world = world;
    }

    public Selection(NBTTagCompound nbt)
    {
        super(nbt);
    }

    public Selection(ByteBuf buf)
    {
        super(buf);
    }

    ////////////////////////////////////////////
    ///             Utility                  ///
    ////////////////////////////////////////////

    /**
     * Replaces all blocks in the cube with a different type
     *
     * @param block - block to place
     * @param meta  - meta to place as
     * @return list of location that were changed, used for Undo commands
     */
    public List<Pos> replaceBlocks(Block block, int meta)
    {
        List<Pos> list = getBlockLocations(block, meta);
        for (Pos Pos : list)
        {
            Pos.setBlock(world, block, meta);
        }
        return list;
    }

    /**
     * Grabs all blocks in the cube
     */
    public List<Pos> getBlockLocations()
    {
        return getBlockLocations(null, -1, -1);
    }

    /**
     * Grabs all blocks in the cube that match
     *
     * @param block - block instance to match against
     * @return list of blocks, never null but can be empty
     */
    public List<Pos> getBlockLocations(Block block)
    {
        return getBlockLocations(block, -1, -1);
    }

    /**
     * Grabs all blocks in the cube that match
     *
     * @param block - block instance to match against
     * @param meta  - meta value to match
     * @return list of blocks, never null but can be empty
     */
    public List<Pos> getBlockLocations(Block block, int meta)
    {
        return getBlockLocations(block, meta, -1);
    }

    /**
     * Grabs all blocks in the cube that match
     *
     * @param block - block instance to match against, if null will match all
     * @param meta  - meta value to match, if -1 will match all meta
     * @param size  - limiter for the list in case only a few blocks are wanted.
     *              If zero or less will not limit size
     * @return list of blocks, never null but can be empty
     */
    public List<Pos> getBlockLocations(Block block, int meta, int size)
    {
        List<Pos> list = new LinkedList<Pos>();
        for (int y = min().yi(); y <= max().yi(); y++)
        {
            for (int x = min().xi(); x <= max().xi(); x++)
            {
                for (int z = min().zi(); z <= max().zi(); z++)
                {
                    if (size > 0 && list.size() > size)
                        return list;

                    Pos Pos = new Pos(x, y, z);
                    Block b = Pos.getBlock(world);
                    int m = Pos.getBlockMetadata(world);
                    if (block == null || b == block && (meta == -1 || m == meta))
                    {
                        list.add(Pos);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Grabs all blocks near the point and within the distance.
     * <p/>
     * Note this search pattern does start at most negative corner
     * TODO replace search pattern with same code the blasts use
     * to select blocks in a bubble
     *
     * @param location - center point of the search
     * @param size     - number of items to return
     * @param distance - distance to search
     * @return list of locations of non air blocks sorted to closest to location
     */
    public List<Pos> getLocationsWithin(Location location, int size, int distance)
    {
        List<Pos> list = new LinkedList<Pos>();
        if (distance > 0)
        {
            int min_y = (int) Math.max(min().yi(), location.y() - distance);
            int max_y = (int) Math.min(max().yi(), location.y() + distance);

            int min_x = (int) Math.max(min().xi(), location.x() - distance);
            int max_x = (int) Math.min(max().xi(), location.x() + distance);

            int min_z = (int) Math.max(min().zi(), location.z() - distance);
            int max_z = (int) Math.min(max().zi(), location.z() + distance);

            for (int y = min_y; y <= max_y; y++)
            {
                for (int x = min_x; x <= max_x; x++)
                {
                    for (int z = min_z; z <= max_z; z++)
                    {
                        if (size > 0 && list.size() >= size)
                        {
                            Collections.sort(list, new Vector3DistanceComparator(location));
                            return list;
                        }

                        Pos pos = new Pos(x, y, z);
                        if (location.distance(pos) <= distance)
                        {
                            Block b = pos.getBlock(location.world());
                            if (b != null && !pos.isAirBlock(location.world()) && pos.getHardness(location.world()) >= 0)
                            {
                                list.add(pos);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
