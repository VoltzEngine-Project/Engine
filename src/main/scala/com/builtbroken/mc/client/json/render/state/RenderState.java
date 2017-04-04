package com.builtbroken.mc.client.json.render.state;

import com.builtbroken.mc.client.json.imp.IRenderState;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public abstract class RenderState implements IRenderState
{
    public final String id;
    public RenderState(String id)
    {
        this.id = id;
    }
}
