package universalelectricity.prefab.microblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class TileEntityMicroblock extends TileEntityAdvanced implements IMicroblock
{
	public boolean[] isOccupied = new boolean[6];
	private IMicroComponent[] microSides = new IMicroComponent[6];

	@Override
	public IMicroComponent[] getMicroComponents()
	{
		return this.microSides;
	}

	@Override
	public boolean placeComponent(ItemStack placingItem, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (placingItem.getItem() instanceof IMicroComponent)
		{
			this.microSides[side] = (IMicroComponent) placingItem.getItem();
			return true;
		}
		return false;
	}
}
