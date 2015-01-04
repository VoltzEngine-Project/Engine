package com.builtbroken.lib.transform.region;

/** Used since scala doesn't have its own easy version of isAssignableFrom
 * Created by robert on 12/20/2014.
 */
public class ClassIsAssignableFrom
{
    public static boolean isAssignableFrom(Class a, Class b)
    {
        return a.isAssignableFrom(b);
    }
}
