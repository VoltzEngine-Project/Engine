package com.builtbroken.mc.client.json.render.state;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.debug.IJsonDebugDisplay;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

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

    public RenderState(String id)
    {
        super(null); //TODO
        this.id = id;
    }

    @Override
    public String getContentID()
    {
        return id;
    }

    @Override
    public String getDisplayName()
    {
        return id;
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
