package resonant.lib.access;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import resonant.lib.utility.nbt.ISaveObj;

/** Used to define a users access to a terminal based object.
 * 
 * @author DarkGuardsman */
public class AccessUser extends User implements ISaveObj
{
    protected boolean isTempary = false;
    protected NBTTagCompound extraData;
    protected AccessGroup group;
    protected List<String> nodes = new ArrayList<String>();

    public AccessUser(String username)
    {
        super(username);
    }

    public AccessUser(EntityPlayer player)
    {
        super(player.username);
    }

    public AccessGroup getGroup()
    {
        return this.group;
    }

    public AccessUser setGroup(AccessGroup group)
    {
        this.group = group;
        return this;
    }

    public boolean hasNode(String node)
    {
        if (node != null && !node.isEmpty())
        {
            //Remove the wild card from the end
            String newNode = node;
            newNode = newNode.replaceAll(".*", "");

            //Loop threw all super nodes to see if the user has a super node of the sub node
            String[] sub_nodes = newNode.split(".");
            if (sub_nodes != null && sub_nodes.length > 0)
            {
                newNode = "";
                //Build a new node start from the most super node moving to the lowest sub node
                for (int i = 0; i < sub_nodes.length; i++)
                {
                    newNode += (i != 0 ? "." : "") + sub_nodes[i];
                    if (nodes.contains(newNode + ".*") || group != null && group.hasNode(newNode + ".*") || nodes.contains(newNode) || group != null && group.hasNode(newNode))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setString("username", this.username);
        nbt.setCompoundTag("extraData", this.userData());
        NBTTagList usersTag = new NBTTagList();
        for (String str : this.nodes)
        {
            NBTTagCompound accessData = new NBTTagCompound();
            accessData.setString("name", str);
            usersTag.appendTag(accessData);
        }
        nbt.setTag("nodes", usersTag);
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.username = nbt.getString("username");
        this.extraData = nbt.getCompoundTag("extraData");
        NBTTagList userList = nbt.getTagList("nodes");
        this.nodes.clear();
        for (int i = 0; i < userList.tagCount(); ++i)
        {
            this.nodes.add(((NBTTagCompound) userList.tagAt(i)).getString("name"));
        }
    }

    public static AccessUser loadFromNBT(NBTTagCompound nbt)
    {
        AccessUser user = new AccessUser("");
        user.load(nbt);
        return user;
    }

    public AccessUser setTempary(boolean si)
    {
        this.isTempary = si;
        return this;
    }

    /** Used to add other data to the user */
    public NBTTagCompound userData()
    {
        if (this.extraData == null)
        {
            this.extraData = new NBTTagCompound();
        }
        return this.extraData;
    }

}
