package calclavia.lib.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class NBTUtility
{
    /** Saves NBT data in the world folder.
     * 
     * @return True on success. */
    public static boolean saveData(File file, NBTTagCompound data)
    {
        try
        {
            File tempFile = new File(file.getParent(), file.getName() + "_tmp.dat");

            CompressedStreamTools.writeCompressed(data, new FileOutputStream(tempFile));

            if (file.exists())
            {
                file.delete();
            }

            tempFile.renameTo(file);

            FMLLog.fine("Saved " + file.getName() + " NBT data file successfully.");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Failed to save " + file.getName() + ".dat!");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveData(File saveDirectory, String filename, NBTTagCompound data)
    {
        return saveData(new File(saveDirectory, filename + ".dat"), data);
    }

    public static boolean saveData(String filename, NBTTagCompound data)
    {
        return saveData(getSaveDirectory(MinecraftServer.getServer().getFolderName()), filename, data);
    }

    public static NBTTagCompound loadData(File file)
    {
        try
        {
            if (file.exists())
            {
                FMLLog.fine("Loaded " + file.getName() + " data.");
                return CompressedStreamTools.readCompressed(new FileInputStream(file));
            }
            else
            {
                FMLLog.fine("Created new " + file.getName() + " data.");
                return new NBTTagCompound();
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to load " + file.getName() + ".dat!");
            e.printStackTrace();
            return null;
        }
    }

    /** Reads NBT data from the world folder.
     * 
     * @return The NBT data */
    public static NBTTagCompound loadData(File saveDirectory, String filename)
    {
        return loadData(new File(saveDirectory, filename + ".dat"));
    }

    public static NBTTagCompound loadData(String filename)
    {
        return loadData(getSaveDirectory(MinecraftServer.getServer().getFolderName()), filename);
    }

    public static File getSaveDirectory(String worldName)
    {
        File parent = getBaseDirectory();

        if (FMLCommonHandler.instance().getSide().isClient())
        {
            parent = new File(getBaseDirectory(), "saves" + File.separator);
        }

        return new File(parent, worldName + File.separator);
    }

    public static File getBaseDirectory()
    {
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            FMLClientHandler.instance().getClient();
            return FMLClientHandler.instance().getClient().mcDataDir;
        }
        else
        {
            return new File(".");
        }
    }

    /** Gets a compound from an itemStack.
     * 
     * @param itemStack
     * @return */
    public static NBTTagCompound getNBTTagCompound(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            if (itemStack.getTagCompound() == null)
            {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            return itemStack.getTagCompound();
        }

        return null;
    }
}
