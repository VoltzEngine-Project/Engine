package calclavia.api.atomicscience;

import net.minecraft.item.ItemStack;

public interface IFissileMaterial
{
	public void onFissile(ItemStack itemStack, IReactor reactor);
}
