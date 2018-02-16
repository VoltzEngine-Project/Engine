package com.builtbroken.mc.framework.guide.parts;

import com.builtbroken.mc.framework.guide.parts.imp.GuidePartContainer;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class Book extends GuidePartContainer<Chapter>
{
    public Book(IJsonProcessor processor)
    {
        super(processor);
    }
}
