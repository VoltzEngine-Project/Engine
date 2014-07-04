package resonant.api.mffs.security;

import com.mojang.authlib.GameProfile;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import resonant.api.mffs.IActivatable;
import resonant.api.mffs.IBiometricIdentifierLink;
import resonant.api.mffs.fortron.IFortronFrequency;
import resonant.api.mffs.modules.IModuleAcceptor;
import resonant.lib.access.Permission;

import java.util.Set;

public interface IInterdictionMatrix extends IInventory, IFortronFrequency, IModuleAcceptor, IBiometricIdentifierLink, IActivatable
{
	/**
	 * Is this specific permission granted to this specific user profile?
	 *
	 * @param profile    - Minecraft profile.
	 * @param permission - The permission.
	 * @return
	 */
	public boolean hasPermission(GameProfile profile, Permission permission);

	/**
	 * The range in which the Interdiction Matrix starts warning the player.
	 *
	 * @return
	 */
	public int getWarningRange();

	/**
	 * The range in which the Interdiction Matrix has an effect on.
	 *
	 * @return
	 */
	public int getActionRange();

	/**
	 * Merges an item into the Interdiction Matrix's safe keeping inventory.
	 *
	 * @param itemStack
	 * @return True if kept, false if dropped.
	 */
	public boolean mergeIntoInventory(ItemStack itemStack);

	public Set<ItemStack> getFilteredItems();

	/**
	 * @return True if the filtering is on ban mode. False if it is on allow-only mode.
	 */
	public boolean getFilterMode();

	@Override
	public int getFortronCost();
}
