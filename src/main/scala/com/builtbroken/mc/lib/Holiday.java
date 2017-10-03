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
    private static boolean halloween;


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

            int month = calendar.get(2) + 1;
            int day = calendar.get(5);

            if (month == 12 && day >= 24 && day <= 26)
            {
                XMAS = true;
            }

            if (month == 10 && day > 20)
            {
                halloween = true;
            }
        }
    }

    public static boolean isXMAS()
    {
        init();
        return XMAS;
    }

    public static boolean isHalloween()
    {
        init();
        return halloween;
    }
}
