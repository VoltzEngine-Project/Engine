package com.builtbroken.mc.lib.node;

import com.builtbroken.mc.api.IPosWorld;
import com.builtbroken.mc.api.tile.ITileNodeProvider;
import com.builtbroken.mc.api.tile.node.ITileModule;
import li.cil.oc.common.tileentity.traits.TileEntity;
import net.minecraft.world.World;

/**
 * Created by robert on 1/12/2015.
 */
public class AbstractNode implements ITileModule, IPosWorld
{
    private final ITileNodeProvider parent;

    public AbstractNode(ITileNodeProvider parent)
    {
        this.parent = parent;
        if(!(parent instanceof TileEntity))
        {

        }
    }

    @Override
    public void onJoinWorld()
    {

    }

    @Override
    public void onLeaveWorld()
    {

    }

    @Override
    public ITileNodeProvider getParent()
    {
        return parent;
    }

    @Override
    public World world()
    {
        return ((TileEntity)parent).getWorldObj();
    }

    @Override
    public double x()
    {
        return ((TileEntity)parent).x();
    }

    @Override
    public double y()
    {
        return ((TileEntity)parent).y();
    }

    @Override
    public double z()
    {
        return ((TileEntity)parent).z();
    }
}
