package com.builtbroken.mc.framework.json.loading;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to give the JSON system access to a field or method for setting values. In the case of a field
 * it also grants access to collect data as well.
 * <p>
 * Can be fine tuned to control logic. Including the ability to define several JSON tags to use, data type to inject (int, double, etc),
 * if the value can be changed during runtime, if the value is required for a JSON file, and if the value should load server/client.
 * <p>
 * For quick setup it is recommended to apply to a field. Then set a single name and data type.
 * <p>
 * For more controlled implement a method will work but this approach will require the use of {@link JsonProcessorDataGetter} in order for
 * other systems to function fully.
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

    /** Data type to load data as, optional for boolean and strings */
    String type() default "Unknown";

    /** Arguments to pass into type converter, optional in most cases */
    String[] args() default "";

    /**
     * Allow the runtime to change the value. This means
     * that anything can change the value including
     * commands and configurations. For configuration
     * only use the settings system instead.
     * <p>
     * Designed for use with the {@link com.builtbroken.mc.framework.json.override.JsonOverrideData}
     * <p>
     * Requires the use of {@link JsonProcessorDataGetter} if annotation is used with a method
     *
     * @return true to allow runtime changes
     */
    boolean allowRuntimeChanges() default false;

    /**
     * Enforced that a value is not null and contains data
     * <p>
     * Only works on fields at this time.
     *
     * @return true to enforce a not null state
     */
    boolean required() default false;

    /**
     * Should the field or method be invoked
     * server side.
     * <p>
     * Make sure to use this for client only data. Though
     * it is best to avoid having client data on a server
     * object.
     *
     * @return true if should invoke
     */
    boolean loadForServer() default true;

    /**
     * Should the field or method be invoked
     * client side.
     * <p>
     * Make sure to use this for server only data. Though
     * it is best to avoid having server data on a client only
     * object.
     *
     * @return true if should invoke
     */
    boolean loadForClient() default true;

}
