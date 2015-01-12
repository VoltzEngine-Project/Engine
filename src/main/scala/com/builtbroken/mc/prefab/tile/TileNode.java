package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.tile.ITileNodeProvider;
import com.builtbroken.mc.api.tile.node.ITileModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.IUpdate;

import java.util.*;

/**
 * Prefab designed to automate all node interaction of the time.
 * Does use some reflection to generate a list of all fields holding nodes
 * This is only used for Java. For Scala classes, use traits instead.
 * <p/>
 *
 * @author Darkguardsman
 */
public abstract class TileNode extends Tile implements ITileNodeProvider
{
	public TileNode(Material material)
	{
		super(material);
	}

	@Override
	public void onNeighborChanged(Block block)
	{
		super.onNeighborChanged(block);
		reconstructNode();
	}

	@Override
	public void onWorldJoin()
	{
		super.onWorldJoin();
        if(!canUpdate())
		    reconstructNode();
	}

	private void reconstructNode()
	{
		for (ITileModule node : getNodes())
		{
			if (node != null)
			{
				node.onJoinWorld();
			}
		}
	}

	@Override
	public void firstTick()
	{
		super.firstTick();
		reconstructNode();
	}

	@Override
	public void update()
	{
		super.update();
		for (ITileModule node : getNodes())
		{
			if (node instanceof IUpdate)
			{
				if (((IUpdate) node).canUpdate())
				{
					((IUpdate) node).update(1/20);
				}
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
	public abstract void getNodes(List<ITileModule> nodes);

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
