package calclavia.lib.access;

import java.util.LinkedHashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import calclavia.lib.utility.nbt.ISaveObj;

import com.builtbroken.common.Group;

/** Used by a terminal to track what users are part of each group. As well used to setup access
 * points to the terminal.
 * 
 * @author DarkGuardsman */
public class AccessGroup extends Group<AccessUser> implements ISaveObj
{
    protected LinkedHashSet<String> nodes = new LinkedHashSet<String>();
    protected AccessGroup extendGroup;

    public AccessGroup(String name, AccessUser... js)
    {
        super(name, js);
    }

    public void setToExtend(AccessGroup group)
    {
        this.extendGroup = group;
    }

    public AccessUser getMember(String name)
    {
        for (AccessUser user : this.memebers)
        {
            if (user.getName().equalsIgnoreCase(name))
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setString("groupName", this.getName());
        NBTTagList usersTag = new NBTTagList();
        for (AccessUser user : this.memebers)
        {
            if (!user.isTempary)
            {
                NBTTagCompound accessData = new NBTTagCompound();
                user.save(accessData);
                usersTag.appendTag(accessData);
            }
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
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.setName(nbt.getString("groupName"));
        NBTTagList userList = nbt.getTagList("users");
        this.getMembers().clear();
        for (int i = 0; i < userList.tagCount(); ++i)
        {
            AccessUser user = AccessUser.loadFromNBT((NBTTagCompound) userList.tagAt(i));
            this.addMemeber(user);
        }
        NBTTagList nodeList = nbt.getTagList("nodes");
        this.nodes.clear();
        for (int i = 0; i < nodeList.tagCount(); ++i)
        {
            this.nodes.add(((NBTTagCompound) nodeList.tagAt(i)).getString("name"));
        }
    }

    public boolean hasNode(String node)
    {
        return this.nodes.contains(node);
    }

    public void addNode(String node)
    {
        this.nodes.add(node);
    }

    public void removeNode(String node)
    {
        if (this.nodes.contains(node))
        {
            this.nodes.remove(node);
        }
    }

    public boolean isMemeber(String string)
    {
        return this.memebers.contains(new AccessUser(string));
    }
}
