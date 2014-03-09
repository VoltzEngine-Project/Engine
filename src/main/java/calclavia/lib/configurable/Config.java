package calclavia.lib.configurable;

import net.minecraftforge.common.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply this annotation to static fields only, otherwise will malfunction and fail.
 * use this to automate the configuration file activity, and as such, no need to "forget" to add a configuration option
 *
 * Keep in mind, This configuration option will work for any type of Access level. this can be applied to private, protected, public fields as far as they are static.
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
    public String category() default Configuration.CATEGORY_GENERAL;

    public String key() default "";

    public String comment() default "";

}
