package resonant.lib.content;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/** Automatically gets the next avaliable ID for a mod.
 * 
 * @author Calclavia */
public class IDManager
{
    private static final int ITEM_SHIFT = 256;

    /** Auto ID Management */
    private final int blockIDBase;
    private final int itemIDBase;

    private int nextBlockID;
    private int nextItemID;

    public IDManager(int blockIDBase, int itemIDBase)
    {
        nextBlockID = this.blockIDBase = blockIDBase;
        nextItemID = this.itemIDBase = itemIDBase;
    }

    public int getNextBlockID()
    {
        return nextBlockID++;
    }

    public int getNextItemID()
    {
        return nextItemID++;
    }

    public int getNextItemID(Configuration config, String name)
    {
        int assignedID = getNextItemID();
        Property prop = config.getItem(name, assignedID);
        int configID = prop.getInt(assignedID);

        if (Item.itemsList[configID] == null && configID >= Block.blocksList.length)
        {
            prop.set(configID);
            return configID;
        }
        else
        {
            for (int x = Item.itemsList.length - 1; x >= ITEM_SHIFT; x--)
            {
                if (Item.itemsList[x] == null)
                {
                    prop.set(x);
                    return prop.getInt();
                }
            }
        }

        return configID;
    }
}
