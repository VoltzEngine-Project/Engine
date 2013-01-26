package universalelectricity.prefab.microblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Applied to all TileEntities that can contain microblocks within it.
 * 
 * @author Calclavia
 * 
 */
public interface IMicroblock
{
	/**
	 * Returns all the micro components within this micro block.
	 * 
	 * @return
	 */
	public IMicroComponent[] getMicroComponents();

	/**
	 * Return true if the micro-component is successfully placed within the micro-block.
	 * 
	 * @param placingItem
	 * @return
	 */
	public boolean placeComponent(ItemStack placingItem, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ);
}
