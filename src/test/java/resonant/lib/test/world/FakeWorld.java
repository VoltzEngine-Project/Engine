package resonant.lib.test.world;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import resonant.lib.utility.ReflectionUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Fake world that creates a single chunk to work out of
 * Created by robert on 11/20/2014.
 */
public class FakeWorld extends World
{
    public static boolean blocksInit = false;
    public List<TileEntity> tiles = new ArrayList<TileEntity>();

    Data[][][] map = new Data[16][256][16];

    public FakeWorld()
    {
        this(null, "FakeWorld", new FakeWorldProvider(), new WorldSettings(new WorldInfo(new NBTTagCompound())), new Profiler());
        if (!blocksInit)
        {
            //Sets registry to avoid trigger creating of ModClassLoader
            try
            {
                ReflectionUtility.setMCField(Block.class, null, "blockRegistry", new FakeRegistryNamespaced());
                Block.registerBlocks();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }
        this.updateEntities();
    }

    public FakeWorld(ISaveHandler p_i45368_1_, String p_i45368_2_, WorldProvider p_i45368_3_, WorldSettings p_i45368_4_, Profiler p_i45368_5_)
    {
        super(p_i45368_1_, p_i45368_2_, p_i45368_3_, p_i45368_4_, p_i45368_5_);
    }

    @Override
    public void updateEntities()
    {
        Iterator<TileEntity> tile_iterator = tiles.iterator();
        while (tile_iterator.hasNext())
        {
            TileEntity tile = tile_iterator.next();
            if (tile.isInvalid())
            {
                tile_iterator.remove();
            }
            else
            {
                tile.updateEntity();
            }
        }
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        if (inMap(x, y, z) && map[x][y][z] != null)
        {
            return map[x][y][z].block;
        }
        return null;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify)
    {
        boolean added_flag = false;
        boolean change_flag = false;
        if (block == null)
        {
            throw new NullPointerException("World.setBlock() can not set a location to null, use Blocks.Air in value of null");
        }

        if (inMap(x, y, z))
        {
            Block pre_block = getBlock(x, y, z);
            if (block != pre_block || getBlockMetadata(x, y, z) != meta)
            {
                map[x][y][z].block = block;
                map[x][y][z].meta = meta;
                change_flag = true;
                if (block != pre_block)
                {
                    added_flag = true;
                }

                //Check if tile changed
                TileEntity newTile = block != null ? block.createTileEntity(this, meta) : null;
                if (newTile != map[x][y][z].tile)
                {
                    if (map[x][y][z].tile != null)
                        map[x][y][z].tile.invalidate();
                    map[x][y][z].tile = newTile;
                    if (newTile != null)
                    {
                        newTile.setWorldObj(this);
                        newTile.xCoord = x;
                        newTile.yCoord = y;
                        newTile.zCoord = z;
                        newTile.validate();
                        if (newTile.canUpdate())
                            tiles.add(newTile);
                    }
                }
                if (added_flag)
                    block.onBlockAdded(this, x, y, z);
                if (change_flag)
                    notifyBlockChange(x, y, z, block);
            }

            return true;
        }
        else
        {
            throw new RuntimeException("Something Attempted to place a block out side of the test area");
        }
    }

    public int getBlockMetadata(int x, int y, int z)
    {
        if (inMap(x, y, z) && map[x][y][z] != null)
        {
            return map[x][y][z].meta;
        }
        return 0;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int n)
    {
        if (inMap(x, y, z) && map[x][y][z] != null)
        {
            map[x][y][z].meta = meta;
            notifyBlockChange(x, y, z, map[x][y][z].block);
            return true;
        }
        return false;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z)
    {
        if (inMap(x, y, z) && map[x][y][z] != null)
        {
            return map[x][y][z].tile;
        }
        return null;
    }

    @Override
    public void notifyBlockOfNeighborChange(int x, int y, int z, final Block block)
    {
        Block b = this.getBlock(x, y, z);
        if (b != null)
        {
            b.onNeighborBlockChange(this, x, y, z, block);
        }
    }

    /**
     * Checks if the location is inside the map bounds
     */
    private boolean inMap(int x, int y, int z)
    {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length && z >= 0 && z < map[0][0].length;
    }

    public void clear()
    {
        for (int x = 0; x < map.length; x++)
        {
            for (int y = 0; x < map[0].length; x++)
            {
                for (int z = 0; x < map[0][0].length; x++)
                {
                    Data data = map[x][y][x];
                    if (data != null)
                    {
                        if (data.tile != null)
                        {
                            data.tile.invalidate();
                            data.tile.setWorldObj(null);
                            data.tile = null;
                        }
                        data.block = null;
                    }
                }
            }
        }
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        return null;
    }

    @Override
    protected int func_152379_p()
    {
        return 0;
    }

    @Override
    public Entity getEntityByID(int p_73045_1_)
    {
        return null;
    }

    /**
     * Holds basic data for the fake chunk
     */
    public static class Data
    {
        public Block block = null;
        public TileEntity tile = null;
        public int meta = 0;
    }
}
