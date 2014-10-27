package resonant.api.mffs.card;

import net.minecraft.item.ItemStack;
import resonant.lib.transform.vector.VectorWorld;

public interface ICoordLink
{
	public void setLink(ItemStack itemStack, VectorWorld position);

	public VectorWorld getLink(ItemStack itemStack);
}
