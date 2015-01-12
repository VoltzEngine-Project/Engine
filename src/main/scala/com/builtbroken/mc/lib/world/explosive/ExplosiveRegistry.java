package com.builtbroken.mc.lib.world.explosive;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosive;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.mod.config.Config;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.IWorldChangeAction;
import com.builtbroken.mc.lib.world.edit.WorldChangeHelper;

import java.util.*;

/**
 * Registry for all explosive which create blasts for anything from bombs to missiles
 * @author Darkguardsman
 */
public final class ExplosiveRegistry
{
    private static final HashMap<String, IExplosive> idToExplosiveMap = new HashMap();
    private static final HashMap<String, List<IExplosive>> modToExplosiveMap = new HashMap();

    @Config
    public static boolean LOG_REGISTERING_EXPLOSIVES = true;

    /**
     * Registers or gets an instanceof of explosive
     *
     * @param modID - modID
     * @param id    - name to register the explosive with
     * @param ex    - explosive instance
     * @return explosive instance
     */
    public static IExplosive registerOrGetExplosive(String modID, String id, IExplosive ex)
    {
        if (registerExplosive(modID, id, ex))
            return ex;
        return get(id);
    }

    /**
     * Registers a new explosive
     *
     * @param modID - modID
     * @param id    - name to register the explosive with
     * @param ex    - explosive instance
     * @return false an explosive is already registered by the ID
     */
    public static boolean registerExplosive(String modID, String id, IExplosive ex)
    {
        if (!isRegistered(ex) && !idToExplosiveMap.containsKey(id))
        {
            //Register explosive
            idToExplosiveMap.put(id, ex);
            ex.onRegistered(id, modID);

            //Save explosive to modID
            List<IExplosive> list;
            if(modToExplosiveMap.containsKey(modID))
                list = modToExplosiveMap.get(modID);
            else
                list = new ArrayList();
            list.add(ex);
            modToExplosiveMap.put(modID, list);

            //Generate log to console
            if(LOG_REGISTERING_EXPLOSIVES)
                References.LOGGER.info("ExplosiveRegistry> Mod: " + modID + "  Registered explosive instance " + ex);

            return true;
        }
        return false;
    }

    /**
     * Called to trigger an explosion at the location
     *
     * @param ex           - explosive handler, used to create the IWorldChangeAction instance
     * @param triggerCause - cause of the trigger
     * @param multi        - size of the action
     * @return if the result completed, was blocked, or failed
     */
    public static WorldChangeHelper.ChangeResult triggerExplosive(World world, double x, double y, double z, IExplosive ex, TriggerCause triggerCause, int multi, NBTTagCompound tag)
    {
        return triggerExplosive(new Location(world, x, y, z), ex, triggerCause, multi, tag);
    }

    /**
     * Called to trigger an explosion at the location
     *
     * @param loc          - location in the world
     * @param ex           - explosive handler, used to create the IWorldChangeAction instance
     * @param triggerCause - cause of the trigger
     * @param multi        - size of the action
     * @return if the result completed, was blocked, or failed
     */
    public static WorldChangeHelper.ChangeResult triggerExplosive(Location loc, IExplosive ex, TriggerCause triggerCause, int multi, NBTTagCompound tag)
    {
        if (isRegistered(ex))
        {
            IWorldChangeAction blast = ex.createBlastForTrigger(loc.world(), loc.x(), loc.y(), loc.z(), triggerCause, multi, tag);
            return WorldChangeHelper.doAction(loc, blast, triggerCause);
        }
        return WorldChangeHelper.ChangeResult.FAILED;
    }

    /**
     * Checks if the explosive is already registered.
     *
     * @param explosive - explosive
     * @return false if the explosive id is empty, or not contained in the registry map
     */
    public static boolean isRegistered(IExplosive explosive)
    {
        return explosive.getID() != null && !explosive.getID().isEmpty() && idToExplosiveMap.containsKey(explosive.getID());
    }

    /**
     * Gets the explosive by name
     *
     * @param name - string to match
     * @return explosive if it was registered
     */
    public static IExplosive get(String name)
    {
        return idToExplosiveMap.get(name);
    }

    /**
     * Gets all explosives registered as a collection
     *
     * @return arraylist
     */
    public static Collection<IExplosive> getExplosives()
    {
        return idToExplosiveMap.values();
    }

    /** Gets the set of all mods that have registered explosives
     * @return key set from modToExplosiveMap
     */
    public static Set<String> getMods()
    {
        return modToExplosiveMap.keySet();
    }

    /** Gets all explosive registered by the mod
     * @param modID - valid mod id
     * @return list or empty list if mod is not found
     */
    public static List<IExplosive> getExplosives(String modID)
    {
        if(modToExplosiveMap.containsKey(modID))
            return modToExplosiveMap.get(modID);
        return new ArrayList();
    }

    /**
     * Gets the explosive map containing names to explosives
     *
     * @return hashmap
     */
    public static HashMap<String, IExplosive> getExplosiveMap()
    {
        return idToExplosiveMap;
    }


}
