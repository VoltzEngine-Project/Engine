package com.builtbroken.mc.lib;

import java.util.Calendar;

/**
 * Handles checks if its a holiday
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public final class Holiday //TODO move to core eventually
{
    private static boolean XMAS;
    private static boolean init = false;

    private Holiday()
    {
    }

    public static void init()
    {
        if (!init)
        {
            init = true;
            Calendar calendar = Calendar.getInstance();

            if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26)
            {
                XMAS = true;
            }
        }
    }

    public static boolean isXMAS()
    {
        init();
        return XMAS;
    }
}
