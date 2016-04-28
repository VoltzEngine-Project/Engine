package com.builtbroken.mc.lib.world.edit.thread;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeLayeredAction;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.event.WorldChangeActionEvent;
import com.builtbroken.mc.api.process.IThreadProcess;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.edit.thread.action.WorldChangeActionPost;
import com.builtbroken.mc.lib.world.edit.thread.action.WorldEditQueue;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collection;

/**
 * Low priority Multi-thread for IWorldChangeActions
 * <p>
 * Calculates the impact then removes X amount of blocks at the end of the world tick
 *
 * @author Darkguardsman
 */
public class WCAThreadProcess implements IThreadProcess
{
    /** Location of the Blast */
    public final Location position;
    /** Blast instance */
    public final IWorldChangeAction blast;
    /** Trigger cause of the blast */
    public final TriggerCause triggerCause;
    /** Blocks per tick limiter */
    public int blocksPerTick = 200;

    /** Blocks to remove from the world */
    private Collection<IWorldEdit> effectedBlocks;

    /**
     * Constructor, nothing should be null and blast should be created with the center equaling
     * vec param. If its isn't it will cause events triggered to return the incorrect results.
     *
     * @param vec          - location of the blast, should be the center
     * @param blast        - blast instance used to remove blocks and build a list
     * @param triggerCause - cause of the explosion
     */
    public WCAThreadProcess(Location vec, IWorldChangeAction blast, TriggerCause triggerCause)
    {
        this.position = vec;
        this.blast = blast;
        this.triggerCause = triggerCause;
        blocksPerTick = blast.shouldThreadAction();
    }

    @Override
    public void runProcess()
    {
        try
        {
            if (blast instanceof IWorldChangeLayeredAction && ((IWorldChangeLayeredAction) blast).getLayers() > 1)
            {
                for (int i = 0; i < ((IWorldChangeLayeredAction) blast).getLayers(); i++)
                {
                    if (((IWorldChangeLayeredAction) blast).shouldContinueAction(i))
                    {
                        effectedBlocks = blast.getEffectedBlocks();
                        //Triggers an event allowing other mods to edit the block list
                        MinecraftForge.EVENT_BUS.post(new WorldChangeActionEvent.FinishedCalculatingEffectEvent(position, effectedBlocks, blast, triggerCause));
                        WorldActionQue.addEditQue(new WorldEditQueue(position.world, blast, blocksPerTick, effectedBlocks));
                    }
                }
                WorldActionQue.addEditQue(new WorldChangeActionPost(blast));
            }
            else
            {
                //Collects the init list of blocks from the blast
                effectedBlocks = blast.getEffectedBlocks();
            }

            //Triggers an event allowing other mods to edit the block list
            MinecraftForge.EVENT_BUS.post(new WorldChangeActionEvent.FinishedCalculatingEffectEvent(position, effectedBlocks, blast, triggerCause));
            WorldActionQue.addEditQue(new WorldEditQueue(position.world, blast, blocksPerTick, effectedBlocks));
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("World Change action thread[" + this + "] has failed to execute correctly.", e);
        }
    }

    @Override
    public void killAction()
    {
        //TODO change so it saves
        this.blast.killAction(false);
    }


    @Override
    public void queProcess()
    {
        for (WorkerThread thread : WorkerThread.threads.values())
        {
            if (thread.contains(this))
            {
                return;
            }
        }
        int lowest = Integer.MAX_VALUE;
        WorkerThread lowestThread = null;
        for (WorkerThread thread : WorkerThread.threads.values())
        {
            if (thread.qued() < lowest)
            {
                lowest = thread.qued();
                lowestThread = thread;
            }
        }
        if (lowestThread != null)
        {
            lowestThread.que(this);
        }
        else
        {
            runProcess();
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (obj instanceof WCAThreadProcess)
        {
            if (((WCAThreadProcess) obj).position != position)
            {
                return false;
            }
            else if (((WCAThreadProcess) obj).blast != blast)
            {
                return false;
            }
            else if (((WCAThreadProcess) obj).triggerCause != triggerCause)
            {
                return false;
            }
        }
        return false;
    }
}