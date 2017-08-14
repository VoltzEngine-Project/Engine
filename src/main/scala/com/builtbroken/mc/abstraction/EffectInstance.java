package com.builtbroken.mc.abstraction;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.abstraction.world.IWorld;

import java.util.HashMap;

/**
 * Object for populating effect data before sent to the client for rendering
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/14/2017.
 */
public class EffectInstance
{
    public double x;
    public double y;
    public double z;

    public double mx;
    public double my;
    public double mz;

    public boolean endPoint = false;

    public final HashMap<String, Object> data = new HashMap();

    public final IWorld world;

    public final String key;

    public EffectInstance(IWorld world, String key, IPos3D pos)
    {
        this.key = key;
        this.world = world;
        setPosition(pos);
    }

    /**
     * Sets the motion of the effect
     * <p>
     * Can't be used with end point
     *
     * @param motion
     */
    public void setMotion(IPos3D motion)
    {
        this.mx = motion.x();
        this.my = motion.y();
        this.mz = motion.z();
        this.endPoint = false;
    }

    /**
     * Sets the end point
     * <p>
     * Can't be used with motion
     *
     * @param endPoint
     */
    public void setEndPoint(IPos3D endPoint)
    {
        this.mx = endPoint.x();
        this.my = endPoint.y();
        this.mz = endPoint.z();
        this.endPoint = true;
    }

    /**
     * Sets the position
     * <p>
     * Mainly used to recycle effect data for repeat effect calls
     *
     * @param pos - position
     */
    public void setPosition(IPos3D pos)
    {
        this.x = pos.x();
        this.y = pos.y();
        this.z = pos.z();
    }

    /**
     * Adds extra data to the effect used by it's provider
     *
     * @param key  - unique data name
     * @param data - data to sync, supports basic types only
     */
    public void addData(String key, Object data) //TODO consider removing Object and repacing with support types only
    {
        this.data.put(key, data);
    }

    /**
     * Sends the effect to the world to be run
     */
    public void send()
    {
        world.runEffect(this);
    }
}
