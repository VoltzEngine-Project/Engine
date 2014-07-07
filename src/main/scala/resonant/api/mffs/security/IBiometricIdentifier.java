package resonant.api.mffs.security;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import resonant.api.blocks.IBlockFrequency;
import resonant.lib.access.Permission;

/**
 * Applied to Biometric Identifiers (extends TileEntity).
 */
public interface IBiometricIdentifier extends IBlockFrequency
{
	/**
	 * Is this specific permission granted to this specific user profile?
	 *
	 * @param profile    - Minecraft profile.
	 * @param permission - The permission.
	 * @return True if there is permission
	 */
	public boolean hasPermission(GameProfile profile, Permission permission);
}
