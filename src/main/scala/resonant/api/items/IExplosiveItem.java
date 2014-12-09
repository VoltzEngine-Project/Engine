package resonant.api.items;

import net.minecraft.item.ItemStack;
import resonant.api.explosive.IExplosive;

/**
 * Created by robert on 12/2/2014.
 */
public interface IExplosiveItem
{
    public IExplosive getExplosive(ItemStack stack);
}
