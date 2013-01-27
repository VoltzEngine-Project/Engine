package universalelectricity.prefab.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector2;
import cpw.mods.fml.common.FMLLog;

/**
 * Allows any mod to integrate and have custom mod permission commands.
 * 
 * @author Calclavia
 * 
 */
public class Permissions
{
	/**
	 * File Structure: Tag: DimData -Tag: DimID -boolean: globalBan -Tag: RegionName -int: X -int: Z
	 * -int: R -int: TYPE
	 */
	public static NBTTagCompound nbtData;

	public static final String FIELD_DIMENSION = "dim";
	public static final String FIELD_GLOBAL_BAN = "globalBan";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_X = "X";
	public static final String FIELD_Z = "Z";
	public static final String FIELD_R = "R";

	public static boolean SHE_DING_BAO_HU;

	public static List<CommandFlag> getFlagsInPosition(World worldObj, Vector2 position)
	{
		if (!worldObj.isRemote)
		{
			try
			{
				NBTTagCompound dimData = nbtData.getCompoundTag(FIELD_DIMENSION + worldObj.provider.dimensionId);

				List<CommandFlag> flags = new ArrayList<CommandFlag>();

				// Regions check
				Iterator i = dimData.getTags().iterator();
				while (i.hasNext())
				{
					try
					{
						NBTTagCompound region = (NBTTagCompound) i.next();

						if (Vector2.distance(position, new Vector2(region.getInteger(FIELD_X), region.getInteger(FIELD_Z))) <= region.getInteger(FIELD_R))
							flags.add(new CommandFlag());
					}
					catch (Exception e)
					{
					}
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	public static String getMinecraftDir()
	{
		return "";
	}

	/**
	 * Saves a mod's permission data.
	 * 
	 * @param data
	 * @param filename
	 * @return
	 */
	public static boolean saveData(NBTTagCompound data, String filename)
	{
		String folder;

		if (MinecraftServer.getServer().isDedicatedServer())
		{
			folder = MinecraftServer.getServer().getFolderName();
		}
		else
		{
			folder = "saves" + File.separator + MinecraftServer.getServer().getFolderName();
		}

		try
		{
			File tempFile;
			File file;

			if (getMinecraftDir() != "")
			{
				tempFile = new File(getMinecraftDir(), folder + File.separator + filename + "_tmp.dat");
				file = new File(getMinecraftDir(), folder + File.separator + filename + ".dat");
			}
			else
			{
				tempFile = new File(folder + File.separator + filename + "_tmp.dat");
				file = new File(folder + File.separator + filename + ".dat");
			}

			CompressedStreamTools.writeCompressed(data, new FileOutputStream(tempFile));

			if (file.exists())
			{
				file.delete();
			}

			tempFile.renameTo(file);

			FMLLog.fine("Saved ICBM data successfully.");
			return true;
		}
		catch (Exception e)
		{
			FMLLog.severe("Failed to save " + filename + ".dat!");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Loads a mod's permission data.
	 * 
	 * @param filename
	 * @return
	 */
	public static NBTTagCompound loadData(String filename)
	{
		String folder;

		if (MinecraftServer.getServer().isDedicatedServer())
		{
			folder = MinecraftServer.getServer().getFolderName();
		}
		else
		{
			folder = "saves" + File.separator + MinecraftServer.getServer().getFolderName();
		}

		try
		{
			File file;

			if (getMinecraftDir() != "")
			{
				file = new File(getMinecraftDir(), folder + File.separator + filename + ".dat");
			}
			else
			{
				file = new File(folder + File.separator + filename + ".dat");
			}

			if (file.exists())
			{
				return CompressedStreamTools.readCompressed(new FileInputStream(file));
			}
			else
			{
				return new NBTTagCompound();
			}
		}
		catch (Exception e)
		{
			FMLLog.severe("Failed to load " + filename + ".dat!");
			e.printStackTrace();
			return null;
		}
	}
}
