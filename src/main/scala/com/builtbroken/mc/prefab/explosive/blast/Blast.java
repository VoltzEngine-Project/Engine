package com.builtbroken.mc.prefab.explosive.blast;

import com.builtbroken.mc.api.IWorldPosition;
import net.minecraftforge.common.util.ForgeDirection;
import com.builtbroken.mc.lib.world.edit.IWorldChangeAction;
import net.minecraft.world.World;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.lib.world.edit.BlockEdit;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Blast implements IWorldChangeAction, IWorldPosition
{
    public World world;
    public int x, y, z;
    public int size = 1;
    public float eUnitPerBlock = 5F;

    public TriggerCause cause = new TriggerCause.TriggerCauseRedstone(ForgeDirection.UNKNOWN, 15);

    public Blast(){}

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

    public Blast setYield(int size)
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
    public final Collection<BlockEdit> getEffectedBlocks()
    {
        List<BlockEdit> list = new LinkedList<BlockEdit>();
        getEffectedBlocks(list);
        return list;
    }

    public void getEffectedBlocks(List<BlockEdit> list)
    {

    }

    @Override
    public void handleBlockPlacement(BlockEdit vec)
    {
        if(vec.hasChanged())
            vec.place();
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


}
