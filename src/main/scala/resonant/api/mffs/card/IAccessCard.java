package resonant.api.mffs.card;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import resonant.lib.access.scala.AbstractAccess;

/**
 * Applied to Item ID and group cards.
 *
 * @author Calclavia
 */
public interface IAccessCard extends ICard
{
	public AbstractAccess getAccess(ItemStack stack);

	public void setAccess(ItemStack stack, AbstractAccess access);
}
