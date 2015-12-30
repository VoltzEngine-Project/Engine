package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeLayeredAction;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.event.WorldChangeActionEvent;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Location;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collection;

/**
 * Low priority Multi-thread for IWorldChangeActions
 * <p/>
 * Calculates the impact then removes X amount of blocks at the end of the world tick
 *
 * @author Darkguardsman
 */
public class WCAThreadProcess
{
    /** Location of the Blast */
    public final Location position;
    /** Blast instance */
    public final IWorldChangeAction blast;
    /** Trigger cause of the blast */
    public final TriggerCause triggerCause;
    /** Blocks per tick limiter */
    public int blocksPerTick = 20;

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

    public void run()
    {
        try
        {
            if (blast instanceof IWorldChangeLayeredAction && ((IWorldChangeLayeredAction) blast).getLayers() > 1)
            {
                for (int i = 0; i < ((IWorldChangeLayeredAction) blast).getLayers(); i++)
                {
                    effectedBlocks = blast.getEffectedBlocks();
                    //Triggers an event allowing other mods to edit the block list
                    MinecraftForge.EVENT_BUS.post(new WorldChangeActionEvent.FinishedCalculatingEffectEvent(position, effectedBlocks, blast, triggerCause));
                    WorldEditQueHandler.addEditQue(new WorldEditQueue(position.world, blast, blocksPerTick, effectedBlocks));
                }
            }
            else
            {
                //Collects the init list of blocks from the blast
                effectedBlocks = blast.getEffectedBlocks();
            }

            //Triggers an event allowing other mods to edit the block list
            MinecraftForge.EVENT_BUS.post(new WorldChangeActionEvent.FinishedCalculatingEffectEvent(position, effectedBlocks, blast, triggerCause));
            WorldEditQueHandler.addEditQue(new WorldEditQueue(position.world, blast, blocksPerTick, effectedBlocks));
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("World Change action thread[" + this + "] has failed to execute correctly.", e);
        }
    }

    /**
     * Called to kill the process.
     */
    public void killAction()
    {
        //TODO change so it saves
        this.blast.killAction(false);
    }


    /**
     * Places this process into a que to be run. If no thread exist it will call
     * the {@link WCAThreadProcess#run()} method. This way there is no chance
     * or loss of effect created by the process.
     */
    public void que()
    {
        for (ThreadWorldAction thread : ThreadWorldAction.threads.values())
        {
            if (thread.contains(this))
            {
                return;
            }
        }
        int lowest = Integer.MAX_VALUE;
        ThreadWorldAction lowestThread = null;
        for (ThreadWorldAction thread : ThreadWorldAction.threads.values())
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
            run();
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