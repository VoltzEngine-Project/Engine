package resonant.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public interface IExternalInventory
{
    public IExternalInventoryBox getInventory();

    public boolean canStore(ItemStack stack, int slot, ForgeDirection side);

    public boolean canRemove(ItemStack stack, int slot, ForgeDirection side);
}
