package calclavia.lib.configurable;

import net.minecraftforge.common.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Handling Configuration is done here only
 *
 *
 * @since 09/03/14
 * @author tgame14
 */
public abstract class ConfigHandler
{

    public static void configure (Configuration config, String namespace) throws ClassNotFoundException, IllegalAccessException
    {
        for (String classPath : ConfigTransformer.classes)
        {
            classPath = classPath.replaceAll("/", ".");
            Class clazz = Class.forName(classPath);

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
