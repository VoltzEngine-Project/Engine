package com.builtbroken.mc.client.json.render.state;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.debug.IJsonDebugDisplay;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

import java.awt.*;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public abstract class RenderState extends JsonGenData implements IRenderState, IJsonDebugDisplay
{
    public final String id;
    /** ID for Super object / parent object of this render state, allows sharing state data */
    public String parent;
    /** Color to apply */
    public String color;
    /** Super object / parent object of this render state, allows sharing state data */
    public IRenderState parentState;

    private int colorCache = -2;

    public RenderState(String id)
    {
        super(null); //TODO
        this.id = id;
    }

    @Override
    public int getColorForTexture(int side)
    {
        if (colorCache != -2)
        {
            return colorCache;
        }
        if (color != null)
        {
            if (color.contains(","))
            {
                try
                {
                    String[] split = color.split(",");
                    if (split.length == 3)
                    {
                        colorCache = new Color(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()), Integer.parseInt(split[2].trim())).getRGB();
                    }
                    else if (split.length == 4)
                    {
                        colorCache = new Color(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()), Integer.parseInt(split[2].trim()), Integer.parseInt(split[3].trim())).getRGB();
                    }
                    else
                    {
                        Engine.logger().error("RenderState#getColorForTexture(" + side + ") Failed to convert [" + color + "] to color. Expected to see number separated list of 3-4 items (red, green, blue, alpha) value 0-255");
                        colorCache = -1;
                    }
                }
                catch (NumberFormatException e)
                {
                    colorCache = -1;
                    Engine.logger().error("RenderState#getColorForTexture(" + side + ") Failed to convert [" + color + "] to color due to invalid number. Expected to see number separated list of 3-4 items (red, green, blue, alpha) value 0-255");
                }
            }
            else if (ClientDataHandler.INSTANCE.canSupportColor(color))
            {
                return ClientDataHandler.INSTANCE.getColorAsInt(color);
            }
            else
            {
                try
                {
                    colorCache = Integer.parseInt(color);
                    if (colorCache < 0)
                    {
                        colorCache = -1;
                    }
                }
                catch (NumberFormatException e)
                {
                    colorCache = -1;
                    Engine.logger().error("RenderState#getColorForTexture(" + side + ") Failed to convert [" + color + "] to color.");
                }
            }
        }
        return -1;
    }

    @Override
    public String getContentID()
    {
        return id; //TODO prefix mod?
    }

    @Override
    public String getDisplayName()
    {
        return id;
    }

    @Override
    public String getUniqueID()
    {
        return id; //todo make sure doesn't contain mod
    }

    @Override
    public void addDebugLines(List<String> lines)
    {
        lines.add("ParentID: " + parent);
        lines.add("Parent: " + parentState);
    }

    @Override
    public IRenderState getParent()
    {
        return parentState;
    }
}
