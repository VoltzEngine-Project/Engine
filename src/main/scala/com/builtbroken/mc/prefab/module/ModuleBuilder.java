package com.builtbroken.mc.prefab.module;

import com.builtbroken.mc.api.modules.IModule;
import com.builtbroken.mc.core.Engine;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by robert on 12/28/2014.
 */
public class ModuleBuilder<C extends IModule>
{
    /** Common ID used to save modules to NBT */
    public static final String SAVE_ID = "moduleID";
    /** Map of builder names to module set builders. */
    public static final HashMap<String, ModuleBuilder> MODULE_BUILDERS = new HashMap();

    HashBiMap<String, Class<C>> idToCLassMap = HashBiMap.create();
    HashMap<String, List<String>> modToModules = new HashMap();

    /**
     * Registers a module to be built by this builder
     *
     * @param mod_id - unique mod ID
     * @param name   - unique module ID
     * @param clazz  - class of the module, must have a constructor that takes an {@link ItemStack}
     * @return true if the module registers correctly
     */
    public boolean register(String mod_id, String name, Class<C> clazz)
    {
        //Value checks to prevent other modders from making mistakes
        if (clazz == null)
        {
            throw new IllegalArgumentException("ModuleBuidler.register(" + mod_id + ", " + name + ", clazz) clazz can not be null");
        }

        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("ModuleBuidler.register(" + mod_id + ", name, " + clazz + ") name can not be empty");
        }

        if (mod_id == null || mod_id.isEmpty())
        {
            throw new IllegalArgumentException("ModuleBuidler.register(mod_id, " + name + ", " + clazz + ") mod_id is invalid");
        }

        //Check to make sure we don't override something already registered
        String id = mod_id + "." + name;
        if (!idToCLassMap.containsKey(id))
        {
            //Add module to id list
            idToCLassMap.put(id, clazz);

            //Adds the module to a per mod list for look up later
            List<String> list = null;
            if (modToModules.containsKey(mod_id))
            {
                list = modToModules.get(mod_id);
            }
            if (list == null)
            {
                list = new ArrayList();
            }
            list.add(id);
            modToModules.put(mod_id, list);

            return true;

        }
        return false;
    }

    /**
     * Is the module by the ID registers
     *
     * @param id - unique id for the module
     * @return true if the module is registered
     */
    public boolean isRegistered(String id)
    {
        return this.idToCLassMap.containsKey(id);
    }

    /**
     * Gets all registered IDs for modules
     *
     * @return set of module keys
     */
    public Set<String> getIDs()
    {
        return idToCLassMap.keySet();
    }

    /**
     * Gets the ID for the module
     *
     * @param module - module
     * @return ID
     */
    public String getID(C module)
    {
        Class clazz = module.getClass();
        if (idToCLassMap.inverse().containsKey(clazz))
        {
            return idToCLassMap.inverse().get(clazz);
        }
        return module.getClass().getSimpleName();
    }

    /**
     * Builds the module from the item stack
     *
     * @param stack - item stack that is a module containing the NBT string id
     *              to use to construct the module
     * @return the module or null if something went wrong
     */
    public C build(ItemStack stack)
    {
        if (stack != null && stack.getTagCompound() != null && stack.getTagCompound().hasKey(SAVE_ID))
        {
            String id = stack.getTagCompound().getString(SAVE_ID);
            if (idToCLassMap.containsKey(id))
            {
                if (idToCLassMap.get(id) != null)
                {
                    try
                    {
                        C instance = idToCLassMap.get(id).getConstructor(ItemStack.class).newInstance(stack);
                        instance.load(stack.getTagCompound());
                        return instance;
                    }
                    catch (InstantiationException e)
                    {
                        Engine.logger().error("ModuleBuilder failed to create module from class " + idToCLassMap.get(id));
                        if (Engine.runningAsDev)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch (IllegalAccessException e)
                    {
                        Engine.logger().error("ModuleBuilder was prevented access to class " + idToCLassMap.get(id));
                        if (Engine.runningAsDev)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch (NoSuchMethodException e)
                    {
                        Engine.logger().error("ModuleBuilder failed to find  constructor(ItemStack.class) for class " + idToCLassMap.get(id));
                        if (Engine.runningAsDev)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch (InvocationTargetException e)
                    {
                        Engine.logger().error("ModuleBuilder failed to invoke constructor(ItemStack.class) for class " + idToCLassMap.get(id));
                        if (Engine.runningAsDev)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Engine.logger().error("ModuleBuilder, module " + id + " has no class registered.");
                }
            }
        }
        else if (stack != null)
        {
            if (Engine.runningAsDev)
            {
                Engine.logger().error("ModuleBuilder failed to create module due to NBT data being " + (stack.getTagCompound() == null ? "null" : "invalid ") + " for item stack " + stack);
            }
        }
        else
        {
            if (Engine.runningAsDev)
            {
                Engine.logger().error("ModuleBuilder failed to create module due to stack being null");
            }
        }
        return null;
    }
}
