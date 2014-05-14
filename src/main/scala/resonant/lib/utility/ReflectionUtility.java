package resonant.lib.utility;

import java.lang.reflect.Constructor;

/** @author DarkGuardsman */
public class ReflectionUtility
{
    /** Looks for a constructor matching the argument given.
     * 
     * @param clazz - class to look for the constructor in
     * @param args - arguments that the constructor should have
     * @return first match found */
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
}
