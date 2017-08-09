package com.builtbroken.mc.framework.multiblock.structure;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/17/2017.
 */
public class MultiBlockLayout extends JsonGenData
{
    public final String key;
    public final HashMap<IPos3D, String> tiles = new HashMap();

    public MultiBlockLayout(IJsonProcessor processor, String key)
    {
        super(processor);
        this.key = key;
    }

    public void addTile(IPos3D pos, String data)
    {
        tiles.put(pos, data);
    }

    @Override
    public void register()
    {
        MultiBlockLayoutHandler.register(this);
    }

    @Override
    public String getContentID()
    {
        return key;
    }
}
