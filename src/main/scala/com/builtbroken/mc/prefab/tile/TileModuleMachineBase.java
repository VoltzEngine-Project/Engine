package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.IUpdate;
import com.builtbroken.mc.api.tile.ISided;
import com.builtbroken.mc.api.tile.ITileModuleProvider;
import com.builtbroken.mc.api.tile.node.ITileModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefab designed to automate all node interaction of the time.
 * Does use some reflection to generate a list of all fields holding nodes
 * This is only used for Java. For Scala classes, use traits instead.
 * <p>
 *
 * @author Darkguardsman
 */
public class TileModuleMachineBase extends TileMachine implements ITileModuleProvider
{
    protected List<ITileModule> modules = new ArrayList();

    public TileModuleMachineBase(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public void onNeighborChanged(Block block)
    {
        super.onNeighborChanged(block);
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onParentChange();
            }
        }
    }

    @Override
    public void onWorldJoin()
    {
        super.onWorldJoin();
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onJoinWorld();
            }
        }
    }

    @Override
    public void update()
    {
        super.update();
        for (ITileModule node : getNodes())
        {
            if (node instanceof IUpdate)
            {
                ((IUpdate) node).update();
            }
        }
    }

    @Override
    public void invalidate()
    {
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onLeaveWorld();
            }
        }
        super.invalidate();
    }

    /**
     * Grabs any node that needs called by save() load() invalidate() update() onJoinWorld() etc
     */
    protected final List<ITileModule> getNodes()
    {
        return modules;
    }

    @Override
    public <N extends ITileModule> N getModule(Class<? extends N> nodeType, ForgeDirection from)
    {
        //TODO think about caching module by side or type to improve CPU time for pipe connections
        for (ITileModule module : getNodes())
        {
            if (!(module instanceof ISided) || ((ISided) module).isValidForSide(from))
            {
                if (nodeType.isAssignableFrom(module.getClass()))
                {
                    return (N) module;
                }
            }
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        for (ITileModule node : getNodes())
        {
            readFromNBT(node, nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        for (ITileModule node : getNodes())
        {
            writeToNBT(node, nbt);
        }
    }

    /**
     * Called to handle reading the module from NBT
     *
     * @param module
     * @param nbt
     */
    protected void readFromNBT(ITileModule module, NBTTagCompound nbt)
    {
        if (module instanceof ISave)
        {
            ((ISave) module).load(nbt);
        }
    }

    /**
     * Called to handle saving the module from NBT
     *
     * @param module
     * @param nbt
     */
    protected void writeToNBT(ITileModule module, NBTTagCompound nbt)
    {
        if (module instanceof ISave)
        {
            ((ISave) module).save(nbt);
        }
    }
}
