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

/** Fake world that creates a single chunk to work out of
 * Created by robert on 11/20/2014.
 */
public class FakeWorld extends World
{
    Data[][][] map = new Data[16][256][16];

    public FakeWorld()
    {
        this(null, "FakeWorld", new FakeWorldProvider() , new WorldSettings(new WorldInfo(new NBTTagCompound())), new Profiler());
    }

    public FakeWorld(ISaveHandler p_i45368_1_, String p_i45368_2_, WorldProvider p_i45368_3_, WorldSettings p_i45368_4_, Profiler p_i45368_5_)
    {
        super(p_i45368_1_, p_i45368_2_, p_i45368_3_, p_i45368_4_, p_i45368_5_);
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        if(inMap(x, y, z) && map[x][y][z] != null)
        {
            return map[x][y][z].block;
        }
        return null;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify)
    {
        if(inMap(x, y, z))
        {
            if(map[x][y][z] == null)
                map[x][y][z] = new Data();


            TileEntity tile = block != null ? block.createTileEntity(this, meta) : null;
            if(block != map[x][y][z].block || tile != map[x][y][z].tile)
            {
                if(map[x][y][z].tile != null)
                    map[x][y][z].tile.invalidate();
                map[x][y][z].tile = null;
            }

            map[x][y][z].block = block;
            map[x][y][z].meta = meta;
            map[x][y][z].tile = tile;
            if(block != null)
            {
                if (tile != null)
                {
                    tile.setWorldObj(this);
                    tile.xCoord = x;
                    tile.yCoord = y;
                    tile.zCoord = z;
                }
                notifyBlockChange(x, y, z, block);
            }
            return true;
        }
        return false;
    }

    public int getBlockMetadata(int x, int y, int z)
    {
        if(inMap(x, y, z) &&  map[x][y][z] != null)
        {
            return map[x][y][z].meta;
        }
        return 0;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int n)
    {
        if(inMap(x, y, z) &&  map[x][y][z] != null)
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
        if(inMap(x, y, z) &&  map[x][y][z] != null)
        {
            return map[x][y][z].tile;
        }
        return null;
    }

    /** Checks if the location is inside the map bounds */
    private boolean inMap(int x, int y, int z)
    {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length && z >= 0 && z < map[0][0].length;
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

    /** Holds basic data for the fake chunk */
    public static class Data
    {
        public Block block = null;
        public TileEntity tile = null;
        public int meta = 0;
    }
}
