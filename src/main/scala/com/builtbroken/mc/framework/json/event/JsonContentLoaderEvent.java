package com.builtbroken.mc.framework.json.event;

import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.JsonLoadPhase;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class JsonContentLoaderEvent extends Event
{
    public final JsonContentLoader contentLoader;
    public final JsonLoadPhase phase;

    public JsonContentLoaderEvent(JsonContentLoader contentLoader, JsonLoadPhase phase)
    {
        this.contentLoader = contentLoader;
        this.phase = phase;
    }
}
