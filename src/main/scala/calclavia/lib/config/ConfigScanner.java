package calclavia.lib.config;

import cpw.mods.fml.common.discovery.ASMDataTable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tgame14
 * @since 11/05/14
 */
public class ConfigScanner
{
	private Set<ASMDataTable.ASMData> configs;
	protected Set<Class> classes;

	private static ConfigScanner instance = new ConfigScanner();

	private ConfigScanner()
	{
		this.configs = new HashSet<ASMDataTable.ASMData>();
		this.classes = new HashSet<Class>();
	}

	public static ConfigScanner instance ()
	{
		return instance;
	}

	public void generateSets(ASMDataTable table)
	{
		configs = table.getAll("calclavia.lib.config.Config");

		for (ASMDataTable.ASMData data : configs)
		{
			try
			{
				classes.add(Class.forName(data.getClassName()));
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
}
