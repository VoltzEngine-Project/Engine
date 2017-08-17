package com.builtbroken.mc.seven.framework.block.listeners;

import com.builtbroken.mc.framework.block.imp.IBlockListener;
import com.builtbroken.mc.api.tile.node.ITileNodeHost;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.framework.block.imp.ITileWithListeners;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2017.
 */
public class ListenerIterator implements Iterator<ITileEventListener>, Iterable<ITileEventListener>
{
    private World world;
    private IBlockAccess access;

    private int x;
    private int y;
    private int z;

    private int index = -1;
    private int nextIndex = -1;
    private int size = -1;

    private BlockBase blockBase;
    private TileEntity tile;
    private String key;

    public ListenerIterator(World world, int x, int y, int z, BlockBase blockBase, String key)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockBase = blockBase;
        this.key = key;
    }

    public ListenerIterator(IBlockAccess world, int x, int y, int z, BlockBase blockBase, String key)
    {
        this.access = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockBase = blockBase;
        this.key = key;
    }

    @Override
    public Iterator<ITileEventListener> iterator()
    {
        return this;
    }

    @Override
    public boolean hasNext()
    {
        if (size == -1)
        {
            size = 2;
            if (blockBase.listeners.containsKey(key))
            {
                size += blockBase.listeners.get(key).size();
            }
            if (getTileEntity() instanceof ITileWithListeners)
            {
                List<ITileEventListener> listenerList = ((ITileWithListeners) tile).getListeners(key);
                if (listenerList != null)
                {
                    size += listenerList.size();
                }
            }
        }
        while (peek() == null && nextIndex < size)
        {
            nextIndex++;
        }
        return peek() != null;
    }

    @Override
    public ITileEventListener next()
    {
        //Clean up last call
        if (get(index) instanceof IBlockListener)
        {
            ((IBlockListener) get(index)).eject();
        }

        //Get current listener
        ITileEventListener re = peek();

        //set next index
        index = nextIndex;
        nextIndex++;
        return re;
    }

    /**
     * Looks at what the next entry in the list will be
     *
     * @return
     */
    protected ITileEventListener peek()
    {
        return get(nextIndex);
    }

    /**
     * Gets the item at the index
     *
     * @return
     */
    protected ITileEventListener get(int index)
    {
        if (index >= 0)
        {
            ITileEventListener re = null;
            if (index == 0)
            {
                TileEntity tile = getTileEntity();
                if (tile instanceof ITileEventListener)
                {
                    re = (ITileEventListener) tile;
                }
            }
            else if (index == 1)
            {
                TileEntity tile = getTileEntity();
                if (tile instanceof ITileNodeHost && ((ITileNodeHost) tile).getTileNode() instanceof ITileEventListener)
                {
                    re = (ITileEventListener) ((ITileNodeHost) tile).getTileNode();
                }
            }
            else
            {
                int i = index - 2;
                if (blockBase.listeners.get(key) != null && i < blockBase.listeners.get(key).size())
                {
                    re = blockBase.listeners.get(key).get(i);
                }
                else
                {
                    TileEntity tile = getTileEntity();
                    if (tile instanceof ITileWithListeners)
                    {
                        List<ITileEventListener> listenerList = ((ITileWithListeners) tile).getListeners(key);
                        if (listenerList != null && i < listenerList.size())
                        {
                            re = listenerList.get(i);
                        }
                    }
                }
            }

            //Inject location
            if (re instanceof IBlockListener)
            {
                if(world != null)
                {
                    ((IBlockListener) re).inject(world, xi(), yi(), zi());
                }
                else
                {
                    ((IBlockListener) re).inject(access, xi(), yi(), zi());
                }
            }

            //Validate listener is usable for location or connected tile
            return re != null && re.isValidForTile() ? re : null;
        }
        return null;
    }

    public TileEntity getTileEntity()
    {
        if (tile == null)
        {
            if (world() != null)
            {
                tile = world().getTileEntity(xi(), yi(), zi());
            }
            else if (access != null)
            {
                tile = access.getTileEntity(xi(), yi(), zi());
            }
        }
        return tile;
    }

    public World world()
    {
        if (access instanceof World)
        {
            return world;
        }
        return world;
    }

    public int xi()
    {
        return x;
    }

    public int yi()
    {
        return y;
    }

    public int zi()
    {
        return z;
    }
}
