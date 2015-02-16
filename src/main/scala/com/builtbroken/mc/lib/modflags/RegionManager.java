package com.builtbroken.mc.lib.modflags;

import com.builtbroken.mc.lib.access.AccessGroup;
import com.builtbroken.mc.lib.access.AccessProfile;
import com.builtbroken.mc.lib.access.Permission;
import com.builtbroken.mc.lib.access.Permissions;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by robert on 2/16/2015.
 */
public class RegionManager
{
    private static HashMap<Integer, RegionController> controllersByDim = new HashMap();

    public static final Permission root = new Permission("region");
    public static final Permission interaction = root.addChild("interaction");
    public static final Permission rightClick = interaction.addChild("rightclick");
    public static final Permission leftClick = interaction.addChild("leftclick");
    public static final Permission break_block = interaction.addChild("break");

    public static RegionController getControllerForWorld(World world)
    {
        if (world != null)
        {
            return getControllerForDim(world.provider.dimensionId);
        }
        return null;
    }

    public static RegionController getControllerForDim(int dim)
    {
        if (!controllersByDim.containsKey(dim))
        {
            controllersByDim.put(dim, new RegionController(dim));
        }
        return controllersByDim.get(dim);
    }

    public static AccessProfile generateDefaultAccessProfile()
    {
        AccessProfile profile = new AccessProfile();
        //Create owner group
        AccessGroup owner_group = new AccessGroup("owner");
        owner_group.addNode(Permissions.profile.toString());

        //Create admin group
        AccessGroup admin_group = new AccessGroup("admin");
        owner_group.addNode(Permissions.group.toString());

        //Create user group
        AccessGroup user_group = new AccessGroup("user");
        user_group.addNode(rightClick.toString());
        user_group.addNode(leftClick.toString());
        user_group.addNode(break_block.toString());


        //Set groups to extend each other
        admin_group.setToExtend(user_group);
        owner_group.setToExtend(admin_group);

        //Add groups to profile
        profile.addGroup(user_group);
        profile.addGroup(admin_group);
        profile.addGroup(owner_group);
        return profile;
    }
}
