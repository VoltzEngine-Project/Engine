package com.builtbroken.mc.lib.helper;

import com.builtbroken.mc.core.References;
import cpw.mods.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author DarkGuardsman
 */
public class ReflectionUtility extends ReflectionHelper
{
    /**
     * Looks for a constructor matching the argument given.
     *
     * @param clazz - class to look for the constructor in
     * @param args  - arguments that the constructor should have
     * @return first match found
     */
    public static <e extends Object> Constructor<e> getConstructorWithArgs(Class<e> clazz, Object... args)
    {
        if (clazz != null)
        {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors)
            {
                if (constructor.getParameterTypes().length == args.length)
                {
                    Class<?>[] pType = constructor.getParameterTypes();
                    for (int i = 0; i < pType.length; i++)
                    {
                        if (!pType[i].equals(args[i].getClass()))
                        {
                            continue;
                        }
                        if (i == pType.length - 1)
                        {
                            try
                            {
                                Constructor<e> con = (Constructor<e>) constructor;
                                return con;
                            } catch (ClassCastException e)
                            {

                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Sets a field inside of Minecraft's code that is normally srg or obfuscated
     *
     * @param clazz     - class to look for the field in
     * @param instance  - - instance of the class, null will assume static field
     * @param fieldName - name of the field as seen in readable version of the code
     * @param newValue  - value to set the field too
     * @throws NoSuchFieldException   - field is not found, can easily happen if you provide the wrong field name or the name changed
     * @throws IllegalAccessException - should never happen as long as Forge/Minecraft don't implement a security manager.
     */
    public static void setMCFieldWithCatch(Class clazz, Object instance, String fieldName, String fieldName2, Object newValue)
    {
        try
        {
            setMCField(clazz, instance, fieldName, fieldName2, newValue);
        } catch (NoSuchFieldException e)
        {
            References.LOGGER.catching(Level.ERROR, e);
        } catch (IllegalAccessException e)
        {
            References.LOGGER.catching(Level.ERROR, e);
        }
    }

    /**
     * Sets a field inside of Minecraft's code that is normally srg or obfuscated
     *
     * @param clazz     - class to look for the field in
     * @param instance  - - instance of the class, null will assume static field
     * @param fieldName - name of the field as seen in readable version of the code
     * @param newValue  - value to set the field too
     * @throws NoSuchFieldException   - field is not found, can easily happen if you provide the wrong field name or the name changed
     * @throws IllegalAccessException - should never happen as long as Forge/Minecraft don't implement a security manager.
     */
    public static void setMCField(Class clazz, Object instance, String fieldName, String fieldName2, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        int m = -1;

        Field field = getMCField(clazz, fieldName, fieldName2);

        //Set the field to public
        field.setAccessible(true);

        //Removed final modifier
        if (Modifier.isFinal(field.getModifiers()))
            m = removeFinalFromField(field);

        //Sets field value
        field.set(instance, newValue);

        //Restores final modifier
        if (m != -1)
            setModifiers(field, m);
    }

    @Deprecated
    public static void setMCField(Class clazz, Object instance, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        setMCField(clazz, instance, fieldName, fieldName, newValue);
    }

    public static void printFields(Class clazz)
    {
        System.out.println("==== Outputting Fields Names ====");
        System.out.println("\tClass: " + clazz);
        for (String name : getFieldNames(clazz))
        {
            System.out.println("\t" + name);
        }
        System.out.println("==== Done Listing Field Names ====");
    }

    public static List<String> getFieldNames(Class clazz)
    {
        List<String> fieldNames = new ArrayList<String>();
        for (Field f : getAllFields(clazz))
        {
            String name = f.getName();
            if (!fieldNames.contains(name))
                fieldNames.add(name);
        }
        return fieldNames;
    }

    public static List<Field> getAllFields(Class clazz)
    {
        List<Field> fields = getFields(clazz);
        fields.addAll(getDeclaredFields(clazz));
        return fields;
    }

    public static List<Field> getFields(Class clazz)
    {
        List<Field> fields = new ArrayList();
        fields.addAll(Arrays.asList(clazz.getFields()));
        return fields;
    }

    public static List<Field> getDeclaredFields(Class clazz)
    {
        List<Field> fields = new ArrayList();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    public static Method getMethod(Class clazz, String name, Class<?>... args)
    {
        Method method = null;
        try
        {
            method = clazz.getMethod(name, args);
        } catch (NoSuchMethodException e)
        {
            try
            {
                method = clazz.getDeclaredMethod(name, args);
            } catch (NoSuchMethodException e2)
            {

            }
        }
        return method;
    }

    public static List<Method> getAllMethods(Class clazz) throws ClassNotFoundException
    {
        List<Method> fields = getMethods(clazz);
        fields.addAll(getDeclaredMethods(clazz));
        return fields;
    }

    public static List<Method> getAllMethods(Class clazz, Class<? extends Annotation>... annotations) throws ClassNotFoundException
    {
        List<Method> fields = getAllMethods(clazz);
        List<Method> returns = new ArrayList();
        Iterator<Method> it = fields.iterator();
        while (it.hasNext())
        {
            Method m = it.next();
            for (Class<? extends Annotation> an : annotations)
            {
                if (m.isAnnotationPresent(an))
                {
                    returns.add(m);
                }
            }
        }
        return fields;
    }

    public static List<Method> getMethods(Class clazz) throws ClassNotFoundException
    {
        List<Method> fields = new ArrayList();
        fields.addAll(Arrays.asList(clazz.getMethods()));
        return fields;
    }

    public static List<Method> getDeclaredMethods(Class clazz) throws ClassNotFoundException
    {
        List<Method> fields = new ArrayList();
        fields.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        return fields;
    }

    public static Field getMCField(Class clazz, String fieldName, String fieldName2)
    {
        Field f = null;
        for (int i = 0; i < 4; i++)
        {
            try
            {
                if (i == 0 || i == 2)
                    f = clazz.getField(i == 0 ? fieldName : fieldName2);
                else
                    f = clazz.getDeclaredField(i == 1 ? fieldName : fieldName2);
            } catch (NoSuchFieldException e)
            {

            }
        }
        if (f != null)
            f.setAccessible(true);
        return f;
    }

    /**
     * Sets a final field, will remove final modified, and will make the field public for access.
     *
     * @param fieldname - name of the field to set
     * @param newValue  - value to set the field too
     * @throws NullPointerException   - if you failed to provide a field
     * @throws NoSuchFieldException   - if the field was not found, which should never happen
     * @throws IllegalAccessException - if a security manager prevents access to the class, there most likely
     *                                shouldn't be one over MC classes. However, with how lex is blocking modder access to things this is very
     *                                likely to change. If this happens use ASM or an access transformer.
     */
    public static void setFinalStaticMCField(Class clazz, String fieldname, String fieldName2, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        setFinalStaticField(getMCField(clazz, fieldname, fieldName2), newValue);
    }

    @Deprecated
    public static void setFinalStaticMCField(Class clazz, String fieldname, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        setFinalStaticField(getMCField(clazz, fieldname, fieldname), newValue);
    }
    /**
     * Sets a final field, will remove final modified, and will make the field public for access.
     *
     * @param field    - field to set, uses reflection to mess with the field object
     * @param newValue - value to set the field too
     * @throws NullPointerException   - if you failed to provide a field
     * @throws NoSuchFieldException   - if the field was not found, which should never happen
     * @throws IllegalAccessException - if a security manager prevents access to the class, there most likely
     *                                shouldn't be one over MC classes. However, with how lex is blocking modder access to things this is very
     *                                likely to change. If this happens use ASM or an access transformer.
     */
    public static void setFinalStaticField(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        setFinalField(null, field, newValue);
    }

    /**
     * Sets a final field, will remove final modified, and will make the field public for access.
     *
     * @param instance - instance of the class, null will assume static field
     * @param field    - field to set, uses reflection to mess with the field object
     * @param newValue - value to set the field too
     * @throws NullPointerException   - if you failed to provide a field
     * @throws NoSuchFieldException   - if the field was not found, which should never happen
     * @throws IllegalAccessException - if a security manager prevents access to the class, there most likely
     *                                shouldn't be one over MC classes. However, with how lex is blocking modder access to things this is very
     *                                likely to change. If this happens use ASM or an access transformer.
     */
    public static void setFinalField(Object instance, Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        //TODO restore private and final modifiers if they existed
        field.setAccessible(true);


        //Sets the field value
        field.set(instance, newValue);
    }

    /**
     * Removes final off of the field so it can be set
     *
     * @param field - field to remove final off of
     * @return the previous modifier state so it can be restored
     * @throws NoSuchFieldException   - field not found, should never happen
     * @throws IllegalAccessException - field can be manipulated
     */
    public static int removeFinalFromField(Field field) throws NoSuchFieldException, IllegalAccessException
    {
        return setModifiers(field, field.getModifiers() & ~Modifier.FINAL);
    }

    /**
     * Sets a fields modifiers, mainly used for removing final modifiers and then restoring it after settings the field
     *
     * @param field    - field to mess with
     * @param modifier - modifers to set
     * @return modifiers before they were changed
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static int setModifiers(Field field, int modifier) throws NoSuchFieldException, IllegalAccessException
    {
        int m = field.getModifiers();
        //Gets the modifier field from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        //set the modifier field to public so it can be accessed
        modifiersField.setAccessible(true);
        //Sets the new modifier
        modifiersField.setInt(field, modifier);
        return m;
    }
}
