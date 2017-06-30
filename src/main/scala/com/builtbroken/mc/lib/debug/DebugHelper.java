package com.builtbroken.mc.lib.debug;

import com.builtbroken.mc.core.Engine;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
public class DebugHelper
{
    /**
     * Used to output information about a method for debug purposes
     *
     * @param logger
     * @param method
     * @param message
     * @param data
     */
    public static void outputMethodDebug(Logger logger, String method, String message, Object... data)
    {
        String out = method + "(";
        if (data != null && data.length > 0)
        {
            for (int i = 0; i < data.length; i++)
            {
                if (data[i] instanceof World)
                {
                    if (((World) data[i]).provider != null)
                    {
                        out += "dim@" + ((World) data[i]).provider.dimensionId;
                    }
                    else
                    {
                        out += data[i] + " np";
                    }
                }
                else
                {
                    out += data[i];
                }
                if (i != (data.length - 1))
                {
                    out += ", ";
                }
            }
        }
        out += "); " + message;

        if (logger != null)
        {
            logger.info(out);
        }
        else
        {
            System.out.println("" + out);
            if (Engine.runningAsDev)
            {
                System.err.println("Logger was null");
                RuntimeException exception = new RuntimeException();
                exception.printStackTrace();
            }
        }
    }
}
