package calclavia.lib.configurable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply this annotation to public static fields only, otherwise will malfunction and fail.
 * use this to automate the configuration file activity, and as such, no need to "forget" to add a configuration option
 *
 * ie:
 * @Config(category = "values", key = "RocketCount")
 * public static int ROCKET_COUNT = 3;
 *
 *
 * @since 09/03/14
 * @author tgame14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Config
{
    public String category() default "Config_Features";

    public String key();

}
