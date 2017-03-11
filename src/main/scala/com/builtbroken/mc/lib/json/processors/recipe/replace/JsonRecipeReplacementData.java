package com.builtbroken.mc.lib.json.processors.recipe.replace;

import com.builtbroken.mc.core.registry.implement.ILoadComplete;
import com.builtbroken.mc.lib.json.processors.JsonGenData;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/11/2017.
 */
public class JsonRecipeReplacementData extends JsonGenData implements ILoadComplete
{
    boolean remove_all;

    @Override
    public void lonLoadCompleted()
    {

    }
}
