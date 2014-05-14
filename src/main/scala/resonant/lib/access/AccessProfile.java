package resonant.lib.access;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;
import resonant.lib.utility.nbt.SaveManager;

/** Designed to be used as a container for AccessGroups and AccessUser. If you plan to use this make
 * sure to use it correctly. This is designed to be saved separate from the world save if marked for
 * global access. Which means it can save/load at will from the world file.
 * 
 * @author DarkGuardsman */
public class AccessProfile implements IVirtualObject
{
    /** List of all AccessProfiles defined in the game */
    private static final Set<AccessProfile> globalList = new LinkedHashSet<AccessProfile>();

    /** List of all containers that use this profile to define some part of their functionality */
    private final Set<IProfileContainer> containers = Collections.newSetFromMap(new WeakHashMap<IProfileContainer, Boolean>());

    /** A list of all groups attached to this profile */
    protected Set<AccessGroup> groups = new LinkedHashSet<AccessGroup>();

    /** Display name of the profile for the user to easily read */
    protected String profileName = "";

    /** Only used by global profiles that have no defined container. Defaults to localHost defining
     * the profile as non-global */
    protected String profileID = "LocalHost";

    /** Is this profile global */
    protected boolean global = false;

    /** Save file by which this was loaded. Not currently used */
    protected File saveFile;

    static
    {
        SaveManager.registerClass("AccessProfile", AccessProfile.class);
    }

    public AccessProfile()
    {
        if (global)
        {
            SaveManager.register(this);
            AccessProfile.globalList.add(this);
        }
    }

    public AccessProfile(NBTTagCompound nbt)
    {
        this(nbt, false);
    }

    public AccessProfile(NBTTagCompound nbt, boolean global)
    {
        this();
        this.load(nbt);
        if (this.profileName == null || this.profileID == null)
        {
            if (!global)
            {
                this.generateNew("Default", null);
            }
            else
            {
                this.generateNew("New Group", "global");
            }
        }
    }

    /** Gets an access profile using its name ID */
    public static AccessProfile get(String name)
    {
        for (AccessProfile profile : globalList)
        {
            if (profile.getID().equalsIgnoreCase(name))
            {
                return profile;
            }
        }
        return null;
    }

    /** Generates the default 3 group access profile */
    public AccessProfile generateNew(String name, Object object)
    {
        AccessUtility.loadNewGroupSet(this);
        this.profileName = name;
        name.replaceAll(" ", "");
        String id = null;
        // Created by player for personal use
        if (object instanceof EntityPlayer)
        {
            id = ((EntityPlayer) object).username + "_" + System.currentTimeMillis();
            this.global = true;
        }//Created by a tile
        else if (object instanceof TileEntity || object == null)
        {
            id = "LocalHost:" + name;
        }//created by the game or player for global use
        else if (object instanceof String && ((String) object).equalsIgnoreCase("global"))
        {
            id = "P_" + name + "_" + System.currentTimeMillis();
            this.global = true;
        }
        this.profileID = id;
        return this;
    }

    /** Display name of the profile */
    public String getName()
    {
        return this.profileName;
    }

    /** Save/Global id of the profie */
    public String getID()
    {
        return this.profileID;
    }

    /** Is this a global profile that is can be accessed by all objects */
    public boolean isGlobal()
    {
        return this.global;
    }

    public AccessUser getUserAccess(String username)
    {
        for (AccessGroup group : this.groups)
        {
            AccessUser user = group.getMember(username);
            if (user != null)
            {
                return user;
            }
        }
        return new AccessUser(username);
    }

    public List<AccessUser> getUsers()
    {
        List<AccessUser> users = new ArrayList<AccessUser>();
        for (AccessGroup group : this.groups)
        {
            users.addAll(group.getMembers());
        }
        return users;
    }

    public void addContainer(IProfileContainer container)
    {
        if (!this.containers.contains(container))
        {
            this.containers.add(container);
        }
    }

    public boolean setUserAccess(String player, AccessGroup g)
    {
        return setUserAccess(player, g, true);
    }

    public boolean setUserAccess(String player, AccessGroup g, boolean save)
    {
        return setUserAccess(new AccessUser(player).setTempary(!save), g);
    }

    public boolean setUserAccess(AccessUser user, AccessGroup group)
    {
        boolean bool = false;

        if (user != null && user.getName() != null)
        {
            bool = this.removeUserAccess(user.getName()) && group == null;

            if (group != null)
            {
                bool = group.addMemeber(user);
            }
            if (bool)
            {
                this.onProfileUpdate();
            }
        }
        return bool;
    }

    public boolean removeUserAccess(String player)
    {
        boolean re = false;
        for (AccessGroup group : this.groups)
        {
            AccessUser user = group.getMember(player);
            if (user != null && group.removeMemeber(user))
            {
                re = true;
            }
        }
        if (re)
        {
            this.onProfileUpdate();
        }
        return re;
    }

    public void onProfileUpdate()
    {
        Iterator<IProfileContainer> it = containers.iterator();
        while (it.hasNext())
        {
            IProfileContainer container = it.next();
            if (container != null && this.equals(container.getAccessProfile()))
            {
                container.onProfileChange();
            }
            else
            {
                it.remove();
            }
        }
    }

    public AccessGroup getGroup(String name)
    {
        return AccessUtility.getGroup(this.getGroups(), name);
    }

    public boolean addGroup(AccessGroup group)
    {
        if (!this.groups.contains(group))
        {
            if (this.groups.add(group))
            {
                this.onProfileUpdate();
                return true;
            }
        }
        return false;
    }

    public AccessGroup getOwnerGroup()
    {
        return this.getGroup("owner");
    }

    public Set<AccessGroup> getGroups()
    {
        if (this.groups == null)
        {
            AccessUtility.loadNewGroupSet(this);
        }
        return this.groups;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.profileName = nbt.getString("name");
        this.global = nbt.getBoolean("global");
        this.profileID = nbt.getString("profileID");

        //Load groups
        NBTTagList group_list = nbt.getTagList("groups");
        if (group_list != null && group_list.tagCount() > 0)
        {
            this.groups.clear();
            //Load group save data
            for (int i = 0; i < group_list.tagCount(); i++)
            {
                AccessGroup group = new AccessGroup("");
                group.load((NBTTagCompound) group_list.tagAt(i));
                this.groups.add(group);
            }
            //Set group extensions
            for (AccessGroup group : this.groups)
            {
                if (group.getExtendGroupName() != null)
                {
                    group.setToExtend(this.getGroup(group.getExtendGroupName()));
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setString("name", this.profileName);
        nbt.setBoolean("global", this.global);
        nbt.setString("profileID", this.profileID);
        NBTTagList groupTags = new NBTTagList();
        for (AccessGroup group : this.getGroups())
        {
            NBTTagCompound groupTag = new NBTTagCompound();
            group.save(groupTag);
            groupTags.appendTag(groupTag);
        }
        nbt.setTag("groups", groupTags);
    }

    @Override
    public File getSaveFile()
    {
        if (this.saveFile == null)
        {
            this.saveFile = new File(NBTUtility.getSaveDirectory(MinecraftServer.getServer().getFolderName()), "Access/Profile/" + this.getID() + ".dat");
        }
        return this.saveFile;
    }

    @Override
    public void setSaveFile(File file)
    {
        this.saveFile = file;

    }

    @Override
    public String toString()
    {
        return LanguageUtility.getLocal("info.accessprofile.tostring").replaceAll("%p", this.profileName.toString()).replaceAll("%g", groups.toString());
    }
}
