package com.builtbroken.mc.framework.trigger;

import com.builtbroken.mc.api.event.TriggerCause;
import net.minecraft.world.World;

/**
 * Action that is activated in reaction to a condition or action being taken
 * <p>
 * Example(1): Entity entering an area {@link WorldTriggerArea} updating a radar's redstone state
 * Example(2): Weapon exploding {@link WorldTriggerEvent}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2017.
 */
public class WorldTrigger
{
    public World world;
    public int x;
    public int y;
    public int z;

    /** Limits to listen for events */
    public int size;

    /** Does the trigger react to everything in the world */
    public boolean global = false;
    /** Is the size box shaped */
    public boolean boxShaped = false;
    /** Is the trigger alive, false will remove it from the world */
    public boolean isAlive = true;

    public long creationTime;
    public long lastActivated;

    /**
     * Called when the trigger is added to the world
     */
    public void onAdded()
    {
        creationTime = System.nanoTime();
    }

    /**
     * Called when the trigger is removed from the world
     */
    public void onRemoved()
    {

    }

    /**
     * Called when the trigger is activated
     *
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @param cause - information about what caused the trigger
     * @return true to remove the trigger from active use
     */
    public boolean trigger(double x, double y, double z, TriggerCause cause)
    {
        boolean kill = doTrigger(x, y, z, cause);
        lastActivated = System.nanoTime();
        return !isAlive || kill;
    }

    /**
     * Called when the trigger is activated
     *
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @param cause - information about what caused the trigger
     * @return true to remove the trigger from active use
     */
    protected boolean doTrigger(double x, double y, double z, TriggerCause cause)
    {
        return false;
    }
}
