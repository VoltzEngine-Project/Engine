package com.builtbroken.mc.framework.json.loading;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Gives JSON systems access to the data. Only requires for methods as
 * fields automaticly are consider getters if used with {@link JsonProcessorData}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/13/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface JsonProcessorDataGetter
{
    /** List of keys to use */
    String[] value();

    /** Data type to convert value into JSON */
    String type();
}
