package calclavia.lib.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import calclavia.lib.Calclavia;
import net.minecraftforge.common.Configuration;

/**
 * How to use the config Handler
 * 
 * First, mark your field with @Config, make sure the class is loaded before you call configure()
 * 
 * @Config(key = "a key", category = "a category")
 * public static double aValue = 3.765D;
 * 
 * this is all you need to do for the field itself,
 * 
 * next:
 * Just before you call save for your Configuration file, call this method:
 * 
 * ConfigHandler.configure(Configuration configObject, String namespace);
 * 
 * now do remember, The namespace is your mods namespace. for the ICBM mod it would be "icbm", for
 * Calclavia Core, it would be "calclavia"
 * yet if you want to split config files, you can do that by separating namespaces:
 * for example, ICBM Sentries separate Config file, and ICBM Explosives separate config file
 * 
 * ConfigHandler.configure(icbmSentryConfigObject, "icbm.sentry");
 * ConfigHandler.configure(icbmExplosivesConfigObject, "icbm.explosion");
 * 
 * @since 09/03/14
 * @author tgame14
 */
public abstract class ConfigHandler
{

	public static void configure(Configuration config, String namespace) throws ClassNotFoundException, IllegalAccessException
	{
		for (String classPath : ConfigTransformer.classes)
		{
			if (classPath.startsWith(namespace))
			{
				Class clazz = Class.forName(classPath);

				for (Field field : clazz.getDeclaredFields())
				{
					Config cfg = field.getAnnotation(Config.class);
					if (cfg != null)
					{
						handleField(field, cfg, config);
					}
				}
			}
		}

	}

	private static void handleField(Field field, Config cfg, Configuration config) throws IllegalAccessException
	{
		try
		{
			field.setAccessible(true);
			String key;

			if (cfg.key().isEmpty())
			{
				key = field.getName();
			}

			else
				key = cfg.key();

			String comment = !cfg.comment().isEmpty() ? cfg.comment() : null;

			if (field.getType() == Integer.TYPE)
			{
				int value = config.get(cfg.category(), key, field.getInt(null), comment).getInt(field.getInt(null));
				field.setInt(null, value);
			}
			else if (field.getType() == Double.TYPE)
			{
				double value = config.get(cfg.category(), key, field.getDouble(null), comment).getDouble(field.getDouble(null));
				field.setDouble(null, value);
			}
			else if (field.getType() == String.class)
			{
				String value = config.get(cfg.category(), key, (String) field.get(null), comment).getString();
				field.set(null, value);
			}
			else if (field.getType() == Boolean.TYPE)
			{
				boolean value = config.get(cfg.category(), key, field.getBoolean(null), comment).getBoolean(field.getBoolean(null));
				field.setBoolean(null, value);
			}
			else if (field.getType() == String[].class)
			{
				String[] values = config.get(cfg.category(), key, (String[]) field.get(null), comment).getStringList();
				field.set(null, values);
			}
			else if (field.getType() == int[].class)
			{
				int[] values = config.get(cfg.category(), key, (int[]) field.get(null), comment).getIntList();
				field.set(null, values);
			}
			else if (field.getType() == boolean[].class)
			{
				boolean[] values = config.get(cfg.category(), key, (boolean[]) field.get(null), comment).getBooleanList();
				field.set(null, values);
			}
		}
		catch (Exception e)
		{
			System.out.println("Failed to configure: " + field.getName());
			e.printStackTrace();
		}
	}
}
