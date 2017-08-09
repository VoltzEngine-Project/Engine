package com.builtbroken.mc.framework.item;

import com.builtbroken.mc.api.items.listeners.IItemEventListener;

import java.util.Iterator;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2017.
 */
public class ItemListenerIterator implements Iterator<IItemEventListener>, Iterable<IItemEventListener>
{
    private int index = -1;
    private int nextIndex = -1;
    private int size = -1;

    private String key;
    private ItemBase item;

    public ItemListenerIterator(ItemBase item, String key)
    {
        this.item = item;
        this.key = key;
    }

    @Override
    public Iterator<IItemEventListener> iterator()
    {
        return this;
    }

    @Override
    public boolean hasNext()
    {
        if (size == -1)
        {
            size = 1;
            if (item.getItemListeners(key) != null)
            {
                size += item.getItemListeners(key).size();
            }
        }
        while (peek() == null && nextIndex < size)
        {
            nextIndex++;
        }
        return peek() != null;
    }

    @Override
    public IItemEventListener next()
    {
        //Get current listener
        IItemEventListener re = peek();

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
    protected IItemEventListener peek()
    {
        return get(nextIndex);
    }

    /**
     * Gets the item at the index
     *
     * @return
     */
    protected IItemEventListener get(int index)
    {
        if (index >= 0)
        {
            if (index == 0)
            {
                return item.node;
            }
            else if (item.getItemListeners(key) != null)
            {
                return item.getItemListeners(key).get(index - 1);
            }
        }
        return null;
    }
}
