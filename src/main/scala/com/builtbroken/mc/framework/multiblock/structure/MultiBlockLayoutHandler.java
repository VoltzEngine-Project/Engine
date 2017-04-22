package com.builtbroken.mc.framework.multiblock.structure;

import com.builtbroken.jlib.data.vector.IPos3D;

import java.util.HashMap;

/**
 * Cache of structures to be used by multi-block tiles
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/17/2017.
 */
public class MultiBlockLayoutHandler
{
    private static final HashMap<String, MultiBlockLayout> layouts = new HashMap();

    static
    {
        //Empty layout, used as a null or default value
        register(new MultiBlockLayout(null, "1x1"));
    }

    public static HashMap<IPos3D, String> get(String key)
    {
        MultiBlockLayout layout = layouts.get(key.toLowerCase());
        if (layout != null)
        {
            return layout.tiles;
        }
        return null;
    }

    public static void register(MultiBlockLayout multiBlockLayout)
    {
        layouts.put(multiBlockLayout.key, multiBlockLayout);
    }
}
