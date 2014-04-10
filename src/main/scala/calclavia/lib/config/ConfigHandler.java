package calclavia.lib.config;

import calclavia.lib.Calclavia;
import calclavia.lib.asm.ConfigTransformer;
import net.minecraftforge.common.Configuration;

import java.lang.reflect.Field;

/**
 * How to use the config Handler
 * <p/>
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
 * @author tgame14
 * @since 09/03/14
 */
public final class ConfigHandler
{

	public static void configure(Configuration config, String namespace)
	{
		config.load();
		for (String classPath : ConfigTransformer.classes)
		{
			if (classPath.startsWith(namespace))
			{
				Class clazz = null;
				try
				{
					clazz = Class.forName(classPath);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}

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
		config.save();

	}

	/**
	 * A method used to handle a class that contains @Config fields, should be used in late phase handling (after postinit)
	 * for anything in initalization phase (in or before postinit) should use
	 *
	 * @param clazz - class that is being handled
	 * @param config - the config object to write and read from
	 */
	public static void handleClass(String clazz, Configuration config)
	{
		Class c = null;
		try
		{
			c = Class.forName(clazz);
		}
		catch (ClassNotFoundException e)
		{
			Calclavia.LOGGER.warning("Error in finding class " + clazz);
			e.printStackTrace();
		}
		config.load();
		for (Field field : c.getDeclaredFields())
		{
			Config cfg = field.getAnnotation(Config.class);
			if (cfg != null)
			{
				handleField(field, cfg, config);
			}
		}
		config.save();
	}

	private static void handleField(Field field, Config cfg, Configuration config)
	{
		try
		{
			// Set field and annotation data Handled before handing the write of field to config
			field.setAccessible(true);
			String key;

			if (cfg.key().isEmpty())
			{
				key = field.getName();
			}

			else
			{
				key = cfg.key();
			}

			String comment = !cfg.comment().isEmpty() ? cfg.comment() : null;

			// if field is Array, use Config lists, otherwise use default config read and writes
			if (!field.getType().isArray())
			{
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

				else if (field.getType() == Float.TYPE)
				{
					float value = (float) config.get(cfg.category(), key, field.getFloat(null), comment).getDouble(field.getDouble(null));
					field.setFloat(null, value);
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
			}

			else
			{
				if (field.getType().getComponentType() == String.class)
				{
					String[] values = config.get(cfg.category(), key, (String[]) field.get(null), comment).getStringList();
					field.set(null, values);
				}
				else if (field.getType().getComponentType() == int.class)
				{
					int[] values = config.get(cfg.category(), key, (int[]) field.get(null), comment).getIntList();
					field.set(null, values);
				}
				else if (field.getType().getComponentType() == boolean.class)
				{
					boolean[] values = config.get(cfg.category(), key, (boolean[]) field.get(null), comment).getBooleanList();
					field.set(null, values);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Failed to configure: " + field.getName());
			e.printStackTrace();
		}
	}
}
