package resonant.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/** Used by tiles to check if it can be removed and how.
 * 
 * @author Darkguardsman */
public interface IRemovable
{
    /** Can the tile be removed by the player. Use this to force tool interaction for removal. */
    public boolean canBeRemoved(EntityPlayer entity);

    /** Gets all the items dropped when the tile is removed */
    public List<ItemStack> getRemovedItems(EntityPlayer entity);
}
