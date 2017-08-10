package com.builtbroken.mc.framework.json.loading;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to simplify loading of data from JSON files into objects
 * <p>
 * Used over GSON and other systems to give better control on how loading works. Including
 * the ability to target methods.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.METHOD})
public @interface JsonProcessorData
{
    /** List of keys to use */
    String[] value();

    /** Primitive type to load, only use for numbers */
    String type() default "Unknown";

    /**
     * Enforced that a value is not null and contains data
     * <p>
     * Only works on fields at this time.
     *
     * @return true to enforce a not null state
     */
    boolean required() default false;
}
