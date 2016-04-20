package com.builtbroken.mc.framework.tile;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.tile.IPlayerUsing;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.prefab.tile.entity.TileEntityBase;
import com.builtbroken.mc.prefab.tile.interfaces.tile.ITile;
import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Base class for VE's tile system that combines the Block and Tile class to make implementing
 * new blocks faster. It does have some memory overhead due to the number of fields each class
 * stores. Though the amount of memory used is very little. If it becomes an issue it is recommended to just use
 * the classic Block & TileEntity system from Minecraft. As this will have less memory overhead but will take
 * more work to implement the same functionality.
 * <p/>
 * In order for this class to work it needs to be registered threw the ModManager or something similar. In
 * which a new BlockTile will be created with a static version of this class in it.
 * <p/>
 * Just as a note the system is designed in a special way in order to function. This class will act as a
 * redirect for your Block & Tile. If a method or Field has BLOCK in the java doc it is treated like
 * a static value. If it has the world TILE in the java doc the it is primary directed at the tile instance
 * at the block location. If it can't find the tile it will redirect to the static tile that is your Block.
 * <p/>
 * <p/>
 * Created by Robert(DarkGuardsman) on 1/4/2015.
 */
public abstract class TileAbstract extends TileEntityBase implements IWorldPosition, IPlayerUsing, IRegistryInit, ITile
{
    /** TILE, Current tick count, starts when the tile is placed */
    public long ticks = 0L;

    /**
     * Tile Data used to describe how the block
     * functions that wrappers the tile.
     */
    protected final BlockProperties tileData;

    /**
     * IF true this is a block wrapper, and not a tile. Make sure to check this
     * before doing tile only, or block only tasks.
     */
    protected final boolean isBlock;

    /**
     * Used to create a static version of the tile for
     * wrapping block calls to this class file.
     *
     * @param data
     */
    public TileAbstract(BlockProperties data)
    {
        this.tileData = data;
        this.isBlock = true;
    }

    /** TILE, use this to initialize a tile without setting block data */
    public TileAbstract()
    {
        this.tileData = BlockProperties.getDataFor(getClass());
        this.isBlock = false;
    }

    /**
     * TILE, Called by the world to update the tile. Never
     * call this from your owner code. Use Update() method
     * as this is set final to ensure base functionality.
     */
    @Override
    public final void updateEntity()
    {
        if (ticks == 0)
        {
            firstTick();
        }
        else
        {
            update();
        }

        //Increase tick
        if (ticks >= Long.MAX_VALUE)
        {
            ticks = 1;
        }
        ticks += 1;
    }

    /**
     * Called to get the next cleanup tick call.
     * <p/>
     * Note: Should be random to avoid several tiles from
     * spiking the CPU when updating at the same time.
     *
     * @return number of ticks, must be greater than zero
     */
    protected int getNextCleanupTick()
    {
        return 100 + (int) (world().rand.nextFloat() * 2000);
    }


    /** BLOCK, called from the world when the block is updated */
    public void blockUpdate()
    {
        if (isBlock)
        {
            update();
        }
        else
        {
            throw new RuntimeException("This method can not be called on a tile object. " + this);
        }
    }

    /**
     * TILE, Called first update() call of the tile. Use
     * this to init any values that are needed right
     * after the tile has been fully placed into the
     * world.
     */
    public void firstTick()
    {
    }

    /**
     * TILE,
     * Called each tick as long as the the tile can update.
     */
    public void update()
    {

    }


    /**
     * Block object that goes to this tile
     */
    @Override
    public Block getBlockType()
    {
        return getTileData().block;
    }

    //==============================
    //==== Location ================
    //==============================
    @Override
    public double z()
    {
        return zCoord;
    }

    public int zi()
    {
        return zCoord;
    }

    @Override
    public double x()
    {
        return xCoord;
    }

    public int xi()
    {
        return xCoord;
    }

    @Override
    public double y()
    {
        return yCoord;
    }

    public int yi()
    {
        return yCoord;
    }

    @Override
    public World world()
    {
        return getWorldObj();
    }


    public BlockProperties getTileData()
    {
        return tileData;
    }
}
