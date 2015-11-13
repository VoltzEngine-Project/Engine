package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.event.WorldChangeActionEvent;
import com.builtbroken.mc.lib.transform.vector.Location;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by robert on 12/2/2014.
 */
public class WorldChangeHelper
{
    /** Called to do a change action in the world
     *
     * @param action - instance of the change
     * @param triggerCause - cause of the trigger
     * @return if the result completed, was blocked, or failed
     */
    public static ChangeResult doAction(World world, double x, double y, double z, IWorldChangeAction action, TriggerCause triggerCause)
    {
        return doAction(new Location(world, x, y, z), action, triggerCause);
    }

    /** Called to do a change action in the world
     *
     * @param loc - location in the world
     * @param action - instance of the change
     * @param triggerCause - cause of the trigger
     * @return if the result completed, was blocked, or failed
     */
    public static ChangeResult doAction(Location loc, IWorldChangeAction action, TriggerCause triggerCause)
    {
        if (action != null)
        {
            Event event = new WorldChangeActionEvent.ActionCreated(loc, action, triggerCause);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled())
            {
                action.doEffectOther(true);
                if (action.shouldThreadAction() > 0)
                {
                    ThreadWorldChangeAction thread = new ThreadWorldChangeAction(loc, action, triggerCause);
                    thread.start();
                }
                else
                {
                    Collection<IWorldEdit> effectedBlocks = getEffectedBlocks(loc, triggerCause, action);
                    if (effectedBlocks != null && !effectedBlocks.isEmpty()) ;
                    {
                        for (IWorldEdit v : effectedBlocks)
                        {
                            action.handleBlockPlacement(v);
                        }
                    }
                }
                action.doEffectOther(false);
                return ChangeResult.COMPLETED;
            }
            return ChangeResult.BLOCKED;
        }
        return ChangeResult.FAILED;
    }

    /** Gets a list of blocks that change will effect
     *
     * @param vec - location in the world
     * @param triggerCause - cause
     * @param blast - action instance
     * @return list of block locations changes
     */
    public static Collection<IWorldEdit> getEffectedBlocks(Location vec, TriggerCause triggerCause, IWorldChangeAction blast)
    {
        Collection<IWorldEdit> effectedBlocks = blast.getEffectedBlocks();
        //Triggers an event allowing other mods to edit the block list
        MinecraftForge.EVENT_BUS.post(new WorldChangeActionEvent.FinishedCalculatingEffectEvent(vec, effectedBlocks, blast, triggerCause));

        //If we have blocks to edit then register with the event handler
        if (effectedBlocks == null)
        {
            return new ArrayList();
        }
        return effectedBlocks;
    }

    public enum ChangeResult
    {
        COMPLETED,
        FAILED,
        BLOCKED
    }
}
