package resonant.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/** Used by tiles to check if it can be removed and how. Do not use these methods to remove your own
 * tile. As this could be used for other checks that may cause no effect on the tile.
 * 
 * @author Darkguardsman */
public abstract interface IRemovable
{
    /** Gets all the items dropped when the tile is removed */
    public List<ItemStack> getRemovedItems(EntityPlayer entity);

    /** Implementation of IRemovable were the default removable is shift + wrench */
    public static interface ISneakWrenchable extends IRemovable
    {

    }

    /** Implementation of IRemovable were the default removable is wrench */
    public static interface IWrenchable extends IRemovable
    {

    }

    /** Implementation of IRemovable were the default removable is shift + right click with empty
     * hand */
    public static interface ISneakPickup extends IRemovable
    {

    }

    /** Implementation of IRemovable were the default removable is right click with empty hand */
    public static interface IPickup extends IRemovable
    {

    }

    /** Implementation of IRemovable were the default removable is shift + wrench */
    public static interface ICustomRemoval extends IRemovable
    {
        /** Can the tile be removed by the player. Use this to force tool interaction for removal. */
        public boolean canBeRemoved(EntityPlayer entity);
    }
}
