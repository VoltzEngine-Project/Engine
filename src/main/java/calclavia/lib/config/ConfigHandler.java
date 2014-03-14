package calclavia.lib.config;

import net.minecraftforge.common.Configuration;

import java.lang.reflect.Field;

/**
 * How to use the config Handler
 * 
 * First, mark your field with @Config
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

			if (field.getType().getName().equals(int.class.getName()))
			{
				int value = config.get(cfg.category(), key, (int) field.getInt(null), !cfg.comment().isEmpty() ? cfg.comment() : null).getInt();
				field.setInt(null, value);
			}

			else if (field.getType().getName().equals(double.class.getName()))
			{
				double value = config.get(cfg.category(), key, (double) field.getDouble(null), !cfg.comment().isEmpty() ? cfg.comment() : null).getDouble((double) field.getDouble(null));
				field.setDouble(null, value);
			}

			else if (field.getType().getName().equals(String.class.getName()))
			{
				String value = config.get(cfg.category(), key, (String) field.get(null), !cfg.comment().isEmpty() ? cfg.comment() : null).getString();
				field.set(null, value);
			}

			else if (field.getType().getName().equals(boolean.class.getName()))
			{
				boolean value = config.get(cfg.category(), key, (boolean) field.getBoolean(null), !cfg.comment().isEmpty() ? cfg.comment() : null).getBoolean((boolean) field.getBoolean(null));
				field.setBoolean(null, value);
			}

			// TODO: If the lists of Configuration work as key=val1, val2, val3 -- im not sure they
			// do, then add support for that, as that should be quite simple. no reason it shouldnt
			// work

		}
		catch (Exception e)
		{
			System.out.println("Failed to configure: " + field.getName());
			e.printStackTrace();
		}
	}

}
