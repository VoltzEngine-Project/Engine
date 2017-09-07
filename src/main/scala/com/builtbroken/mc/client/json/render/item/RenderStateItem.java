package com.builtbroken.mc.client.json.render.item;

import com.builtbroken.mc.client.json.render.state.RenderStateTexture;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/5/2017.
 */
public class RenderStateItem extends RenderStateTexture
{
    public RenderStateItem(String id)
    {
        super(id);
    }

    @Override
    public String toString()
    {
        return "RenderItemState[" + id + ", " + getTextureID() + "]@" + hashCode();
    }
}
