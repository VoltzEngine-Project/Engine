package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.mc.core.Engine;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/7/2015.
 */
public class ThreadWorldAction extends Thread
{
    private Queue<WCAThreadProcess> que = new ConcurrentLinkedQueue<>();

    public static final ConcurrentHashMap<String, ThreadWorldAction> threads = new ConcurrentHashMap();

    public ThreadWorldAction(String name)
    {
        super("WorldChangeAction[" + name + "]");
        this.setPriority(Thread.NORM_PRIORITY);
        threads.put(name, this);
    }

    @Override
    public void run()
    {
        try
        {
            if (que.size() > 0)
            {
                WCAThreadProcess process = que.poll();
                process.run();
            }
            else
            {
                wait(1000);
            }
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("World Change action thread[" + this + "] has failed to execute correctly.", e);
        }
    }

    /**
     * Adds a world change action process to the current thread que
     *
     * @param process
     */
    public void que(WCAThreadProcess process)
    {
        if (!contains(process))
        {
            que.add(process);
            notify();
        }
    }

    /**
     * Checks if the thread contains the current process
     *
     * @param process - process
     * @return true if it contains the process
     */
    public boolean contains(WCAThreadProcess process)
    {
        return que.contains(process);
    }

    /**
     * Number of processes in que
     *
     * @return processes
     */
    public int qued()
    {
        return que.size();
    }
}
