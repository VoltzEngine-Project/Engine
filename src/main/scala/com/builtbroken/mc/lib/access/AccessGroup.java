package com.builtbroken.mc.lib.access;

import com.builtbroken.jlib.type.Group;
import com.builtbroken.mc.api.ISave;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

/**
 * Permission system group used to track players with the access they have. Used
 * with several different systems include sentry gun AI targeting, and inventory
 * locking.
 *
 * @author DarkGuardsman
 */
public class AccessGroup implements ISave, Cloneable
{
    /** Name of the group */
    private String name;
    /** System time when the group was created */
    protected long creation_time;

    /** Permission nodes this group contains, not including super nodes from {@link #extendGroup} */
    protected Set<String> nodes = new LinkedHashSet();
    /** Group this group inebriates permissions from */
    protected AccessGroup extendGroup;
    /** Name of the extend group, used mainly for save/load */
    protected String extendGroup_name;

    /** Map of user's names to profiles */
    protected final HashMap<String, AccessUser> username_to_profile = new HashMap();
    /** Map of user's UUID to profiles */
    protected final HashMap<UUID, AccessUser> uuid_to_profile = new HashMap();

    public AccessGroup(String group_name, AccessUser... users)
    {
        this.name = group_name;
        this.creation_time = System.currentTimeMillis();

        for (AccessUser user : users)
        {
            addMember(user);
        }
    }

    /**
     * Gets the AccessUser object that goes with the user name
     *
     * @param username - user name of the EntityPlayer
     * @return the exact user, or a fake user to prevent NPE
     */
    public AccessUser getMember(String username)
    {
        if (username_to_profile.containsKey(username))
        {
            return username_to_profile.get(username);
        }
        return null;
    }

    /**
     * Adds a user profile directly to the group
     *
     * @param obj - access profile with valid username
     * @return true if the profile was valid and added
     */
    public boolean addMember(AccessUser obj)
    {
        if (isValid(obj))
        {
            if (obj.userID != null)
            {
                uuid_to_profile.put(obj.userID, obj);
            }
            username_to_profile.put(obj.username, obj);
            obj.setGroup(this);
            return true;
        }
        return false;
    }

    @Deprecated
    public boolean addMember(String name)
    {
        //TODO trigger super profile that a new member was added
        return getMember(name) == null && addMember(new AccessUser(name));
    }

    /**
     * Adds a user to the group
     *
     * @param player - user with a valid UUID
     * @return true if the user was added
     */
    public boolean addMember(EntityPlayer player)
    {
        //TODO trigger super profile that a new member was added
        return player != null && addMember(new AccessUser(player));
    }

    /**
     * Removes a player from the group
     *
     * @param player - player with a valid UUID
     * @return true if the player was removed using it's UUID
     */
    public boolean removeMember(EntityPlayer player)
    {
        return player != null && removeMember(player.getGameProfile().getId());
    }

    /**
     * Removes a user with a username
     *
     * @param name - user's name
     * @return true if it was contained in {@link #username_to_profile}
     */
    public boolean removeMember(String name)
    {
        return removeMember(getMember(name));
    }

