package calclavia.lib.utility.nbt;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLLog;

/** Simple manager that handles common saving and creation of object threw Minecraft's NBT system.
 * 
 * @author Darkguardsman */
public class SaveManager
{
    /** Map of save names with there class file */
    private static HashMap<String, Class<?>> idToClassMap = new HashMap<String, Class<?>>();

    /** Reverse of the idToClassMap */
    private static HashMap<Class<?>, String> classToIDMap = new HashMap<Class<?>, String>();

    /** List of object to save on the next save call */
    private static List<WeakReference<Object>> saveList = new ArrayList<WeakReference<Object>>();

    /** Object that save each time the world saves */
    private static List<WeakReference<Object>> objects = new ArrayList<WeakReference<Object>>();

    /** Instance of this class */
    private static SaveManager instance;

    /** Last cpu time that the save manager tried to save a file */
    private static long lastSaveMills = 0;

    /** Gets an instance of this class */
    public static SaveManager instance()
    {
        if (instance == null)
        {
            instance = new SaveManager();
        }
        return instance;
    }

    /** Called when the object wants to be save only on the next save call. Will be removed from the
     * save manager after */
    public static void markNeedsSaved(Object object)
    {
        synchronized (objects)
        {
            if (object instanceof IVirtualObject && !saveList.contains(object))
            {
                saveList.add(new WeakReference<Object>(object));
            }
        }
    }

    /** Registers the object to be saved on each world save event */
    public static void register(Object object)
    {
        synchronized (objects)
        {
            if (object instanceof IVirtualObject && !objects.contains(object))
            {
                objects.add(new WeakReference<Object>(object));
            }
        }
    }

    /** Call this to register a class with an id to be use in recreating an object from a save. Any
     * object that is registered to this should use a no parm constructor. Unless the class plans to
     * construct itself without using the save manager.
     * 
     * @param id - string that will be used to save the class by
     * @param clazz - class to link with the id */
    public static void registerClass(String id, Class<?> clazz)
    {
        synchronized (classToIDMap)
        {
            synchronized (idToClassMap)
            {
                if (id != null && clazz != null)
                {
                    if (idToClassMap.containsKey(id) && idToClassMap.get(id) != null)
                    {
                        System.out.println("[CoreMachine]SaveManager: Something attempted to register a class with the id of another class");
                        System.out.println("[CoreMachine]SaveManager: Id:" + id + "  Class:" + clazz.getName());
                        System.out.println("[CoreMachine]SaveManager: OtherClass:" + idToClassMap.get(id).getName());
                    }
                    else
                    {
                        idToClassMap.put(id, clazz);
                        classToIDMap.put(clazz, id);
                    }
                }
            }
        }
    }

    /** Creates then loads an object from a file. The file should have an nbt structure or it may not
     * work.
     * 
     * @param file - file
     * @return the object created from the file */
    public static Object createAndLoad(File file, Object... args)
    {
        if (file.exists())
        {
            Object obj = createAndLoad(NBTUtility.loadData(file), args);
            if (obj instanceof IVirtualObject)
            {
                ((IVirtualObject) obj).setSaveFile(file);
            }
            return obj;
        }
        return null;
    }

    public static Object createAndLoad(NBTTagCompound nbt, Object... args)
    {
        Object obj = null;
        if (nbt != null && nbt.hasKey("id"))
        {
            try
            {
                Class<?> clazz = getClass(nbt.getString("id"));

                if (clazz == null)
                {
                    return null;
                }
                if (args == null || args.length == 0)
                {
                    Constructor<?>[] constructors = clazz.getConstructors();
                    Constructor<?> con = null;
                    loop:
                    for (Constructor<?> constructor : constructors)
                    {
                        if (constructor.getParameterTypes().length == args.length)
                        {
                            Class<?>[] pType = constructor.getParameterTypes();
                            for (int i = 0; i < pType.length; i++)
                            {
                                if (!pType[i].equals(args[i].getClass()))
                                {
                                    continue;
                                }
                                if (i == pType.length - 1)
                                {
                                    con = constructor;
                                    break loop;
                                }
                            }
                        }
                    }
                    if (con != null)
                    {
                        obj = con.newInstance(args);
                    }
                }
                else
                {
                    obj = clazz.newInstance();
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            if (obj instanceof ISaveObj)
            {
                try
                {
                    ((ISaveObj) obj).load(nbt);
                }
                catch (Exception e)
                {
                    FMLLog.log(Level.SEVERE, e, "[CalclaviaCore]SaveManager: An object %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author", nbt.getString("id"), obj.getClass().getName());
                    obj = null;
                }
            }
            else
            {
                MinecraftServer.getServer().getLogAgent().logWarning("[CalclaviaCore]SaveManager: Skipping object with id " + nbt.getString("id"));
            }

            return obj;
        }
        return null;
    }

    @ForgeSubscribe
    public void worldSave(WorldEvent evt)
    {
        //current time milli-seconds is used to prevent the files from saving 20 times when the world loads
        if (System.currentTimeMillis() - lastSaveMills > 2000)
        {
            lastSaveMills = System.currentTimeMillis();
            saveAll();
        }
    }

    /** Called to save all object currently set to save next call */
    public static void saveAll()
    {
        List<WeakReference<Object>> objs = new ArrayList<WeakReference<Object>>();
        objs.addAll(SaveManager.objects);
        objs.addAll(SaveManager.saveList);
        for (WeakReference<Object> ref : objs)
        {
            Object object = ref.get();
            if (object instanceof IVirtualObject)
            {
                saveObject((IVirtualObject) object);
            }
        }
        saveList.clear();
    }

    /** Saves an object to its preferred save location. Does check for null, registered save class,
     * and if save file doesn't exist. Redirects to NBTUtility for actual saving of the file itself.
     * 
     * @param object - instance of @IVirtualObject */
    public static void saveObject(IVirtualObject object)
    {
        if (object != null)
        {
            if (getID(object.getClass()) != null)
            {
                if (((IVirtualObject) object).getSaveFile() != null)
                {
                    File file = ((IVirtualObject) object).getSaveFile();
                    file.mkdirs();
                    NBTTagCompound tag = new NBTTagCompound();
                    ((IVirtualObject) object).save(tag);
                    tag.setString("id", getID(object.getClass()));
                    NBTUtility.saveData(file, tag);
                }
                else
                {
                    FMLLog.fine("[Calclavia-Core]SaveManager: Error Save File returned null for " + object.toString());
                    FMLLog.fine("[Calclavia-Core]             Class '" + object.getClass());
                }
            }
            else
            {
                FMLLog.fine("[Calclavia-Core]SaveManager: Unregistered save class '" + object.getClass() + "' attempted to save");
            }
        }
        else
        {
            FMLLog.fine("[Calclavia-Core]SaveManager: Something tried to save null");
        }
    }

    /** Gets the ID that the class will be saved using */
    public static String getID(Class clazz)
    {
        return classToIDMap.get(clazz);
    }

    /** Gets the class that was registered with the ID */
    public static Class getClass(String id)
    {
        return idToClassMap.get(id);
    }
}
