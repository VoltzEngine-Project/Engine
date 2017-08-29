package com.builtbroken.mc.framework.thread.action;

import com.builtbroken.mc.api.process.IWorldAction;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Contains references to current ques and runs them one at a time.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/30/2015.
 */
public final class WorldActionQue
{
    /** Current queue of queues */
    private static final Queue<IWorldAction> editQues = new LinkedList();
    public static int MAX_EDITS_PER_TICK = 1000;
    /** Current queue being processed */
    private static IWorldAction currentQue;

    /**
     * Adds a queue to the process list
     *
     * @param queue - queue to add
     */
    public static void addEditQue(IWorldAction queue)
    {
        synchronized (editQues)
        {
            if (!editQues.contains(queue))
            {
                editQues.offer(queue);
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        //Run at end of world tick TODO get time left until next tick
        if (event.phase == TickEvent.Phase.END)
        {
            final long startTime = System.nanoTime();

            int edits = 0;

            //Time is less than 10ms and edits are less then max
            while (System.nanoTime() - startTime < 10000 && edits < MAX_EDITS_PER_TICK)
            {
                if (currentQue == null || currentQue.isQueDone())
                {
                    currentQue = editQues.poll();
                }
                if (currentQue != null)
                {
                    int i = currentQue.runQue(event.world, event.side);
                    if (i > 0)
                    {
                        edits += i;
                    }
                }
            }
        }
    }
}
