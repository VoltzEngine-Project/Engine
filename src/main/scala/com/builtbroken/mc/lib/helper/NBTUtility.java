package com.builtbroken.mc.lib.helper;

import com.builtbroken.jlib.data.science.units.UnitHelper;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Point;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;

import java.io.*;
import java.util.UUID;

/**
 * Utility that manages common NBT save and load methods
 *
 * @author Calclavia, DarkGuardsamn
 */
public class NBTUtility
{
    /**
     * Saves NBT data in the world folder.
     *
     * @return True on success.
     */
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

            // Calclavia.LOGGER.fine("Saved " + file.getName() + " NBT data file successfully.");
            return true;
        } catch (Exception e)
        {
            Engine.instance.logger().fatal("Failed to save " + file.getName() + ".dat!");
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
            if(file == null)
            {
                throw new FileNotFoundException("File is null so can not be found");
            }
            if (file.exists())
            {
                return CompressedStreamTools.readCompressed(new FileInputStream(file));
            }
            else
            {
                Engine.instance.logger().info("Save file " + file + "\n\tdoes not exist in the save folder! Generating new NBT data in it's place.");
                return new NBTTagCompound();
            }
        }
        catch (FileNotFoundException e)
        {
            Engine.instance.logger().error("Save Loader Error, File not found " + file, e);
            return new NBTTagCompound();
        }
        catch (IOException e)
        {
            Engine.instance.logger().error("Save Loader Error, Failed to read " + file, e);
            return new NBTTagCompound();
        }
    }

    /**
     * Reads NBT data from the world folder.
     *
     * @return The NBT data
     */
    public static NBTTagCompound loadData(File saveDirectory, String filename)
    {
        return loadData(new File(saveDirectory, filename + ".dat"));
    }

    public static NBTTagCompound loadData(String filename)
    {
        return loadData(getSaveDirectory(MinecraftServer.getServer().getFolderName()), filename);
    }

    public static File getSaveDirectory()
    {
        return getSaveDirectory(MinecraftServer.getServer().getFolderName());
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

    /**
     * Gets a compound from an itemStack.
     *
     * @param itemStack
     * @return
     */
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

    /**
     * Used to save an object without knowing what the object is exactly. Supports most
     * NBTTagCompound save methods including some special cases. Which includes boolean being saves
     * as a string so it can be loaded as a boolean from an object save.
     *
     * @param tag   - NBTTagCompound to save the tag too
     * @param key   - name to save the object as
     * @param value - the actual object
     * @return the tag when done saving too i
     */
    public static NBTTagCompound saveObject(NBTTagCompound tag, String key, Object value)
    {
        if (value instanceof Float)
        {
            tag.setFloat(key, (Float) value);
        }
        else if (value instanceof Double)
        {
            tag.setDouble(key, (Double) value);
        }
        else if (value instanceof Integer)
        {
            tag.setInteger(key, (Integer) value);
        }
        else if (value instanceof String)
        {
            tag.setString(key, (String) value);
        }
        else if (value instanceof Short)
        {
            tag.setShort(key, (Short) value);
        }
        else if (value instanceof Byte)
        {
            tag.setByte(key, (Byte) value);
        }
        else if (value instanceof Long)
        {
            tag.setLong(key, (Long) value);
        }
        else if (value instanceof Boolean)
        {
            tag.setString(key, "NBT:SAVE:BOOLEAN:" + value);
        }
        else if (value instanceof NBTBase)
        {
            tag.setTag(key, (NBTBase) value);
        }
        else if (value instanceof String)
        {
            tag.setString(key, (String) value);
        }
        else if (value instanceof byte[])
        {
            tag.setByteArray(key, (byte[]) value);
        }
        else if (value instanceof int[])
        {
            tag.setIntArray(key, (int[]) value);
        }
        else if (value instanceof NBTTagCompound)
        {
            tag.setTag(key, (NBTTagCompound) value);
        }
        else if (value instanceof Point)
        {
            tag.setString(key, "NBT:SAVE:VECTOR:2:" + ((Point) value).x() + ":" + ((Point) value).y());
        }
        else if (value instanceof Pos)
        {
            tag.setString(key, "NBT:SAVE:VECTOR:3:" + ((Pos) value).x() + ":" + ((Pos) value).y() + ":" + ((Pos) value).z());
        }
        return tag;

    }

    /**
     * @param key
     * @param value
     * @return NBTTagCompound that then can be added to save file
     */
    public static NBTTagCompound saveObject(String key, Object value)
    {
        return saveObject(new NBTTagCompound(), key, value);
    }

    /**
     * Reads an unknown object with a known name from NBT
     *
     * @param tag - tag to read the value from
     * @param key - name of the value
     * @return object or suggestionValue if nothing is found
     */
    public static Object loadObject(NBTTagCompound tag, String key)
    {
        if (tag != null && key != null)
        {
            NBTBase saveTag = tag.getTag(key);
            if (saveTag instanceof NBTTagFloat)
            {
                return tag.getFloat(key);
            }
            else if (saveTag instanceof NBTTagDouble)
            {
                return tag.getDouble(key);
            }
            else if (saveTag instanceof NBTTagInt)
            {
                return tag.getInteger(key);
            }
            else if (saveTag instanceof NBTTagString)
            {
                String str = tag.getString(key);
                if (str.startsWith("NBT:SAVE:"))
                {
                    str.replaceAll("NBT:SAVE:", "");
                    if (str.startsWith("BOOLEAN:"))
                    {
                        str.replaceAll("BOOLEAN:", "");
                        if (str.equalsIgnoreCase("true"))
                        {
                            return true;
                        }
                        if (str.equalsIgnoreCase("false"))
                        {
                            return false;
                        }
                    }
                    if (str.startsWith("VECTOR:"))
                    {
                        str.replaceAll("VECTOR:", "");
                        String[] nums = str.split(":");
                        if (UnitHelper.tryToParseDouble(nums[0]) == 2)
                        {
                            return new Point(UnitHelper.tryToParseDouble(nums[1]), UnitHelper.tryToParseDouble(nums[2]));
                        }
                        if (UnitHelper.tryToParseDouble(nums[0]) == 3)
                        {
                            return new Pos(UnitHelper.tryToParseDouble(nums[1]), UnitHelper.tryToParseDouble(nums[2]), UnitHelper.tryToParseDouble(nums[3]));
                        }
                    }
                    return null;
                }
                return str;
            }
            else if (saveTag instanceof NBTTagShort)
            {
                return tag.getShort(key);
            }
            else if (saveTag instanceof NBTTagByte)
            {
                return tag.getByte(key);
            }
            else if (saveTag instanceof NBTTagLong)
            {
                return tag.getLong(key);
            }
            else if (saveTag instanceof NBTBase)
            {
                return tag.getTag(key);
            }
            else if (saveTag instanceof NBTTagByteArray)
            {
                return tag.getByteArray(key);
            }
            else if (saveTag instanceof NBTTagIntArray)
            {
                return tag.getIntArray(key);
            }
            else if (saveTag instanceof NBTTagCompound)
            {
                return tag.getCompoundTag(key);
            }
        }
        return null;
    }

    public static NBTTagCompound saveProfile(NBTTagCompound nbt, GameProfile profile)
    {
        nbt.setString("UUID", profile.getId().toString());
        nbt.setString("username", profile.getName());
        return nbt;
    }

    public static GameProfile loadProfile(NBTTagCompound nbt)
    {
        if (nbt.getString("UUID").split("-").length == 5)
        {
            GameProfile profile = new GameProfile(UUID.fromString(nbt.getString("UUID")), nbt.getString("username"));

            if (profile.isComplete())
            {
                return profile;
            }
        }

        return null;
    }
}
