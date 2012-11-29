package universalelectricity.core;

import net.minecraftforge.common.Configuration;

public class UEConfig
{
	public static int getConfigData(Configuration configuration, String category, String name, int defaultInt)
	{
		configuration.load();
		int returnInt = defaultInt;
		try
		{
			returnInt = Integer.parseInt(configuration.get(category, name, defaultInt).value);
			configuration.save();
		}
		catch (Exception e)
		{
		}
		return returnInt;
	}

	public static int getConfigData(Configuration configuration, String name, int defaultInt)
	{
		return getConfigData(configuration, Configuration.CATEGORY_GENERAL, name, defaultInt);
	}

	public static boolean getConfigData(Configuration configuration, String category, String name, boolean defaultBoolean)
	{
		configuration.load();
		boolean returnBoolean = defaultBoolean;
		try
		{
			returnBoolean = Boolean.parseBoolean(configuration.get(category, name, defaultBoolean).value);
			configuration.save();
		}
		catch (Exception e)
		{
		}
		return returnBoolean;
	}

	public static boolean getConfigData(Configuration configuration, String name, boolean defaultBoolean)
	{
		return getConfigData(configuration, Configuration.CATEGORY_GENERAL, name, defaultBoolean);
	}

	public static int getBlockConfigID(Configuration configuration, String category, String name, int defaultID)
	{
		configuration.load();
		int id = defaultID;

		try
		{
			id = Integer.parseInt(configuration.getBlock(category, name, defaultID).value);

			if (id <= 136) { return defaultID; }
		}
		catch (Exception e)
		{
		}

		configuration.save();
		return id;
	}

	public static int getBlockConfigID(Configuration configuration, String name, int defaultID)
	{
		return getItemConfigID(configuration, Configuration.CATEGORY_BLOCK, name, defaultID);
	}

	public static int getItemConfigID(Configuration configuration, String category, String name, int defaultID)
	{
		configuration.load();
		int id = defaultID;

		try
		{
			id = Integer.parseInt(configuration.getItem(category, name, defaultID).value);

			if (id < 256) { return defaultID; }
		}
		catch (Exception e)
		{
		}

		configuration.save();
		return id;
	}

	public static int getItemConfigID(Configuration configuration, String name, int defaultID)
	{
		return getItemConfigID(configuration, Configuration.CATEGORY_ITEM, name, defaultID);
	}
}
