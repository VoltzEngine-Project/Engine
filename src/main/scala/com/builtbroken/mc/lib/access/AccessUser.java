package com.builtbroken.mc.lib.access;

import com.builtbroken.mc.api.ISave;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to define a users access to a terminal based object.
 *
 * @author DarkGuardsman
 */
public class AccessUser implements ISave
{
    protected String username;
    protected boolean isTempary = false;
    protected NBTTagCompound extraData;
    protected AccessGroup group;
    public List<String> nodes = new ArrayList<String>();

    public AccessUser(String username)
    {
        this.username = username;
    }

    public AccessUser(EntityPlayer player)
    {
        this(player.getCommandSenderName());
    }

    public static AccessUser loadFromNBT(NBTTagCompound nbt)
    {
        AccessUser user = new AccessUser("");
        user.load(nbt);
        return user;
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

    /**
     * Checks if this or it's supper group has the permission node
     */
    public boolean hasNode(String node)
    {
        return hasExactNode(node) || hasNodeInUser(node) || this.getGroup() != null && this.getGroup().hasNode(node);
    }

    public boolean hasNodeInUser(String node)
    {
        String tempNode = node.replace(".*", "");
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

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        nbt.setString("username", this.username);
        nbt.setTag("extraData", this.userData());
        NBTTagList usersTag = new NBTTagList();
        for (String str : this.nodes)
        {
            NBTTagCompound accessData = new NBTTagCompound();
            accessData.setString("name", str);
            usersTag.appendTag(accessData);
        }
        nbt.setTag("permissions", usersTag);
        return nbt;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.username = nbt.getString("username");
        this.extraData = nbt.getCompoundTag("extraData");
        NBTTagList userList = nbt.getTagList("permissions", 10);
        this.nodes.clear();
        for (int i = 0; i < userList.tagCount(); ++i)
        {
            this.nodes.add(userList.getCompoundTagAt(i).getString("name"));
        }
    }

    public AccessUser setTempary(boolean si)
    {
        this.isTempary = si;
        return this;
    }

    /**
     * Used to add other data to the user
     */
    public NBTTagCompound userData()
    {
        if (this.extraData == null)
        {
            this.extraData = new NBTTagCompound();
        }
        return this.extraData;
    }

    public String getName()
    {
        return this.username;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof String)
        {
            return ((String) obj).equalsIgnoreCase(this.getName());
        }
        return obj instanceof AccessUser && ((AccessUser) obj).getName().equalsIgnoreCase(this.getName());
    }

    @Override
    public String toString()
    {
        return "[User:" + this.getName() + "]";
    }

}
