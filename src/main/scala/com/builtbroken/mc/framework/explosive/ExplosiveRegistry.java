package com.builtbroken.mc.framework.explosive;

import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosive;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.explosive.IExplosiveHolder;
import com.builtbroken.mc.api.items.explosives.IExplosiveItem;
import com.builtbroken.mc.core.CommonProxy;
import com.builtbroken.mc.core.ConfigValues;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.lib.data.item.ItemStackWrapper;
import com.builtbroken.mc.lib.world.edit.WorldChangeHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.*;

/**
 * Registry for all explosive which create blasts for anything from bombs to missiles
 *
 * @author Darkguardsman
 */
public final class ExplosiveRegistry
{
    //Constants, in theory these are inlined during compile time
    public static final String EXPONENTIAL = "exponential";


    /** Explosive ID to explosive handler */
    private static final HashMap<String, IExplosiveHandler> idToExplosiveMap = new HashMap();
    /** Mod id to explosives */
    private static final HashMap<String, List<IExplosiveHandler>> modToExplosiveMap = new HashMap();
    /** Item to explosive */
    private static final HashMap<ItemStackWrapper, IExplosiveHandler> itemToExplosive = new HashMap();
    /** Item to size of explosive & scaling type, only used to none IExplosive items */
    private static final HashMap<ItemStackWrapper, Pair<Double, String>> itemToExplosiveSize = new HashMap();
    /** Cache of Item to size of the explosive scaled by {@link ItemStack#stackSize} */
    private static final HashMap<ItemStackWrapper, HashMap<Integer, Double>> itemToExplosiveSizeScaled = new HashMap();
    /** Explosive handler to items */
    public static final HashMap<IExplosiveHandler, List<ItemStackWrapper>> explosiveToItems = new HashMap();
    /** Explosive handler to config scale */
    public static final HashMap<IExplosiveHandler, Float> explosiveConfigScale = new HashMap();

    /**
     * Registers or gets an instanceof of explosive
     *
     * @param modID - modID
     * @param id    - name to register the explosive with
     * @param ex    - explosive instance
     * @return explosive instance
     */
    public static IExplosiveHandler registerOrGetExplosive(String modID, String id, IExplosiveHandler ex)
    {
        return registerOrGetExplosive(modID, id, ex, true, true);
    }

    /**
     * Registers or gets an instanceof of explosive
     *
     * @param modID          - modID
     * @param id             - name to register the explosive with
     * @param ex             - explosive instance
     * @param canDisable     - can the explosive be disable by users
     * @param canConfigScale - can the explosive be scaled by users
     * @return explosive instance
     */
    public static IExplosiveHandler registerOrGetExplosive(String modID, String id, IExplosiveHandler ex, boolean canDisable, boolean canConfigScale)
    {
        if (registerExplosive(modID, id, ex, canDisable, canConfigScale))
        {
            return ex;
        }
        return get(id); //TODO move get call before register call
    }

    /**
     * Registers a new explosive
     *
     * @param modID - modID
     * @param id    - name to register the explosive with
     * @param ex    - explosive instance
     * @return false an explosive is already registered by the ID
     */
    public static boolean registerExplosive(String modID, String id, IExplosiveHandler ex)
    {
        return registerExplosive(modID, id, ex, true, true);
    }

