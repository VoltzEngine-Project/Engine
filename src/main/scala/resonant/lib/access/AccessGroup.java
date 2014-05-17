package resonant.lib.access;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import resonant.lib.type.Group;
import resonant.lib.utility.nbt.ISaveObj;

/** Permission system group used to track players with the access they have. Used with several
 * different systems include sentry gun AI targeting, and inventory locking.
 * 
 * @author DarkGuardsman */
public class AccessGroup extends Group<AccessUser> implements ISaveObj
{
    protected Set<String> nodes = new LinkedHashSet<String>();
    protected AccessGroup extendGroup;
    protected String extendGroup_name;
    protected long creation_time;

    public AccessGroup(String group_name, AccessUser... users)
    {
        super(group_name, users);
        this.creation_time = System.currentTimeMillis();
    }

    /** Gets the AccessUser object that goes with the user name
     * 
     * @param username - user name of the EntityPlayer
     * @return the exact user, or a fake user to prevent NPE */
    public AccessUser getMember(String username)
    {
        for (AccessUser user : this.members)
        {
            if (user.getName().equalsIgnoreCase(username))
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean addMemeber(AccessUser obj)
    {
        if (super.addMemeber(obj))
        {
            obj.setGroup(this);
            return true;
        }
        return false;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setString("groupName", this.getName());
        if (this.extendGroup_name != null)
        {
            nbt.setString("extendGroup", this.extendGroup_name);
        }
        NBTTagList usersTag = new NBTTagList();
        for (AccessUser user : this.members)
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
        nbt.setTag("nodes", nodesTag);
        nbt.setLong("creationDate", this.creation_time);

    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        //load group name
        this.setName(nbt.getString("groupName"));

        //Load extend group
        if (nbt.hasKey("extendGroup"))
        {
            this.extendGroup_name = nbt.getString("extendGroup");
        }

        //Load users
        NBTTagList userList = nbt.getTagList("users");
        getMembers().clear();

        for (int i = 0; i < userList.tagCount(); ++i)
        {
            AccessUser user = AccessUser.loadFromNBT((NBTTagCompound) userList.tagAt(i));
            this.addMemeber(user);
        }

        //Load permission nodes
        NBTTagList nodeList = nbt.getTagList("nodes");
        this.nodes.clear();
        for (int i = 0; i < nodeList.tagCount(); ++i)
        {
            this.nodes.add(((NBTTagCompound) nodeList.tagAt(i)).getString("name"));
        }

        //Load creation date
        if (nbt.hasKey("creationDate"))
        {
            this.creation_time = nbt.getLong("creationDate");
        }
        else
        {
            this.creation_time = System.currentTimeMillis();
        }
    }

    /** Checks if this or it's supper group has the permission node */
    public boolean hasNode(String node)
    {
        if (node.contains(".*"))
        {
            String tempNode = node.replaceAll(".*", "");
            for (String headNode : nodes)
            {
                if (tempNode.contains(headNode))
                {
                    return true;
                }
            }
        }
        return this.nodes.contains(node) || this.getExtendGroup() != null && this.getExtendGroup().hasNode(node);
    }

    /** Adds a permission node to the group */
    public void addNode(String node)
    {
        this.nodes.add(node);
    }

    /** Removes a permission node from the group */
    public void removeNode(String node)
    {
        //TODO remove sub nodes linked to this node
        if (this.nodes.contains(node))
        {
            this.nodes.remove(node);
        }
    }

    /** Checks if the user is a member of the group */
    public boolean isMemeber(String string)
    {
        return this.members.contains(new AccessUser(string));
    }

    /** Sets this group it extends another group */
    public void setToExtend(AccessGroup group)
    {
        this.extendGroup = group;
        if (this.extendGroup != null)
        {
            this.extendGroup_name = this.extendGroup.getName();
        }
    }

    /** Gets the group this group extends */
    public AccessGroup getExtendGroup()
    {
        return this.extendGroup;
    }

    /** Gets the name of the group this group extends. Only used to init the extend group after
     * loading the group from a save. */
    public String getExtendGroupName()
    {
        return this.extendGroup_name;
    }

    public Set<String> getNodes()
    {
        return nodes;
    }
}
