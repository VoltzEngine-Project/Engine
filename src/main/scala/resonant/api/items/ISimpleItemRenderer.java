package resonant.api.items;

import net.minecraft.item.ItemStack;

/** Very simple item renderer used by Resonant Engine to render items as tiles */
public interface ISimpleItemRenderer
{
    void renderInventoryItem(ItemStack itemStack);
}
