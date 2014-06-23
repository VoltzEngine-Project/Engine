package resonant.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IInventoryProvider
{
	public IExternalInventory getInventory();

	public boolean canStore(ItemStack stack, int slot, ForgeDirection side);

	public boolean canRemove(ItemStack stack, int slot, ForgeDirection side);
}
