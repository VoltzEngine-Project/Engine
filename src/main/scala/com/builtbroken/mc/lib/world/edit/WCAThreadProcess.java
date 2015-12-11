package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeAudio;
import com.builtbroken.mc.api.edit.IWorldChangeGraphics;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.event.WorldChangeActionEvent;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Location;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collection;
import java.util.Iterator;

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
            //Collects the init list of blocks from the blast
            effectedBlocks = blast.getEffectedBlocks();

            //Triggers an event allowing other mods to edit the block list
            MinecraftForge.EVENT_BUS.post(new WorldChangeActionEvent.FinishedCalculatingEffectEvent(position, effectedBlocks, blast, triggerCause));

            //If we have blocks to edit then register with the event handler
            if (effectedBlocks != null && !effectedBlocks.isEmpty())
            {
                FMLCommonHandler.instance().bus().register(this);
            }
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("World Change action thread[" + this + "] has failed to execute correctly.", e);
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        try
        {
            if (event.phase == TickEvent.Phase.END)
            {
                Iterator<IWorldEdit> it = effectedBlocks.iterator();
                int c = 0;
                while (it.hasNext() && c++ <= blocksPerTick)
                {
                    IWorldEdit edit = it.next();
                    try
                    {
                        if (!event.world.isRemote)
                        {
                            blast.handleBlockPlacement(edit);
                        }
                        if (blast instanceof IWorldChangeAudio)
                        {
                            ((IWorldChangeAudio) blast).playAudioForEdit(edit);
                        }
                        if (blast instanceof IWorldChangeGraphics)
                        {
                            ((IWorldChangeGraphics) blast).displayEffectForEdit(edit);
                        }
                    }
                    catch (Exception e)
                    {
                        Engine.instance.logger().error("Failed to place block for change action"
                                + "\nSide: " + event.side
                                + "\nChangeAction: " + blast
                                + "\nEdit: " + edit
                                , e);
                    }
                    it.remove();
                }
            }
            if (effectedBlocks == null || effectedBlocks.isEmpty())
            {
                FMLCommonHandler.instance().bus().unregister(this);
                if (blast instanceof IWorldChangeAudio)
                {
                    ((IWorldChangeAudio) blast).doEndAudio();
                }
                if (blast instanceof IWorldChangeGraphics)
                {
                    ((IWorldChangeGraphics) blast).doEndDisplay();
                }
            }
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("Crash while processing world change " + blast, e);
            //TODO insert crash protection to kill thread if crashing continues
        }
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