    /**
     * Removes a user's access from this group
     *
     * @param user - user's profile
     * @return true if removed
     */
    public boolean removeMember(AccessUser user)
    {
        //TODO trigger super profile that a member removed
        if (user != null && username_to_profile.containsKey(user.getName()))
        {
            username_to_profile.remove(user.username);
            if (user.userID != null)
            {
                uuid_to_profile.remove(user.userID);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes a user with a {@link UUID}
     *
     * @param id - user's id
     * @return true if it was contained in {@link #uuid_to_profile}
     */
    public boolean removeMember(UUID id)
    {
        if (uuid_to_profile.containsKey(id))
        {
            return removeMember(uuid_to_profile.get(id));
        }
        return false;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        nbt.setString("groupName", this.getName());
        if (this.extendGroup_name != null)
        {
            nbt.setString("extendGroup", this.extendGroup_name);
        }
        NBTTagList usersTag = new NBTTagList();
        for (AccessUser user : this.username_to_profile.values())
        {
            NBTTagCompound accessData = new NBTTagCompound();
            user.save(accessData);
            usersTag.appendTag(accessData);
        }

        nbt.setTag("users", usersTag);

        NBTTagList nodesTag = new NBTTagList();
        for (String str : this.nodes)
        {
            NBTTagCompound accessData = new NBTTagCompound();
            accessData.setString("name", str);
            nodesTag.appendTag(accessData);
        }
        nbt.setTag("permissions", nodesTag);
        nbt.setLong("creationDate", this.creation_time);
        return nbt;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        // load group name
        this.setName(nbt.getString("groupName"));

        // Load extend group
        if (nbt.hasKey("extendGroup"))
        {
            this.extendGroup_name = nbt.getString("extendGroup");
        }

        // Load users
        NBTTagList userList = nbt.getTagList("users", 10);
        getMembers().clear();

        for (int i = 0; i < userList.tagCount(); ++i)
        {
            AccessUser user = AccessUser.loadFromNBT(userList.getCompoundTagAt(i));
            this.addMember(user);
        }

        // Load permission permissions
        NBTTagList nodeList = nbt.getTagList("permissions", 10);
        this.nodes.clear();
        for (int i = 0; i < nodeList.tagCount(); ++i)
        {
            this.nodes.add(nodeList.getCompoundTagAt(i).getString("name"));
        }

        // Load creation date
        if (nbt.hasKey("creationDate"))
        {
            this.creation_time = nbt.getLong("creationDate");
        }
        else
        {
            this.creation_time = System.currentTimeMillis();
        }
    }

    /**
     * Checks if this or it's supper group has the permission node
     */
    public boolean hasNode(String node)
    {
        return hasExactNode(node) || hasNodeInGroup(node) || this.getExtendGroup() != null && this.getExtendGroup().hasNode(node);
    }

    public boolean hasNodeInGroup(String node)
    {
        String tempNode = node.replaceAll(".*", "");
        for (String headNode : nodes)
        {
            if (tempNode.contains(headNode))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasExactNode(String node)
    {
        return this.nodes.contains(node);
    }

    /**
     * Adds a permission node to the group
     */
    public void addNode(String node)
    {
        this.nodes.add(node);
    }

    public void addNode(Permission node)
    {
        addNode(node.toString());
    }

    /**
     * Removes a permission node from the group
     */
    public void removeNode(String node)
    {
        // TODO remove sub permissions linked to this node
        if (this.nodes.contains(node))
        {
            this.nodes.remove(node);
        }
    }

    /**
     * Sets this group it extends another group
     */
    public void setToExtend(AccessGroup group)
    {
        this.extendGroup = group;
        if (this.extendGroup != null)
        {
            this.extendGroup_name = this.extendGroup.getName();
        }
    }

    /**
     * Gets the group this group extends
     */
    public AccessGroup getExtendGroup()
    {
        return this.extendGroup;
    }

    /**
     * Gets the name of the group this group extends. Only used to init the
     * extend group after loading the group from a save.
     */
    public String getExtendGroupName()
    {
        return this.extendGroup_name;
    }

    public Set<String> getNodes()
    {
        return nodes;
    }

    /**
     * Gets a list of all users in the group
     *
     * @return collection of users
     */
    public Collection<AccessUser> getMembers()
    {
        return username_to_profile.values();
    }

    /**
     * Checks if an access profile for a user is valid. Normal
     * checks involve NPE, username, and contains
     *
     * @param obj - valid profile
     * @return true if the profile is valid
     */
    protected boolean isValid(AccessUser obj)
    {
        return obj != null && obj.username != null && !getMembers().contains(obj);
    }

    /**
     * Name of the group
     *
     * @return string
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of the group, warning this
     * may break group connections. As the
     * name is also used to link groups in a
     * profile. Avoid using this directly.
     *
     * @param name - valid name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    @Override
    public AccessGroup clone()
    {
        AccessGroup group = new AccessGroup(this.getName());
        for (String node : getNodes())
        {
            group.getNodes().add(node);
        }
        return group;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Group && ((Group<?>) obj).getName().equalsIgnoreCase(this.getName());
    }

    @Override
    public String toString()
    {
        return "[Group:" + this.getName() + "]";
    }
}
