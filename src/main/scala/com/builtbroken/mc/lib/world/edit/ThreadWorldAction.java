package com.builtbroken.mc.lib.world.edit;

import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.core.Engine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private boolean waiting = false;
    private boolean kill = false;
    private Logger logger;

    public static final ConcurrentHashMap<String, ThreadWorldAction> threads = new ConcurrentHashMap();

    public ThreadWorldAction(String name)
    {
        super("WorldChangeAction[" + name + "]");
        this.setPriority(Thread.NORM_PRIORITY);
        threads.put(name, this);
        logger = LogManager.getLogger("WorldChangeAction[" + name + "]");
    }

    @Override
    public void run()
    {
        try
        {
            while (!kill)
            {
                if (que.size() > 0)
                {
                    WCAThreadProcess process = que.poll();
                    debug("Running process " + process);
                    long ticks = System.nanoTime();
                    process.run();
                    ticks = System.nanoTime() - ticks;
                    debug("Finished " + process + " in " + StringHelpers.formatNanoTime(ticks));
                }
                else
                {
                    try
                    {
                        debug("sleeping");
                        waiting = true;
                        synchronized (this)
                        {
                            wait(10000);
                        }
                    }
                    catch (IllegalMonitorStateException e)
                    {
                        Engine.instance.logger().error(this + " has failed and is terminating...", e);
                        kill = true;
                    }
                    catch (InterruptedException e)
                    {
                        debug("interrupted");
                    }
                }
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
            debug("Added " + process + " to que");
            que.add(process);
            if (waiting)
            {
                debug("waking");
                //http://tutorials.jenkov.com/java-concurrency/thread-signaling.html
                synchronized (this)
                {
                    waiting = false;
                    notify();
                    debug("woken");
                }
            }
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

    public void kill()
    {
        this.kill = true;
    }

    /**
     * Prints a debug msg to console if debug
     * mode is enabled
     *
     * @param msg - message to print
     */
    protected void debug(String msg)
    {
        if (Engine.runningAsDev)
        {
            logger.info(this + " | " + msg);
        }
    }
}
