package com.builtbroken.mc.lib.access;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Applied to tileEntities that contain an access profile that describes how the tile interacts with
 * users
 *
 * @author DarkGuardsman
 */
public interface IProfileContainer
{
    /**
     * Return the active profile of the machine. When calling this avoid editing the profile
     */
    AccessProfile getAccessProfile();

    /**
     * Sets the active profile used by the machine. Should only be called by set profile GUI
     */
    void setAccessProfile(AccessProfile profile);

    /**
     * Strait up yes or no can this user access the tile. Any future checks should be done after the
     * user has accessed the machine
     * @deprecated use {@link #hasNode(EntityPlayer, String)} instead with permission node
     * {@link Permissions#machineOpen}
     */
    @Deprecated
    boolean canAccess(String username);

    /**
     * Called to check if a user has the permission node.
     *
     * @param player - player
     * @param node   - permission node
     * @return true if profile is null or user has the node
     */
    boolean hasNode(EntityPlayer player, String node);

    /**
     * Called to check if a user has the permission node.
     *
     * @param username - player name, used only if the user is offline
     * @param node     - permission node
     * @return true if profile is null or user has the node
     */
    boolean hasNode(String username, String node);

    /**
     * Called when the profile has changed, useful for updating the client
     */
    void onProfileChange();
}
