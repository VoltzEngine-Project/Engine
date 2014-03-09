package calclavia.lib.configurable;

import net.minecraftforge.common.Configuration;

import java.lang.reflect.Field;

/**
 * Handling the configuration here is done by passing the start namespace, and the Configuration file to write to
 *
 * @since 09/03/14
 * @author tgame14
 */
public abstract class ConfigHandler
{

    public static void configure (Configuration config, String namespace) throws ClassNotFoundException, IllegalAccessException
    {
        System.out.println("Entering configure " + ConfigTransformer.classes);
        for (String classPath : ConfigTransformer.classes)
        {
            String str = classPath.replaceAll("/", ".");
            System.out.println(str);

            Class clazz = Class.forName(str);
            System.out.println("Clazz " + clazz);

            for (Field field : clazz.getFields())
            {
                Config cfg = field.getAnnotation(Config.class);
                if (cfg != null)
                {
                    handleField(field, cfg, config);
                }
            }
        }

    }

    private static void handleField(Field field, Config cfg, Configuration config) throws IllegalAccessException
    {
        if (field.getType().getName().equals(int.class.getName()))
        {
            int value = config.get(cfg.category(), cfg.key(), (int) field.getInt(null)).getInt();
            field.setInt(null, value);
        }

        if (field.getType().getName().equals(double.class.getName()))
        {
            double value = config.get(cfg.category(), cfg.key(), (double) field.getDouble(null)).getDouble((double) field.getDouble(null));
            field.setDouble(null, value);
        }

        if (field.getType().getName().equals(String.class.getName()))
        {
            String value = config.get(cfg.category(), cfg.key(), (String) field.get(null)).getString();
            field.set(null, value);
        }
    }


}
