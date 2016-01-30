package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeAudio;
import com.builtbroken.mc.api.edit.IWorldChangeGraphics;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Prefab for implement explosive blast actions
 */
public abstract class Blast implements IWorldChangeAction, IWorldPosition, IWorldChangeAudio, IWorldChangeGraphics
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
    /** Custom NBT data provided by the explosive */
    public NBTTagCompound additionBlastData;

    public Blast() {}

    public Blast(final World world, int x, int y, int z, int size)
    {
        setLocation(world, x, y, z);
        setYield(size);
    }

    public Blast setLocation(final World world, double x, double y, double z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Blast setYield(double size)
    {
        this.size = size;
        return this;
    }

    public Blast setEnergyPerBlock(float f)
    {
        this.eUnitPerBlock = f;
        return this;
    }

    public Blast setCause(final TriggerCause cause)
    {
        this.cause = cause;
        return this;
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
        if (world.isRemote)
        {
            try
            {
                world.playAuxSFX(2001, (int)x, (int)y, (int)z, Block.getIdFromBlock(blocks.getBlock()));
            }
            catch (Exception e)
            {
                if (Engine.runningAsDev)
                {
                    Engine.logger().error("Failed to play audio for block " + blocks, e);
                }
            }
        }
    }

    @Override
    public void doStartAudio()
    {
        world.playAuxSFX(1002, (int)x, (int)y, (int)z, 0);
    }

    @Override
    public void doEndAudio()
    {
        world.playAuxSFX(1002, (int)x, (int)y, (int)z, 0);
    }

    @Override
    public void displayEffectForEdit(IWorldEdit blocks)
    {
        if (world.isRemote)
        {
            Location v = new Location(blocks);
            v.playBlockBreakAnimation();

            Random rand = blocks.world().rand;
            for (int i = 0; i < 3 + rand.nextInt(10); i++)
            {
                v = new Location(blocks).addRandom(rand, 0.5);
                Pos vel = new Pos().addRandom(rand, 0.2);
                v.spawnParticle("portal", vel);
            }
        }
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
