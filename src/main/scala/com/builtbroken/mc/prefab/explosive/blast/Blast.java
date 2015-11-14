package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.edit.IWorldChangeAudio;
import com.builtbroken.mc.api.edit.IWorldChangeGraphics;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.core.Engine;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Blast implements IWorldChangeAction, IWorldPosition, IWorldChangeAudio, IWorldChangeGraphics
{
    public World world;
    public int x, y, z;
    public double size = 1;
    public float eUnitPerBlock = 5F;

    public TriggerCause cause = new TriggerCause.TriggerCauseRedstone(ForgeDirection.UNKNOWN, 15);

    public Blast() {}

    public Blast(World world, int x, int y, int z, int size)
    {
        setLocation(world, x, y, z);
        setYield(size);
    }

    public Blast setLocation(World world, int x, int y, int z)
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

    public Blast setCause(TriggerCause cause)
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
    public final Collection<IWorldEdit> getEffectedBlocks()
    {
        List<IWorldEdit> list = new LinkedList<>();
        getEffectedBlocks(list);
        return list;
    }

    public void getEffectedBlocks(List<IWorldEdit> list)
    {

    }

    @Override
    public void handleBlockPlacement(IWorldEdit vec)
    {
        if (vec.hasChanged())
        { vec.place(); }
    }

    @Override
    public void doEffectOther(boolean beforeBlocksPlaced)
    {

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
        try
        {
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(blocks.getBlock()));
        }
        catch (Exception e)
        {
            if (Engine.runningAsDev)
            {
                Engine.logger().error("Failed to play audio for block " + blocks, e);
            }
        }
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
}
