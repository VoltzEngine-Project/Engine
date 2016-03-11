package com.builtbroken.mc.lib.access;

import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

/**
 * Reference class for storing and access {@link AccessProfile} that are shared
 * across several objects.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2016.
 */
public final class GlobalAccessSystem
{
    private static final HashMap<String, AccessProfile> name_to_profiles = new HashMap();
    //TODO implement an automated unload system


    /**
     * Called to get or create a profile
     *
     * @param name          - unique id of the profile, try to prefix with the mod and machine
     * @param defaultGroups - if true will create default groups
     * @return existing or new access profile
     */
    public static AccessProfile getOrCreateProfile(String name, boolean defaultGroups)
    {
        if (name_to_profiles.containsKey(name) && name_to_profiles.get(name) != null)
        {
            return name_to_profiles.get(name);
        }
        AccessProfile p = loadProfile(name, false);
        if (p == null)
        {
            p = createProfile(name, defaultGroups);
        }
        if (!name_to_profiles.containsKey(name) || name_to_profiles.get(name) == null)
        {
            name_to_profiles.put(name, p);
        }
        return p;
    }

    public static AccessProfile createProfile(String name, boolean defaultGroups)
    {
        AccessProfile profile = new AccessProfile();
        if (defaultGroups)
        {
            AccessUtility.loadNewGroupSet(profile);
        }
        profile.initName(name.trim(), "P_" + name + "_" + System.currentTimeMillis());
        if (!name_to_profiles.containsKey(name) || name_to_profiles.get(name) == null)
        {
            name_to_profiles.put(name, profile);
        }
        return profile;
    }

    /**
     * Called to load a profile from disk
     *
     * @param name   - name of the profile
     * @param create - if file is missing will create new group
     * @return existing profile from save or new profile
     */
    protected static AccessProfile loadProfile(String name, boolean create)
    {
        NBTTagCompound tag = NBTUtility.loadData("bbm/accessProfiles/" + name + ".dat");
        if (!tag.hasNoTags())
        {
            return new AccessProfile(tag, true);
        }
        else if (create)
        {
            return createProfile(name, true);
        }
        return null;
    }
}
