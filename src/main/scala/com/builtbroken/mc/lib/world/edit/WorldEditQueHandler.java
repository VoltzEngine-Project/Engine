package com.builtbroken.mc.lib.world.edit;

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
public class WorldEditQueHandler
{
    /** Current queue of queues */
    private static final Queue<WorldEditQueue> editQues = new LinkedList();
    /** Current queue being processed */
    private static WorldEditQueue currentQue;

    /**
     * Adds a queue to the process list
     *
     * @param queue - queue to add
     */
    public static void addEditQue(WorldEditQueue queue)
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
        if (event.phase == TickEvent.Phase.END)
        {
            if (currentQue == null || currentQue.isEmpty())
            {
                currentQue = editQues.poll();
            }
            if (currentQue != null)
            {
                currentQue.runQue(event.world, event.side);
            }
        }
    }
}
