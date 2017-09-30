package com.builtbroken.mc.framework.thread.delay;

import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/28/2017.
 */
public abstract class DelayedAction
{
    public final World world;

    public int ticksToWait = 0;

    public boolean isAlive = true;

    public DelayedAction(World world, int ticks)
    {
        this.world = world;
        this.ticksToWait = ticks;
    }

    /**
     * Called to kill the action before it is
     * finished.
     */
    public void kill()
    {
        isAlive = false;
    }

    /**
     * Can the action be cleared before it is run.
     * <p>
     * Best to return true for non-import tasks that
     * could be cleared to save CPU with no issues.
     * Things like audio, graphics, damage, etc
     *
     * @return true if yes
     */
    public boolean canClear()
    {
        return false;
    }

    /**
     * Called to queue the action for ticking
     */
    public void start()
    {
        DelayedActionHandler.add(this);
    }

    /**
     * Called to trigger the action
     *
     * @return true if done
     */
    public abstract boolean trigger();
}
