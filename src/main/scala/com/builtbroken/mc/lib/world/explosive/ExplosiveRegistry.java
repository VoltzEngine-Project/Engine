package com.builtbroken.mc.lib.world.explosive;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosive;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.explosive.IExplosiveHolder;
import com.builtbroken.mc.api.items.IExplosiveHolderItem;
import com.builtbroken.mc.api.items.IExplosiveItem;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.WorldChangeHelper;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
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
    /** Explosive ID to explosive handler */
    private static final HashMap<String, IExplosiveHandler> idToExplosiveMap = new HashMap();
    /** Mod id to explosives */
    private static final HashMap<String, List<IExplosiveHandler>> modToExplosiveMap = new HashMap();
    /** Item to explosive */
    private static final HashMap<ItemStackWrapper, IExplosiveHandler> itemToExplosive = new HashMap();
    /** Item to size of explosive, only used to none IExplosive items */
    private static final HashMap<ItemStackWrapper, Double> itemToExplosiveSize = new HashMap();
    /** Explosive handler to items */
    public static final HashMap<IExplosiveHandler, List<ItemStackWrapper>> explosiveToItems = new HashMap();

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
        if (registerExplosive(modID, id, ex))
        {
            return ex;
        }
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
    public static boolean registerExplosive(String modID, String id, IExplosiveHandler ex)
    {
        if (Engine.explosiveConfig == null || Engine.explosiveConfig.getBoolean("enable_" + id, modID, true, ""))
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
                if (Engine.log_registering_explosives)
                {
                    Engine.instance.logger().info("ExplosiveRegistry> Mod: " + modID + "  Registered explosive instance " + ex);
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
     *             Uses {@link com.builtbroken.mc.api.items.IExplosiveHolderItem} to get size
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
                itemToExplosiveSize.put(wrapper, size);
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
     * Called to trigger an explosion at the location
     *
     * @param ex           - explosive handler, used to create the IWorldChangeAction instance
     * @param triggerCause - cause of the trigger
     * @param multi        - size of the action
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
     * @param multi        - size of the action
     * @return if the result completed, was blocked, or failed
     */
    public static WorldChangeHelper.ChangeResult triggerExplosive(Location loc, IExplosiveHandler ex, TriggerCause triggerCause, double multi, NBTTagCompound tag)
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
        return idToExplosiveMap.get(name);
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
            if (stack.getItem() instanceof IExplosiveHolderItem)
            {
                return ((IExplosiveHolderItem) stack.getItem()).getExplosiveSize(stack);
            }
            return getExplosiveSize(new ItemStackWrapper(stack));
        }
        return 0;
    }

    /**
     * Gets the size of the explosive
     *
     * @param stack
     * @return
     */
    public static double getExplosiveSize(ItemStackWrapper stack)
    {
        if (stack != null && stack.itemStack != null)
        {
            if (stack.itemStack.getItem() instanceof IExplosiveHolderItem)
            {
                return ((IExplosiveHolderItem) stack.itemStack.getItem()).getExplosiveSize(stack.itemStack);
            }
            if (itemToExplosiveSize.containsKey(stack))
            {
                return itemToExplosiveSize.get(stack);
            }
            return 0;
        }
        return 0;
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
}
