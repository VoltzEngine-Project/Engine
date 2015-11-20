package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeAudio;
import com.builtbroken.mc.api.edit.IWorldChangeGraphics;
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
 * Helper class for triggering and running world change actions.
 * Created by robert on 12/2/2014.
 */
public final class WorldChangeHelper
{
    /**
     * Called to do a change action in the world
     *
     * @param action       - instance of the change
     * @param triggerCause - cause of the trigger
     * @return if the result completed, was blocked, or failed
     */
    public static ChangeResult doAction(World world, double x, double y, double z, IWorldChangeAction action, TriggerCause triggerCause)
    {
        return doAction(new Location(world, x, y, z), action, triggerCause);
    }

    /**
     * Called to do a change action in the world
     *
     * @param loc          - location in the world
     * @param action       - instance of the change
     * @param triggerCause - cause of the trigger
     * @return if the result completed, was blocked, or failed
     */
    public static ChangeResult doAction(Location loc, IWorldChangeAction action, TriggerCause triggerCause)
    {
        //No action handling, no action created
        if (action != null)
        {
            //Trigger even to allow blocking explosives
            Event event = new WorldChangeActionEvent.ActionCreated(loc, action, triggerCause);
            MinecraftForge.EVENT_BUS.post(event);

            //If even is canceled do nothing
            if (!event.isCanceled())
            {
                //Trigger start animations and audio
                if (action instanceof IWorldChangeAudio)
                {
                    ((IWorldChangeAudio) action).doStartAudio();
                }
                if (action instanceof IWorldChangeGraphics)
                {
                    ((IWorldChangeGraphics) action).doStartDisplay();
                }
                //Trigger pre-destruction effects ( such as mob damage and force calculations)
                action.doEffectOther(true);

                //Check if we want to run as a thread
                if (action.shouldThreadAction() > 0)
                {
                    //Generate thread and return completed as we have no way to track what happens(effectively)
                    ThreadWorldChangeAction thread = new ThreadWorldChangeAction(loc.clone(), action, triggerCause);
                    thread.start();
                }
                else
                {
                    //Same code as a thread but in this thread.
                    Collection<IWorldEdit> effectedBlocks = getEffectedBlocks(loc, triggerCause, action);
                    if (effectedBlocks != null && !effectedBlocks.isEmpty())
                    {
                        for (IWorldEdit v : effectedBlocks)
                        {
                            action.handleBlockPlacement(v);
                            if (action instanceof IWorldChangeAudio)
                            {
                                ((IWorldChangeAudio) action).playAudioForEdit(v);
                            }
                            if (action instanceof IWorldChangeGraphics)
                            {
                                ((IWorldChangeGraphics) action).displayEffectForEdit(v);
                            }
                        }
                    }
                    //Trigger ending animations and audio
                    if (action instanceof IWorldChangeAudio)
                    {
                        ((IWorldChangeAudio) action).doEndAudio();
                    }
                    if (action instanceof IWorldChangeGraphics)
                    {
                        ((IWorldChangeGraphics) action).doEndDisplay();
                    }
                    //Trigger ending effects
                    action.doEffectOther(false);
                }

                return ChangeResult.COMPLETED;
            }
            return ChangeResult.BLOCKED;
        }
        return ChangeResult.FAILED;
    }

    /**
     * Gets a list of blocks that change will effect
     *
     * @param vec          - location in the world
     * @param triggerCause - cause
     * @param blast        - action instance
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

    /** Enum of return types for action completion. */
    public enum ChangeResult
    {
        /** Main action code completed without issues */
        COMPLETED,
        /** Action code failed to run do to a set of conditions */
        FAILED,
        /** Action was blocked or prevented from proceeding */
        BLOCKED
    }
}
