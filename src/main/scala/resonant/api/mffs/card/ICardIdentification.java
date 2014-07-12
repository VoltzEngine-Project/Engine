package resonant.api.mffs.card;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import resonant.lib.access.java.Permission;

/**
 * Applied to Item ID cards.
 *
 * @author Calclavia
 */
@Deprecated
public interface ICardIdentification extends ICard
{
	public boolean hasPermission(ItemStack itemStack, Permission... permission);

	public boolean addPermission(ItemStack itemStack, Permission... permission);

	public boolean removePermission(ItemStack itemStack, Permission... permission);

	public GameProfile getUsername(ItemStack itemStack);

	public void setUsername(ItemStack itemStack, String profile);
}
