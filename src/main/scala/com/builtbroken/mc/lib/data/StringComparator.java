package com.builtbroken.mc.lib.data;

import java.util.Comparator;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class StringComparator implements Comparator<String>
{
    @Override
    public int compare(String o1, String o2)
    {
        if (o1.equalsIgnoreCase(o2))
        {
            return 0;
        }
        else if (o1 == null)
        {
            return -1;
        }
        else if (o2 == null)
        {
            return 1;
        }

        int l = o1.length() < o2.length() ? o1.length() : o2.length();
        for (int i = 0; i < l; i++)
        {
            char c = o1.charAt(i);
            char c2 = o2.charAt(i);
            int result = Character.compare(c, c2);
            if (result != 0)
            {
                return result;
            }
        }
        return Integer.compare(o1.length(), o2.length());
    }
}
