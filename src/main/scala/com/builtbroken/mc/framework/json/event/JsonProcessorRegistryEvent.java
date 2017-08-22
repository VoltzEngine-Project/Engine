package com.builtbroken.mc.framework.json.event;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a new processor has been registered
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class JsonProcessorRegistryEvent extends Event
{
    public final IJsonProcessor processor;

    public JsonProcessorRegistryEvent(IJsonProcessor processor)
    {
        this.processor = processor;
    }
}
