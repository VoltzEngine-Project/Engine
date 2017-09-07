package com.builtbroken.mc.framework.json.debug.data;

import com.builtbroken.mc.framework.json.debug.IJsonDebugDisplay;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class DebugJsonData implements IJsonDebugData
{
    public final IJsonDebugDisplay jsonObject;

    public DebugJsonData(IJsonDebugDisplay jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    @Override
    public String buildDebugLineDisplay()
    {
        String msg = jsonObject.getDisplayName();
        if (msg == null)
        {
            msg = jsonObject.toString();
        }

        List<String> lines = new ArrayList();
        jsonObject.addDebugLines(lines);

        if (!lines.isEmpty())
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
        jsonObject.onDoubleClickLine();
    }
}
