package resonant.lib.utility.nbt;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.logging.Level;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import resonant.lib.utility.ReflectionUtility;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

/** Simple manager that handles common saving and creation of object threw Minecraft's NBT system.
 * 
 * @author Darkguardsman */
public class SaveManager
{
    /** Map of save names with there class file */
    private HashMap<String, Class<?>> idToClassMap = new HashMap<String, Class<?>>();

    /** Reverse of the idToClassMap */
    private HashMap<Class<?>, String> classToIDMap = new HashMap<Class<?>, String>();

    /** List of object to save on the next save call */
    private LinkedHashSet<IVirtualObject> saveList = new LinkedHashSet<IVirtualObject>();

    /** Object that save each time the world saves */
    private LinkedHashSet<IVirtualObject> objects = new LinkedHashSet<IVirtualObject>();

    /** Instance of this class */
    private static SaveManager instance;

    /** Last cpu time that the save manager tried to save a file */
    private long lastSaveMills = 0;

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
        synchronized (instance())
        {
            if (object instanceof IVirtualObject && !instance().saveList.contains(object))
            {
                instance().saveList.add((IVirtualObject) object);
            }
        }
    }

    /** Registers the object to be saved on each world save event */
    public static void register(Object object)
    {
        synchronized (instance())
        {
            if (object instanceof IVirtualObject && !instance().objects.contains(object))
            {
                instance().saveList.add((IVirtualObject) object);
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
        synchronized (instance())
        {
            if (id != null && clazz != null)
            {
                if (instance().idToClassMap.containsKey(id) && instance().idToClassMap.get(id) != null)
                {
                    System.out.println("[CoreMachine]SaveManager: Something attempted to register a class with the id of another class");
                    System.out.println("[CoreMachine]SaveManager: Id:" + id + "  Class:" + clazz.getName());
                    System.out.println("[CoreMachine]SaveManager: OtherClass:" + instance().idToClassMap.get(id).getName());
                }
                else
                {
                    instance().idToClassMap.put(id, clazz);
                    instance().classToIDMap.put(clazz, id);
                }
            }
        }
    }

    /** Creates an object from an NBT save file.
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

    /** Loads an object from an NBTTagCompound
     * 
     * @param nbt - NBTTagCompound
     * @param args - argument that will be used to construct the object's class
     * @return new object or null if something went wrong */
    public static Object createAndLoad(NBTTagCompound nbt, Object... args)
    {
        Object obj = null;
        try
        {
            if (nbt != null && nbt.hasKey("id"))
            {
                try
                {
                    Class<?> clazz = getClass(nbt.getString("id"));
                    if (clazz != null)
                    {
                        if (args == null || args.length == 0)
                        {
                            Constructor<?> con = ReflectionUtility.getConstructorWithArgs(clazz, args);
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
        }
        catch (Exception e)
        {
            FMLLog.fine("[Resonant Engine]SaveManager: Error trying to load object from save");
            e.printStackTrace();
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
        for (IVirtualObject ref : instance().objects)
        {
            saveObject(ref);
        }
        for (IVirtualObject ref : instance().saveList)
        {
            saveObject(ref);
        }
        instance().saveList.clear();
    }

    /** Saves an object to its preferred save location. Does check for null, registered save class,
     * and if save file doesn't exist. Redirects to NBTUtility for actual saving of the file itself.
     * 
     * @param object - instance of @IVirtualObject */
    public static void saveObject(IVirtualObject object)
    {
        try
        {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
            {
                if (object != null)
                {
                    if (getID(object.getClass()) != null)
                    {
                        if (object.getSaveFile() != null)
                        {
                            /* Get file, and make directories */
                            File file = object.getSaveFile();
                            file.mkdirs();

                            /* Create nbt save object */
                            NBTTagCompound tag = new NBTTagCompound();
                            object.save(tag);
                            tag.setString("id", getID(object.getClass()));

                            /* Save data using NBTUtility */
                            NBTUtility.saveData(file, tag);
                        }
                        else
                        {
                            throw new NullPointerException("SaveManager: Object save file path is null");
                        }
                    }
                    else
                    {
                        throw new Exception("SaveManager: Object does not have a save ID");
                    }
                }
                else
                {
                    throw new NullPointerException("SaveManager: Attempted to save a null object");
                }
            }
        }
        catch (Exception e)
        {
            FMLLog.fine("[Resonant Engine]SaveManager: Error trying to save object class: " + (object != null ? object.getClass() : "null"));
            e.printStackTrace();
        }
    }

    /** Gets the ID that the class will be saved using */
    public static String getID(Class clazz)
    {
        return instance().classToIDMap.get(clazz);
    }

    /** Gets the class that was registered with the ID */
    public static Class getClass(String id)
    {
        return instance().idToClassMap.get(id);
    }
}
