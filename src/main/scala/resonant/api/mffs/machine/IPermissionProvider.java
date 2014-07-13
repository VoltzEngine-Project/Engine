package resonant.api.mffs.machine;

import com.mojang.authlib.GameProfile;
import resonant.lib.access.java.Permission;

/**
 * Used by tiles that provide permissions.
 * @author Calclavia
 */
public interface IPermissionProvider
{
	/**
	 * Does this field matrix provide a specific permission?
	 * */
	public boolean hasPermission(GameProfile profile, Permission permission);
}
