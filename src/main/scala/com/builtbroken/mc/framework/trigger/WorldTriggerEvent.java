package com.builtbroken.mc.framework.trigger;

/**
 * Simple trigger that reacts to an event fired by another system
 * <p>
 * Example: Armory Weapon exploding, Machine turning off, Sound being produced
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2017.
 */
public class WorldTriggerEvent extends WorldTrigger
{
    /** ID of the event to listen for */
    public final String eventID;

    public WorldTriggerEvent(String eventID)
    {
        this.eventID = eventID;
    }
}
