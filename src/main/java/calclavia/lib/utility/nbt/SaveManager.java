package calclavia.lib.utility.nbt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

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
    private static List<Object> saveList = new ArrayList<Object>();

    /** Object that save each time the world saves */
    private static List<Object> objects = new ArrayList<Object>();

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
                saveList.add(object);
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
                objects.add(object);
            }
        }
    }

    /** Call this to register a class with an id to be use in recreating an object from a save. Any
     * object that is registered to this should use a no parm constructor. Unless the class plans to
     * construct itself without using the save manager.
     * 
     * @param id - string that will be used to save the class by
     * @param clazz - class to link with the id */
    public static void registerClass(String id, Class clazz)
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
    public static Object createAndLoad(File file)
    {
        if (file.exists())
        {
            Object obj = createAndLoad(NBTUtility.loadData(file));
            if (obj instanceof IVirtualObject)
            {
                ((IVirtualObject) obj).setSaveFile(file);
            }
            return obj;
        }
        return null;
    }

    /** Creates an object from the save using its id */
    public static Object createAndLoad(NBTTagCompound par0NBTTagCompound)
    {
        Object obj = null;
        if (par0NBTTagCompound != null && par0NBTTagCompound.hasKey("id"))
        {
            try
            {
                Class clazz = getClass(par0NBTTagCompound.getString("id"));

                if (clazz != null)
                {
                    obj = clazz.newInstance();
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            if (obj instanceof IVirtualObject)
            {
                try
                {
                    ((IVirtualObject) obj).load(par0NBTTagCompound);
                }
                catch (Exception e)
                {
                    FMLLog.log(Level.SEVERE, e, "An object %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author", par0NBTTagCompound.getString("id"), obj.getClass().getName());
                    obj = null;
                }
            }
            else
            {
                MinecraftServer.getServer().getLogAgent().logWarning("Skipping object with id " + par0NBTTagCompound.getString("id"));
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
            this.saveAll();
        }
    }

    @ForgeSubscribe
    public void onServerStopping(FMLServerStoppingEvent evt)
    {
        this.saveAll();
    }

    public void saveAll()
    {
        List<Object> objs = new ArrayList<Object>();
        objs.addAll(SaveManager.objects);
        objs.addAll(SaveManager.saveList);
        for (Object object : objs)
        {
            if (object instanceof IVirtualObject)
            {
                saveObject(object);
            }
        }
        saveList.clear();
    }

    /** Saves an object along with its ID */
    public static void saveObject(Object object)
    {
        if (object instanceof IVirtualObject && getID(object.getClass()) != null && ((IVirtualObject) object).getSaveFile() != null)
        {
            File file = ((IVirtualObject) object).getSaveFile();
            file.mkdirs();
            NBTTagCompound tag = new NBTTagCompound();
            ((IVirtualObject) object).save(tag);
            tag.setString("id", getID(object.getClass()));
            NBTUtility.saveData(file, tag);
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
