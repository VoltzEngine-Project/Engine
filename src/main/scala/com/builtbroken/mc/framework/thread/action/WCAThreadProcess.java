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

    public List<LayerDebugData> layerDebugData;

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
                        //Get start time
                        long time = System.nanoTime();


                        ///Create list to populate edits
                        effectedBlocks = new ArrayList();

                        //Get edits for layer
                        ((IWorldChangeLayeredAction) blast).getEffectedBlocks((List<IWorldEdit>) effectedBlocks, i);

                        //If running as dev, log data about layer
                        if (Engine.runningAsDev)
                        {
                            time = System.nanoTime() - time;

                            if (layerDebugData == null)
                            {
                                layerDebugData = new ArrayList();
                            }

                            LayerDebugData data = new LayerDebugData();
                            data.layer = i;
                            data.blocksEdited = effectedBlocks.size();
                            data.timeProcessedNano = time;

                            layerDebugData.add(data);
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

                if (Engine.runningAsDev && layerDebugData.size() > 0)
                {
                    long time = 0;
                    long blocks = 0;

                    for (LayerDebugData data : layerDebugData)
                    {
                        time += data.timeProcessedNano;
                        blocks += data.blocksEdited;
                    }

                    Engine.logger().info(this + " ran " + layerDebugData.size() + " layers in " + StringHelpers.formatNanoTime(time) + " edit " + blocks + " blocks with an average of " + StringHelpers.formatNanoTime(time / layerDebugData.size()) + " per layer");
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
            Engine.logger().error("World Change action thread[" + this + "] has failed to execute correctly.", e);
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

    @Override
    public String toString()
    {
        return "WCAThreadProcess[" + position + " || " + blast + "]@" + hashCode();
    }

    public static class LayerDebugData
    {
        public int layer;
        public int blocksEdited;
        public long timeProcessedNano;

        @Override
        public String toString()
        {
            return "Layer[" + layer + "] edited " + blocksEdited + " tiles, took " + StringHelpers.formatNanoTime(timeProcessedNano);
        }
    }
}