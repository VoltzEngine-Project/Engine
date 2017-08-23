package com.builtbroken.mc.lib.render.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/23/2017.
 */
public abstract class FxBase extends Particle
{
    protected FxBase(World worldIn, double posXIn, double posYIn, double posZIn)
    {
        super(worldIn, posXIn, posYIn, posZIn);
    }

    public FxBase(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }

    public void setMotion(double motionX, double motionY, double motionZ)
    {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }
}
