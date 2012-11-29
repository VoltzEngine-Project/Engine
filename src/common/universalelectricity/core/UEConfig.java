package universalelectricity.core;

import net.minecraftforge.common.Configuration;

public class UEConfig
{
	public static int getConfigData(Configuration configuration, String name, int defaultInt)
	{
		configuration.load();
		int returnInt = defaultInt;
		returnInt = Integer.parseInt(configuration.get(Configuration.CATEGORY_GENERAL, name, defaultInt).value);
		configuration.save();
		return returnInt;
	}

	public static boolean getConfigData(Configuration configuration, String name, boolean defaultBoolean)
	{
		configuration.load();
		boolean returnBoolean = defaultBoolean;
		returnBoolean = Boolean.parseBoolean(configuration.get(Configuration.CATEGORY_GENERAL, name, defaultBoolean).value);
		configuration.save();
		return returnBoolean;
	}

	public static int getBlockConfigID(Configuration configuration, String name, int defaultID)
	{
		configuration.load();
		int id = defaultID;

		id = Integer.parseInt(configuration.getBlock(name, defaultID).value);

		if (id <= 136) { return defaultID; }

		configuration.save();
		return id;
	}

	public static int getItemConfigID(Configuration configuration, String name, int defaultID)
	{
		configuration.load();
		int id = defaultID;

		id = Integer.parseInt(configuration.getItem(Configuration.CATEGORY_ITEM, name, defaultID).value);

		if (id < 256) { return defaultID; }

		configuration.save();
		return id;
	}
}
