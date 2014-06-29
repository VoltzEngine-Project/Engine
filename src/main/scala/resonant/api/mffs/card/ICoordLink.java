package resonant.api.mffs.card;

import net.minecraft.item.ItemStack;
import universalelectricity.core.transform.vector.VectorWorld;

public interface ICoordLink
{
    public void setLink(ItemStack itemStack, VectorWorld position);

    public VectorWorld getLink(ItemStack itemStack);
}
