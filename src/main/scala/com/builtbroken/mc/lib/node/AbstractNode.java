package com.builtbroken.mc.lib.node;

import com.builtbroken.mc.api.IPosWorld;
import com.builtbroken.mc.api.IUpdate;
import com.builtbroken.mc.api.tile.ITileModuleProvider;
import com.builtbroken.mc.api.tile.node.ITileModule;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by robert on 1/12/2015.
 */
public class AbstractNode implements ITileModule, IPosWorld, IUpdate
{
    private final ITileModuleProvider parent;

    public AbstractNode(ITileModuleProvider parent)
    {
        this.parent = parent;
        if (!(parent instanceof TileEntity))
        {
            throw new IllegalArgumentException(getClass() + " requires that " + parent +" is an instanceof TileEntity");
        }
    }


    @Override
    public boolean update()
    {
        return false;
    }

    @Override
    public void onJoinWorld()
    {

    }

    @Override
    public void onParentChange()
    {

    }

    @Override
    public void onLeaveWorld()
    {

    }

    //==============================
    //========= Helper =============
    //==============================

    public <N extends ITileModule> N getModule(Class<? extends N> nodeType, ForgeDirection from)
    {
        TileEntity tile = toLocation().getTileEntity();
        if(tile instanceof ITileModuleProvider)
        {
            return ((ITileModuleProvider) tile).getModule(nodeType, from);
        }
        return null;
    }


    //==============================
    //====== Accessors =============
    //==============================

    @Override
    public ITileModuleProvider getParent()
    {
        return parent;
    }

    @Override
    public World world()
    {
        return ((TileEntity) parent).getWorldObj();
    }

    @Override
    public double x()
    {
        return ((TileEntity) parent).xCoord;
    }

    @Override
    public double y()
    {
        return ((TileEntity) parent).yCoord;
    }

    @Override
    public double z()
    {
        return ((TileEntity) parent).zCoord;
    }


    //==============================
    //====== Converters ============
    //==============================

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + hashCode() + "]";
    }

    public Location toLocation()
    {
        return new Location(this);
    }

    public Pos toPos()
    {
        return new Pos(this);
    }
}