    /**
     * Registers a new explosive
     *
     * @param modID - modID
     * @param id_r  - name to register the explosive with
     * @param ex    - explosive instance
     * @return false an explosive is already registered by the ID
     */
    public static boolean registerExplosive(final String modID, final String id_r, final IExplosiveHandler ex, boolean canDisable, boolean canConfigScale)
    {
        final String id = id_r.toLowerCase();
        if (CommonProxy.explosiveConfig == null || canDisable && CommonProxy.explosiveConfig.getBoolean("enable_" + id, modID, true, ""))
        {
            if (!isRegistered(ex) && !idToExplosiveMap.containsKey(id))
            {
                //Register explosive
                idToExplosiveMap.put(id, ex);
                ex.onRegistered(id, modID);

                //Save explosive to modID
                List<IExplosiveHandler> list;
                if (modToExplosiveMap.containsKey(modID))
                {
                    list = modToExplosiveMap.get(modID);
                }
                else
                {
                    list = new ArrayList();
                }
                list.add(ex);
                modToExplosiveMap.put(modID, list);

                //Generate log to console
                if (ConfigValues.log_registering_explosives)
                {
                    Engine.logger().info("ExplosiveRegistry> Mod: " + modID + "  Registered explosive instance " + ex);
                }

                if (canConfigScale && CommonProxy.explosiveConfig != null)
                {
                    explosiveConfigScale.put(ex, CommonProxy.explosiveConfig.getFloat("scale_" + id, modID, 1f, 0, 100, "Changes the size of the explosive, 0 = nothing, 1 = 100% (same radius), 2 = 200%(2x size/radius, 4x blocks destroyed)"));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Registers an item as an explosive for crafting recipes
     *
     * @param item - item to be used in crafting, must be an instance of {@link IExplosiveItem}
     * @return true if it was registered
     */
    public static boolean registerExplosiveItem(ItemStack item)
    {
        if (item != null && item.getItem() instanceof IExplosiveItem)
        {
            IExplosiveHandler ex = ((IExplosiveItem) item.getItem()).getExplosive(item);
            return registerExplosiveItem(item, ex);
        }
        return false;
    }

    /**
     * Registers an item as an explosive for crafting recipes
     *
     * @param item - item to be used in crafting, must be an instance of {@link IExplosive}
     *             Uses {@link IExplosiveHolder} to get size
     * @return true if it was registered
     */
    public static boolean registerExplosiveItem(ItemStack item, IExplosiveHandler ex)
    {
        if (item != null && ex != null)
        {
            ItemStackWrapper wrapper = new ItemStackWrapper(item);
            if (!itemToExplosive.containsKey(wrapper))
            {
                itemToExplosive.put(wrapper, ex);

                //Get list or make new
                List<ItemStackWrapper> items;
                if (explosiveToItems.containsKey(ex))
                {
                    items = explosiveToItems.get(ex);
                    if (items == null)
                    {
                        items = new ArrayList();
                    }
                }
                else
                {
                    items = new ArrayList();
                }
                //Update entry
                if (!items.contains(wrapper))
                {
                    items.add(wrapper);
                }
                explosiveToItems.put(ex, items);
                return true;
            }
            else
            {
                Engine.error("ExplosiveRegistry: Attempt to register item[" + item + "] to " + ex + " when the item was already registered.");
            }
        }
        else if (item == null)
        {
            Engine.error("ExplosiveRegistry: Attempt to register null item to " + ex + ".");
        }
        else if (ex == null)
        {
            Engine.error("ExplosiveRegistry: Attempt to register item[" + item + "] to null explosive handler.");
        }
        return false;
    }

    /**
     * Registers an item as an explosive for crafting recipes
     *
     * @param item - item to be used in crafting, must be an instance of {@link IExplosive}
     *             Uses {@link IExplosiveHolder} to get size
     * @return true if it was registered
     */
    public static boolean registerExplosiveItem(ItemStack item, IExplosiveHandler ex, double size)
    {
        if (registerExplosiveItem(item, ex))
        {
            ItemStackWrapper wrapper = new ItemStackWrapper(item);
            if (!itemToExplosiveSize.containsKey(wrapper))
            {
                itemToExplosiveSize.put(wrapper, new Pair(size, EXPONENTIAL));
            }
            return true;
        }
        return false;
    }

    /**
     * Removes an item from the explosive item system. Normally
     * only used for JUnit testing but still functions in any
     * other condition.
     *
     * @param stack - item to remove
     * @return true if the item was registered
     */
    public static boolean unregisterExplosiveItem(ItemStack stack)
    {
        ItemStackWrapper wrapper = new ItemStackWrapper(stack);
        if (itemToExplosive.containsKey(wrapper))
        {
            IExplosiveHandler ex = itemToExplosive.get(wrapper);
            itemToExplosive.remove(wrapper);

            if (explosiveToItems.containsKey(ex))
            {
                List<ItemStackWrapper> list = explosiveToItems.get(ex);
                if (list != null)
                {
                    if (list.contains(wrapper))
                    {
                        list.remove(wrapper);
                    }
                    explosiveToItems.put(ex, list);
                }
            }
            if (itemToExplosiveSize.containsKey(wrapper))
            {
                itemToExplosiveSize.remove(stack);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes the explosive from the registry. This is
     * only designed for JUnit testing. However, can
     * be used in dire need.
     *
     * @param ex_name
     */
    public static void unregisterExplosive(String ex_name)
    {
        if(ex_name != null && !ex_name.isEmpty())
        {
            IExplosiveHandler handler = get(ex_name.toLowerCase());
            if (handler != null)
            {
                idToExplosiveMap.remove(ex_name.toLowerCase());
                explosiveConfigScale.remove(handler);
                for (ItemStackWrapper wrapper : explosiveToItems.get(handler))
                {
                    itemToExplosive.remove(wrapper);
                }
                explosiveToItems.remove(handler);
            }
        }
    }


    /**
     * Called to trigger an explosion at the location
     *
     * @param ex           - explosive handler, used to create the IWorldChangeAction instance
     * @param triggerCause - cause of the trigger
     * @param multi        - size of the action, can be scaled by user configs
     * @return if the result completed, was blocked, or failed
     */
    public static WorldChangeHelper.ChangeResult triggerExplosive(World world, double x, double y, double z, IExplosiveHandler ex, TriggerCause triggerCause, double multi, NBTTagCompound tag)
    {
        return triggerExplosive(new Location(world, x, y, z), ex, triggerCause, multi, tag);
    }

    /**
     * Called to trigger an explosion at the location
     *
     * @param loc          - location in the world
     * @param ex           - explosive handler, used to create the IWorldChangeAction instance
     * @param triggerCause - cause of the trigger
     * @param multi        - size of the action, can be scaled by user configs
     * @return if the result completed, was blocked, or failed
     */
    public static WorldChangeHelper.ChangeResult triggerExplosive(Location loc, IExplosiveHandler ex, TriggerCause triggerCause, double multi, NBTTagCompound tag)
    {
        if (isRegistered(ex))
        {
            IWorldChangeAction blast = ex.createBlastForTrigger(loc.oldWorld(), loc.x(), loc.y(), loc.z(), triggerCause, multi * (explosiveConfigScale.containsKey(ex) ? explosiveConfigScale.get(ex) : 1), tag);
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
    public static boolean isRegistered(IExplosiveHandler explosive)
    {
        return explosive != null && explosive.getID() != null && !explosive.getID().isEmpty() && idToExplosiveMap.containsKey(explosive.getID());
    }

    /**
     * Gets the explosive by name
     *
     * @param name - string to match
     * @return explosive if it was registered
     */
    public static IExplosiveHandler get(String name)
    {
        return idToExplosiveMap.get(name.toLowerCase());
    }

    /**
     * Gets the explosive linked the item. If the item
     * is an instance of {@link IExplosiveItem} it will
     * call the {@link IExplosiveItem#getExplosive(ItemStack)}
     * method. If it does not use the {@link IExplosiveItem}
     * interface it will use the map of items to explosives.
     *
     * @param stack - stack, if null will return null
     * @return IExplosiveHandler for this item or null
     */
    public static IExplosiveHandler get(ItemStack stack)
    {
        if (stack != null)
        {
            if (stack.getItem() instanceof IExplosiveItem)
            {
                return ((IExplosiveItem) stack.getItem()).getExplosive(stack);
            }
            return itemToExplosive.get(new ItemStackWrapper(stack));
        }
        return null;
    }

    /**
     * Gets the size of the explosive
     *
     * @param stack
     * @return
     */
    public static double getExplosiveSize(ItemStack stack)
    {
        if (stack != null)
        {
            if (stack.getItem() instanceof IExplosiveItem)
            {
                return ((IExplosiveItem) stack.getItem()).getExplosiveSize(stack);
            }
            return getExplosiveSize(new ItemStackWrapper(stack));
        }
        return 0;
    }

    /**
     * Gets the size of the explosive. If {@link ItemStackWrapper#itemStack}'s
     * {@link ItemStack#getItem()} is an instance of {@link IExplosiveItem} it
     * will return the value of {@link IExplosiveItem#getExplosiveSize(ItemStack)}
     * before attempting to use cached values.
     * <p>
     * If cache values are used and not stored they will be calculated. If no
     * value was registered it will return 0.
     * <p>
     * If 0 is returned assume the explosive does not function or has
     * a single block effect.
     *
     * @param wrapper - wrapper containing a valid ItemStack
     * @return a value equal to or greater than zero in terms of meters
     */
    public static double getExplosiveSize(ItemStackWrapper wrapper)
    {
        if (wrapper != null && wrapper.itemStack != null)
        {
            //Return value from interface first
            if (wrapper.itemStack.getItem() instanceof IExplosiveItem)
            {
                return ((IExplosiveItem) wrapper.itemStack.getItem()).getExplosiveSize(wrapper.itemStack);
            }
            //Return cached values that were registered
            if (itemToExplosiveSize.containsKey(wrapper))
            {
                Pair<Double, String> pair = itemToExplosiveSize.get(wrapper);
                if (pair != null && pair.right().equals(EXPONENTIAL))
                {
                    //Init cache if null
                    if (!itemToExplosiveSizeScaled.containsKey(wrapper))
                    {
                        itemToExplosiveSizeScaled.put(wrapper, new HashMap<Integer, Double>());
                    }

                    HashMap<Integer, Double> map = itemToExplosiveSizeScaled.get(wrapper);
                    //Init cached scale size if null
                    if (!map.containsKey(wrapper.itemStack.stackSize))
                    {
                        map.put(wrapper.itemStack.stackSize, getExplosiveSize(pair.left(), wrapper.itemStack.stackSize));
                    }
                    return map.get(wrapper.itemStack.stackSize);
                }
            }
            return 0;
        }
        return 0;
    }

    /**
     * Used to scale the explosive size based on it's volume to get a new
     * radius value. The current equation uses a 2D circle to calculate
     * the new radius.
     *
     * @param radius        - original size for a single value in m^2
     * @param scaleByFactor - scale factor
     * @return new size
     */
    public static double getExplosiveSize(double radius, double scaleByFactor)
    {
        //Too small
        if (radius <= 0.01 || scaleByFactor <= 0.01)
        {
            return 0;
        }
        //Default
        if (scaleByFactor <= 1.001)
        {
            return radius;
        }
        return Math.sqrt(scaleByFactor * radius * radius);
    }

    /**
     * Returns original list of items registered as explosives. Do not
     * modify this list as it will effect functionality.
     *
     * @param ex - explosive handler, can be null but will return null
     * @return list of items, or null
     */
    public static List<ItemStackWrapper> getItems(IExplosiveHandler ex)
    {
        if (ex != null && explosiveToItems.containsKey(ex))
        {
            return explosiveToItems.get(ex);
        }
        return null;
    }

    /**
     * Gets all explosives registered as a collection
     *
     * @return arraylist
     */
    public static Collection<IExplosiveHandler> getExplosives()
    {
        return idToExplosiveMap.values();
    }

    /**
     * Gets the set of all mods that have registered explosives
     *
     * @return key set from modToExplosiveMap
     */
    public static Set<String> getMods()
    {
        return modToExplosiveMap.keySet();
    }

    /**
     * Gets all explosive registered by the mod
     *
     * @param modID - valid mod id
     * @return list or empty list if mod is not found
     */
    public static List<IExplosiveHandler> getExplosives(String modID)
    {
        if (modToExplosiveMap.containsKey(modID))
        {
            return modToExplosiveMap.get(modID);
        }
        return new ArrayList();
    }

    /**
     * Gets the explosive map containing names to explosives
     *
     * @return hashmap
     */
    public static HashMap<String, IExplosiveHandler> getExplosiveMap()
    {
        return idToExplosiveMap;
    }

    /**
     * NEVER USE THIS METHOD OUTSIDE OF JUNIT TESTING
     * <p>
     * Clears all data stored by the registry. This is designed
     * to wipe the registry between unit tests.
     */
    public static void _clearRegistry()
    {
        if (!Engine.isJUnitTest())
        {
            Engine.error("Clearing the registry should never be called out side of JUnit Testing");
        }
        else
        {
            idToExplosiveMap.clear();
            modToExplosiveMap.clear();
            itemToExplosive.clear();
            itemToExplosiveSize.clear();
            itemToExplosiveSizeScaled.clear();
            explosiveToItems.clear();
        }
    }
}
