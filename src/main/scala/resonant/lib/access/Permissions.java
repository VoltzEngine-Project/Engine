package resonant.lib.access;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Constants that represent permissions by which machines and entities used in combination with
 * ISpecialAccess to limit users on what they can do. These permissions should be used in the same way by
 * all machines, entities, and other mods. Too change the meaning of the node will make it difficult
 * to offer universal meaning for all machines. As well would create the need to add a per node per
 * machine per group access list making it more complicated for users to use.
 *
 * @author DarkGuardsman
 */
public class Permissions
{
	public static final Permissions instance = new Permissions();

	/**
	 * Global list of all permissions
	 */
	public final Set<Permission> permissions = new LinkedHashSet();

	public final Permission root = new Permission("root");

	//Inventory only permissions, overridden by machine permissions
	public final Permission inventory = root.addChild("inventory");
	public final Permission inventoryOpen = inventory.addChild("open");
	public final Permission inventoryInput = inventory.addChild("input");
	public final Permission inventoryOutput = inventory.addChild("output");
	public final Permission inventoryModify = inventory.addChild("modify");
	public final Permission inventoryLock = inventory.addChild("lock");
	public final Permission inventoryUnlock = inventory.addChild("unlock");
	public final Permission inventoryDisable = inventory.addChild("disable");
	public final Permission inventoryEnable = inventory.addChild("enable");

	//Master machines permissions, overrides all lower permissions of the same type
	public final Permission machine = root.addChild("machine");
	public final Permission machineOpen = machine.addChild("open");
	public final Permission machineLock = machine.addChild("lock");
	public final Permission machineUnlock = machine.addChild("unlock");
	public final Permission machineEnable = machine.addChild("enable");
	public final Permission machineDisable = machine.addChild("disable");
	public final Permission machineTurnOn = machine.addChild("on");
	public final Permission machineTurnOff = machine.addChild("off");
	public final Permission machineConfigure = machine.addChild("config");
	public final Permission machineUpgrade = machine.addChild("upgrade");
	public final Permission machineDowngrade = machine.addChild("downgrade");

	//Group permissions, these are almost always held by only admins and owners
	public final Permission profile = root.addChild("profile");
	public final Permission profileAddGroup = profile.addChild("addGroup");
	public final Permission profileRemoveGroup = profile.addChild("removeGroup");
	public final Permission profileModifyGroup = profile.addChild("modifyGroup");

	public final Permission group = root.addChild("group");
	public final Permission groupPermission = group.addChild("permission");
	public final Permission groupPermissionAdd = groupPermission.addChild("add");
	public final Permission groupPermissionRemove = groupPermission.addChild("remove");

	public final Permission groupUser = group.addChild("user");
	public final Permission groupUserAdd = groupUser.addChild("add");
	public final Permission groupUserRemove = groupUser.addChild("remove");
	public final Permission groupUserModify = groupUser.addChild("modify");

	public final Permission groupUserPermission = group.addChild("permission");
	public final Permission groupUserPermissionAdd = groupUserPermission.addChild("add");
	public final Permission groupUserPermissionRemove = groupUserPermission.addChild("remove");

	public final Permission groupEntity = group.addChild("entity");
	public final Permission GROUP_ADD_ENTITY = groupEntity.addChild("add");
	public final Permission GROUP_REMOVE_ENTITY = groupEntity.addChild("remove");
	
	//Default group ID nodes for the entire profile
	public final Permission PROFILE_OWNER = profile.addChild("owner");
	public final Permission PROFILE_ADMIN = profile.addChild("admin");
	public final Permission PROFILE_USER = profile.addChild("user");

	//Default user ID nodes for a single group
	public final Permission GROUP_OWNER = group.addChild("owner");
	public final Permission GROUP_ADMIN = group.addChild("admin");
	public final Permission GROUP_USER = group.addChild("user");
	
	public static Permissions get()
	{
		return instance;
	}
}
