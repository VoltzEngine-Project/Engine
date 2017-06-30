package com.builtbroken.mc.lib.world.edit.thread;

import com.builtbroken.mc.api.VoltzEngineAPI;
import com.builtbroken.mc.api.process.IThreadProcess;
import com.builtbroken.mc.api.process.IWorkerThread;
import com.builtbroken.mc.core.Engine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread used to do repeat tasks that need to run outside of the minecraft world.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/7/2015.
 */
public class WorkerThread extends Thread implements IWorkerThread
{
    private Queue<IThreadProcess> queue = new ConcurrentLinkedQueue<>();
    private boolean waiting = false;
    private boolean kill = false;
    private boolean paused = false;

    private IThreadProcess currentProcess;


    public WorkerThread(String name)
    {
        super("WorkerThread[" + name + "]");
        this.setPriority(Thread.NORM_PRIORITY);
        VoltzEngineAPI.WORKER_THREADS.put(name, this);
    }

    @Override
    public void run()
    {
        try
        {
            while (!kill)
            {
                if (paused)
                {
                    wait(100);
                }
                if (queue.size() > 0)
                {
                    currentProcess = queue.poll();
                    currentProcess.runProcess();
                }
                else
                {
                    try
                    {
                        waiting = true;
                        synchronized (this)
                        {
                            wait(1000);
                        }
                    }
                    catch (IllegalMonitorStateException e)
                    {
                        Engine.instance.logger().error(this + " has failed and is terminating...", e);
                        kill = true;
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("World Change action thread[" + this + "] has failed to execute correctly.", e);
        }
        finally
        {
            //Clean up to prevent currentProcess from running after world has closed
            if (queue.size() > 0)
            {
                Engine.instance.logger().info("Killing " + this + " with processes left to run...");
                for (IThreadProcess process : queue)
                {
                    Engine.instance.logger().info("\t" + process);
                    process.killAction();
                }
            }
        }
    }

    @Override
    public void add(IThreadProcess process)
    {
        if (!contains(process))
        {
            queue.add(process);
            if (waiting)
            {
                //http://tutorials.jenkov.com/java-concurrency/thread-signaling.html
                synchronized (this)
                {
                    waiting = false;
                    notify();
                }
            }
        }
    }

    @Override
    public boolean contains(IThreadProcess process)
    {
        return queue.contains(process);
    }

    @Override
    public int containedProcesses()
    {
        return queue.size();
    }

    @Override
    public void kill()
    {
        this.kill = true;
        if (currentProcess != null)
        {
            currentProcess.killAction();
        }
    }

    @Override
    public boolean clearProcesses()
    {
        if (currentProcess != null)
        {
            currentProcess.killAction();
        }
        queue.clear();
        return queue.isEmpty();
    }

    @Override
    public boolean pauseWorker()
    {
        paused = true;
        return paused;
    }

    @Override
    public boolean resumeWorker()
    {
        paused = false;
        return !paused;
    }
}
