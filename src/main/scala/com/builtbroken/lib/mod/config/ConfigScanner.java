package com.builtbroken.lib.mod.config;

import cpw.mods.fml.common.discovery.ASMDataTable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author tgame14
 * @since 11/05/14
 */
public class ConfigScanner
{
	private static ConfigScanner instance = new ConfigScanner();
	protected Set<Class> classes;
	private Set<ASMDataTable.ASMData> configs;

	private ConfigScanner()
	{
		this.configs = new LinkedHashSet<ASMDataTable.ASMData>();
		this.classes = new LinkedHashSet<Class>();
	}

	public static ConfigScanner instance()
	{
		return instance;
	}

	public void generateSets(ASMDataTable table)
	{
		configs = table.getAll(Config.class.getName());

		for (ASMDataTable.ASMData data : configs)
		{
			try
			{
				addClass(Class.forName(data.getClassName()));
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * TODO: Temporary patch, ASM Table is not being read properly.
	 */
	public void addClass(Class clazz)
	{
		classes.add(clazz);
	}
}
