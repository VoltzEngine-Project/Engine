package com.builtbroken.mc.prefab.tile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.tile.node.INode;
import com.builtbroken.mc.api.tile.INodeProvider;
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
public abstract class TileNode extends Tile implements INodeProvider
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
		for (INode node : getNodes())
		{
			if (node != null)
			{
				node.reconstruct();
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
		for (INode node : getNodes())
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
		for (INode node : getNodes())
		{
			if (node != null)
			{
				node.deconstruct();
			}
		}
		super.invalidate();
	}

    /** Grabs any node that needs called by save() load() invalidate() update() onJoinWorld() etc */
	protected final List<INode> getNodes()
	{
		List<INode> nodes = new LinkedList<INode>();
		getNodes(nodes);
		return nodes;
	}

	/** Grabs any node that needs called by save() load() invalidate() update() onJoinWorld() etc */
	public abstract void getNodes(List<INode> nodes);

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		for (INode node : getNodes())
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
		for (INode node : getNodes())
		{
			if (node instanceof ISave)
			{
				((ISave) node).save(nbt);
			}
		}
	}
}
