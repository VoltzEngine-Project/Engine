package com.builtbroken.mc.debug.data;

/**
 * Used to store debug data for GUI display
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/24/2017.
 */
public class DebugData implements IJsonDebugData
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

    @Override
    public String buildDebugLineDisplay()
    {
        if (lines != null && lines.length > 0)
        {
            String text = "<html>";
            text += "<h3>" + msg + "</h3>";
            for (String s : lines)
            {
                text += "<p>" + s.replace("\t", "    ") + "</p>";
            }
            text += "</html>";
            return text;
        }

        return msg.replace("\t", "    ");
    }

    @Override
    public void onDoubleClicked()
    {

    }
}
