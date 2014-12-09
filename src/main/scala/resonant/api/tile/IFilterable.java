package resonant.api.tile;

import net.minecraft.item.ItemStack;

/**
 * Applied to TileEntities that can accept a filter.z
 *
 * @author Calclavia
 */
public interface IFilterable
{
	public ItemStack getFilter();

	public void setFilter(ItemStack filter);
}
