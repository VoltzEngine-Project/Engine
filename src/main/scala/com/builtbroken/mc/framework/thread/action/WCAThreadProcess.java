package com.builtbroken.mc.framework.thread.action;

import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeLayeredAction;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.event.WorldChangeActionEvent;
import com.builtbroken.mc.api.process.IThreadProcess;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.vector.Location;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Low priority Multi-thread for IWorldChangeActions
 * <p>
 * Calculates the impact then removes X amount of blocks at the end of the world tick
 *
 * @author Darkguardsman
 */
public class WCAThreadProcess implements IThreadProcess
{
    public static int MAX_EDITS_PER_TICK = 200;

    /** Location of the Blast */
    public final Location position;
    /** Blast instance */
    public final IWorldChangeAction blast;
    /** Trigger cause of the blast */
    public final TriggerCause triggerCause;
    /** Blocks per tick limiter */
    public int blocksPerTick;

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
        if (blocksPerTick < 0)
        {
            //blocksPerTick = MAX_EDITS_PER_TICK;
        }
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
                        long time = System.nanoTime();
                        effectedBlocks = new ArrayList();
                        ((IWorldChangeLayeredAction) blast).getEffectedBlocks((List<IWorldEdit>) effectedBlocks, i);
                        if (Engine.runningAsDev)
                        {
                            time = System.nanoTime() - time;
                            System.out.println("Layer(" + i + ") generated " + effectedBlocks.size() + " blocks and took " + StringHelpers.formatNanoTime(time));
                        }

                        //Triggers an event allowing other mods to edit the block list
                        MinecraftForge.EVENT_BUS.post(new WorldChangeActionEvent.FinishedCalculatingEffectEvent(position, effectedBlocks, blast, triggerCause));
                        WorldActionQue.addEditQue(new WorldEditQueue(position.world, blast, blocksPerTick, effectedBlocks));
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                //Collects the init list of blocks from the blast
                effectedBlocks = blast.getEffectedBlocks();
                //Triggers an event allowing other mods to edit the block list
                MinecraftForge.EVENT_BUS.post(new WorldChangeActionEvent.FinishedCalculatingEffectEvent(position, effectedBlocks, blast, triggerCause));
                WorldActionQue.addEditQue(new WorldEditQueue(position.world, blast, blocksPerTick, effectedBlocks));
            }
            WorldActionQue.addEditQue(new WorldChangeActionPost(blast));
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