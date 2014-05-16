package resonant.lib.access;

/** Constants that represent nodes by which machines and entities used in combination with
 * ISpecialAccess to limit users on what they can do. These nodes should be used in the same way by
 * all machines, entities, and other mods. Too change the meaning of the node will make it difficult
 * to offer universal meaning for all machines. As well would create the need to add a per node per
 * machine per group access list making it more complicated for users to use.
 * 
 * @author DarkGuardsman */
public class Nodes
{
    //Inventory only nodes, overridden by machine nodes
    public static final String INV = "inv.*";
    public static final String INV_OPEN = "inv.open";
    public static final String INV_TAKE = "inv.take";
    public static final String INV_GIVE = "inv.give";
    public static final String INV_EDIT = "inv.edit";
    public static final String INV_CHANGE = "inv.change";
    public static final String INV_LOCK = "inv.lock";
    public static final String INV_UNLOCK = "inv.unlock";
    public static final String INV_DISABLE = "inv.disable";
    public static final String INV_ENABLE = "inv.enable";

    //Master machines nodes, overrides all lower nodes of the same type
    public static final String MACHINE = "machine.*";
    public static final String MACHINE_OPEN = "machine.open";
    public static final String MACHINE_LOCK = "machine.lock";
    public static final String MACHINE_UNLOCK = "machine.unlock";
    public static final String MACHINE_ENABLE = "machine.enable";
    public static final String MACHINE_DISABLE = "machine.disable";
    public static final String MACHINE_TURN_ON = "machine.on";
    public static final String MACHINE_TURN_OFF = "machine.off";
    public static final String MACHINE_CONFIG = "machine.config";
    public static final String MACHINE_UPGRADE = "machine.upgrade";
    public static final String MACHINE_DOWNGRADE = "machine.downgrade";

    //Group nodes, these are almost always held by only admins and owners
    public static final String PROFILE = "profile.*";
    public static final String PROFILE_CREATE_GROUP = "profile.group.add";
    public static final String PROFILE_REMOVE_GROUP = "profile.group.remove";
    public static final String PROFILE_EDIT_GROUP = "profile.group.edit";
    public static final String PROFILE_EDIT = "profile.edit";

    public static final String GROUP = "group.*";
    public static final String GROUP_ADD_NODE = "group.node.add";
    public static final String GROUP_REMOVE_NODE = "group.node.remove";
    public static final String GROUP_ADD_USER = "group.user.add";
    public static final String GROUP_REMOVE_USER = "group.user.remove";
    public static final String GROUP_EDIT_USER = "group.user.edit";
    public static final String GROUP_USER_ADD_NODE = "group.user.node.add";
    public static final String GROUP_USER_REMOVE_NODE = "group.user.node.remove";
    public static final String GROUP_ADD_ENTITY = "group.entity.add";
    public static final String GROUP_REMOVE_ENTITY = "group.entity.remove";

    //Applied to group in an access profile
    public static final String PROFILE_OWNER = "profile.owner";
    public static final String PROFILE_ADMIN = "profile.admin";
    public static final String PROFILE_USER = "profile.user";

    //Applied to users in a group, make sure to remove on group change
    public static final String GROUP_OWNER = "group.owner";
    public static final String GROUP_ADMIN = "group.admin";
    public static final String GROUP_USER = "group.user";
}
