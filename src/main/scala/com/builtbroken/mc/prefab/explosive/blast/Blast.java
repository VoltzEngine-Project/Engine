package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeAudio;
import com.builtbroken.mc.api.edit.IWorldChangeGraphics;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefab for implement explosive blast actions
 */
public abstract class Blast<B extends Blast> implements IWorldChangeAction, IWorldPosition, IWorldChangeAudio, IWorldChangeGraphics
{
    /** Current world */
    public World world;
    /** Center coords of the blast */
    public double x, y, z;
    /** Size of the explosive */
    public double size = 1;
    /** Energy cost to damage each block */
    public float eUnitPerBlock = 5F;
    /** Used to stop the explosion mid action */
    public boolean killExplosion = false;

    /** Cause of the explosion */
    public TriggerCause cause = new TriggerCause.TriggerCauseRedstone(ForgeDirection.UNKNOWN, 15);
    private NBTTagCompound additionBlastData;

    public Blast() {}

    public Blast(final World world, int x, int y, int z, int size)
    {
        setLocation(world, x, y, z);
        setYield(size);
    }

    public B setLocation(final World world, double x, double y, double z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        return (B) this;
    }

    public B setYield(double size)
    {
        this.size = size;
        return (B) this;
    }

    public B setEnergyPerBlock(float f)
    {
        this.eUnitPerBlock = f;
        return (B) this;
    }

    public B setCause(final TriggerCause cause)
    {
        this.cause = cause;
        return (B) this;
    }

    /**
     * Sets the custome NBT data to be used by the explosive
     *
     * @param additionBlastData
     */
    public B setAdditionBlastData(NBTTagCompound additionBlastData)
    {
        this.additionBlastData = additionBlastData;
        return (B)this;
    }

    /** Custom NBT data provided by the explosive */
    public NBTTagCompound getAdditionBlastData()
    {
        return additionBlastData;
    }

    @Override
    public int shouldThreadAction()
    {
        return size > 4 ? 20 : -1;
    }

    @Override
    public List<IWorldEdit> getEffectedBlocks()
    {
        List<IWorldEdit> list = new ArrayList<IWorldEdit>();
        getEffectedBlocks(list);
        return list;
    }

    /**
     * Called by {@link #getEffectedBlocks()} to make processing
     * a list of blocks eaiser.
     *
     * @param list - list to add changes too.
     */
    public void getEffectedBlocks(final List<IWorldEdit> list)
    {

    }

    @Override
    public void handleBlockPlacement(final IWorldEdit vec)
    {
        if (vec != null && vec.hasChanged())
        {
            vec.place();
        }
    }

    @Override
    public void doEffectOther(boolean beforeBlocksPlaced)
    {

    }

    @Override
    public void killAction(boolean willSave)
    {
        this.killExplosion = true;
    }

    /**
     * Checks if the world action should be killed
     *
     * @return true if the action should be killed
     */
    protected boolean shouldKillAction()
    {
        return killExplosion || world == null || world.provider == null || DimensionManager.getWorld(world.provider.dimensionId) == null;
    }

    @Override
    public World world()
    {
        return world;
    }

    @Override
    public double x()
    {
        return x;
    }

    @Override
    public double y()
    {
        return y;
    }

    @Override
    public double z()
    {
        return z;
    }


    @Override
    public void playAudioForEdit(IWorldEdit blocks)
    {
    }

    @Override
    public void doStartAudio()
    {
    }

    @Override
    public void doEndAudio()
    {
    }

    @Override
    public void displayEffectForEdit(IWorldEdit blocks)
    {
    }

    @Override
    public void doStartDisplay()
    {

    }

    @Override
    public void doEndDisplay()
    {

    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (obj.getClass() == getClass() && obj instanceof Blast)
        {
            if (((Blast) obj).world != world)
            {
                return false;
            }
            else if (((Blast) obj).x != x)
            {
                return false;
            }
            else if (((Blast) obj).y != y)
            {
                return false;
            }
            else if (((Blast) obj).z != z)
            {
                return false;
            }
            else if (((Blast) obj).size != size)
            {
                return false;
            }
            return true;
        }
        return false;
    }
}
