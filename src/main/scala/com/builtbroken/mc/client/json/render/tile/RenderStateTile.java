package com.builtbroken.mc.client.json.render.tile;

import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public class RenderStateTile extends ModelState
{
    public RenderStateTile(String ID)
    {
        super(ID);
    }

    public RenderStateTile(String ID, String modelID, Pos offset, Pos scale, EulerAngle rotation)
    {
        super(ID, modelID, offset, scale, rotation);
    }

    @Override
    public String toString()
    {
        return "RenderStateTile[" + id + "]@" + hashCode();
    }
}
