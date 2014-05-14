package resonant.lib.access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/** Handler for the default group loaded by all machines that use AccessProfiles. Includes functions
 * that are helpful when dealing with access profiles. It is suggested to never modify the default
 * group unless there is not other way. However, any access node needs to be registered threw this
 * class to allow other things to access it. This also include applying those nodes to the default
 * groups.
 * 
 * @author DarkGuardsman */
public class AccessUtility
{
    /** Global list of all nodes used as permissions for access profiles */
    public static final Set<String> nodes = new LinkedHashSet<String>();
    /** Map of default groups and those group permissions nodes. Used to build a new group set. */
    public static final HashMap<String, List<String>> groupDefaultNodes = new LinkedHashMap<String, List<String>>();
    /** Map of default groups and the group it extends. Used to build a new group set. */
    public static final HashMap<String, String> groupDefaultExtends = new LinkedHashMap<String, String>();

    //Pre-loads the default groups
    static
    {
        List<String> list = new ArrayList<String>();
        //Owner group defaults
        list.add(Nodes.PROFILE_OWNER);
        list.add(Nodes.INV_DISABLE);
        list.add(Nodes.INV_ENABLE);
        list.add(Nodes.PROFILE);

        //Admin group defaults
        List<String> list2 = new ArrayList<String>();
        list2.add(Nodes.PROFILE_ADMIN);
        list2.add(Nodes.INV_EDIT);
        list2.add(Nodes.INV_LOCK);
        list2.add(Nodes.INV_UNLOCK);
        list2.add(Nodes.INV_CHANGE);
        list2.add(Nodes.GROUP);

        //User group defaults
        List<String> list3 = new ArrayList<String>();
        list3.add(Nodes.PROFILE_USER);
        list3.add(Nodes.INV_OPEN);
        list3.add(Nodes.INV_TAKE);
        list3.add(Nodes.INV_GIVE);

        createDefaultGroup("user", null, list3);
        createDefaultGroup("admin", "user", list2);
        createDefaultGroup("owner", "admin", list);
    }

    /** Creates a default group for all machines to use. Only add a group if there is no option to
     * really manage the group's settings
     * 
     * @param name - group name
     * @param prefabGroup - group this should extend. Make sure it exists.
     * @param nodes - all commands or custom nodes */
    public static void createDefaultGroup(String name, String prefabGroup, List<String> nodes)
    {
        if (name != null)
        {
            groupDefaultNodes.put(name, nodes);
            groupDefaultExtends.put(name, prefabGroup);
        }
    }

    /** Creates a default group for all machines to use. Only add a group if there is no option to
     * really manage the group's settings
     * 
     * @param name - group name
     * @param prefabGroup - group this should extend. Make sure it exists.
     * @param nodes - all commands or custom nodes */
    public static void createDefaultGroup(String name, String prefabGroup, String... nodes)
    {
        createDefaultGroup(name, prefabGroup, nodes != null ? Arrays.asList(nodes) : null);
    }

    /** Builds a new default group list for a basic machine */
    public static List<AccessGroup> buildNewGroup()
    {
        List<AccessGroup> groups = new ArrayList<AccessGroup>();

        //Create groups and load nodes
        for (Entry<String, List<String>> entry : groupDefaultNodes.entrySet())
        {
            AccessGroup group = new AccessGroup(entry.getKey());
            if (entry.getValue() != null)
            {
                for (String string : entry.getValue())
                {
                    group.addNode(string);
                }
            }
            groups.add(group);
        }

        //Set group to extend each other
        for (Entry<String, String> entry : groupDefaultExtends.entrySet())
        {
            if (entry.getKey() != null && !entry.getKey().isEmpty())
            {
                AccessGroup group = getGroup(groups, entry.getKey());
                AccessGroup groupToExtend = getGroup(groups, entry.getValue());
                if (group != null && groupToExtend != null)
                {
                    group.setToExtend(groupToExtend);
                }
            }
        }

        return groups;
    }

    /** Builds then loaded a new default group set into the terminal */
    public static void loadNewGroupSet(IProfileContainer container)
    {
        if (container != null)
            loadNewGroupSet(container.getAccessProfile());
    }

    public static void loadNewGroupSet(AccessProfile profile)
    {
        if (profile != null)
        {
            List<AccessGroup> groups = buildNewGroup();
            for (AccessGroup group : groups)
                profile.addGroup(group);
        }
    }

    /** Picks a group out of a list using the groups name */
    public static AccessGroup getGroup(Collection<AccessGroup> groups, String name)
    {
        for (AccessGroup group : groups)
        {
            if (group.getName().equalsIgnoreCase(name))
            {
                return group;
            }
        }
        return null;
    }

    /** Registers a node with the master list making it available */
    public static void register(String node)
    {
        if (!nodes.contains(node))
        {
            nodes.add(node);
        }
    }

}
