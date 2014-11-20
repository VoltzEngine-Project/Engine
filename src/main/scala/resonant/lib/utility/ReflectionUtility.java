package resonant.lib.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author DarkGuardsman
 */
public class ReflectionUtility
{
	/**
	 * Looks for a constructor matching the argument given.
	 *
	 * @param clazz - class to look for the constructor in
	 * @param args  - arguments that the constructor should have
	 * @return first match found
	 */
	public static Constructor<?> getConstructorWithArgs(Class<?> clazz, Object... args)
	{
		Constructor<?> con = null;
		if (clazz != null)
		{
			Constructor<?>[] constructors = clazz.getConstructors();
			loop:
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
							con = constructor;
							break loop;
						}
					}
				}
			}
		}
		return con;
	}

    /** Sets a final field, will remove final modified, and will make the field public for access.
     *
     * @param field - field to set, uses reflection to mess with the field object
     * @param newValue - value to set the field too
     * @throws NullPointerException - if you failed to provide a field
     * @throws NoSuchFieldException - if the field was not found, which should never happen
     * @throws IllegalAccessException - if a security manager prevents access to the class, there most likely
     * shouldn't be one over MC classes. However, with how lex is blocking modder access to things this is very
     * likely to change. If this happens use ASM or an access transformer.
     */
    public static void setFinalStaticField(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
       setFinalField(null, field, newValue);
    }

    /** Sets a final field, will remove final modified, and will make the field public for access.
     *
     * @param instance - instance of the class, null will assume static field
     * @param field - field to set, uses reflection to mess with the field object
     * @param newValue - value to set the field too
     * @throws NullPointerException - if you failed to provide a field
     * @throws NoSuchFieldException - if the field was not found, which should never happen
     * @throws IllegalAccessException - if a security manager prevents access to the class, there most likely
     * shouldn't be one over MC classes. However, with how lex is blocking modder access to things this is very
     * likely to change. If this happens use ASM or an access transformer.
     */
    public static void setFinalField(Object instance, Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        //TODO restore private and final modifiers if they existed
        field.setAccessible(true);

        //Gets the modifier field from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        //set the modifier field to public so it can be accessed
        modifiersField.setAccessible(true);
        //Removes final modifier from the field
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        //Sets the field value
        field.set(instance, newValue);
    }
}
