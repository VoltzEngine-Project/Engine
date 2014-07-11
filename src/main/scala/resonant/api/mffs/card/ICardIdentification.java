package resonant.api.mffs.card;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import resonant.lib.access.Permission;

/**
 * Applied to Item ID cards.
 *
 * @author Calclavia
 */
public interface ICardIdentification extends ICard
{
	public boolean hasPermission(ItemStack itemStack, Permission... permission);

	public boolean addPermission(ItemStack itemStack, Permission... permission);

	public boolean removePermission(ItemStack itemStack, Permission... permission);

	public GameProfile getProfile(ItemStack itemStack);

	public void setProfile(ItemStack itemStack, GameProfile profile);
}
