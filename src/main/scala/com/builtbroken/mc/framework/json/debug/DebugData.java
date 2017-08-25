package com.builtbroken.mc.framework.json.debug;

/**
 * Used to store debug data for GUI display
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/24/2017.
 */
public class DebugData
{
    /** Main display message */
    public String msg;
    /** Additional details */
    public String[] lines;

    @Deprecated
    public DebugData()
    {
    }

    public DebugData(String msg)
    {
        this.msg = msg;
    }
}
