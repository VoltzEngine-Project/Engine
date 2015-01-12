package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.tile.ISided;
import com.builtbroken.mc.api.tile.ITileModuleProvider;
import com.builtbroken.mc.api.tile.node.ITileModule;
import com.builtbroken.mc.prefab.tile.module.InventoryModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.IUpdate;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

/**
 * Prefab designed to automate all node interaction of the time.
 * Does use some reflection to generate a list of all fields holding nodes
 * This is only used for Java. For Scala classes, use traits instead.
 * <p/>
 *
 * @author Darkguardsman
 */
public class TileModuleMachine extends TileMachine implements ITileModuleProvider
{
    protected HashMap<String, List<ITileModule>> modules = new HashMap();

	public TileModuleMachine(String name, Material material)
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

    /** Grabs any node that needs called by save() load() invalidate() update() onJoinWorld() etc */
	protected final List<ITileModule> getNodes()
	{
		List<ITileModule> nodes = new LinkedList<ITileModule>();
		getNodes(nodes);
		return nodes;
	}

	/** Grabs any node that needs called by save() load() invalidate() update() onJoinWorld() etc */
	public void getNodes(List<ITileModule> nodes)
    {
        for(List<ITileModule> list : modules.values())
        {
            if(list != null)
                nodes.addAll(list);
        }
    }

    @Override
    public <N extends ITileModule> N getModule(Class<? extends N> nodeType, ForgeDirection from)
    {
        if(modules.containsKey("inventory") && IInventory.class.isAssignableFrom(nodeType))
        {
            List<ITileModule> list = modules.get("inventory");
            for(ITileModule module : list)
            {
                if(!(module instanceof ISided) || ((ISided) module).isValidForSide(from))
                {
                    if(nodeType.isAssignableFrom(module.getClass()))
                    {
                        return (N) module;
                    }
                }
            }
        }
        else
        {
            for(ITileModule module : getNodes())
            {
                if(!(module instanceof ISided) || ((ISided) module).isValidForSide(from))
                {
                    if(nodeType.isAssignableFrom(module.getClass()))
                    {
                        return (N) module;
                    }
                }
            }
        }
        return null;
    }

    public void addModule(String id, ITileModule module)
    {
        List<ITileModule> list = null;

        if(modules.containsKey(id))
            list = modules.get(id);

        if(list != null)
            list = new ArrayList();

        list.add(module);
        modules.put(id, list);
    }

    public void addInventoryModule(int size)
    {
        addModule("inventory", new InventoryModule(this, size));
    }

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		for (ITileModule node : getNodes())
		{
			if (node instanceof ISave)
			{
				((ISave) node).load(nbt);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		for (ITileModule node : getNodes())
		{
			if (node instanceof ISave)
			{
				((ISave) node).save(nbt);
			}
		}
	}
}
