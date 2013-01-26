package universalelectricity.prefab.microblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMicroblock extends TileEntity implements IMicroblock
{
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
