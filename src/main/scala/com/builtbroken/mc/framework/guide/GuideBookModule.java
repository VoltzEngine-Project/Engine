package com.builtbroken.mc.framework.guide;

import com.builtbroken.mc.framework.guide.parts.Chapter;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.processors.guide.JsonProcessorChapter;
import com.builtbroken.mc.framework.mod.loadable.ILoadableProxy;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class GuideBookModule implements ILoadableProxy
{
    public static final GuideBookModule INSTANCE = new GuideBookModule();

    public HashMap<String, Chapter> id_to_chapter = new HashMap();

    @Override
    public void loadJsonContentHandlers()
    {
        JsonContentLoader.INSTANCE.add(new JsonProcessorChapter());
    }

    @Override
    public boolean shouldLoad()
    {
        return true; //TODO implement config to disable guide book
    }
}
