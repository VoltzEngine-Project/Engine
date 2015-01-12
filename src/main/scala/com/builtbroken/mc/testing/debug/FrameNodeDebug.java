package com.builtbroken.mc.testing.debug;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.tile.ITileModuleProvider;
import com.builtbroken.mc.api.tile.node.ITileModule;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author Darkguardsman
 */
@SuppressWarnings("serial")
public class FrameNodeDebug extends FrameDebug
{
	protected ITileModuleProvider nodeProvider = null;
	/**
	 * Linked node
	 */
	protected ITileModule node = null;
	protected Class<? extends ITileModule> nodeClazz = null;

	/**
	 * Are we debugging a node
	 */

	public FrameNodeDebug(TileEntity tile, Class<? extends ITileModule> nodeClazz)
	{
		super(tile);
		this.nodeClazz = nodeClazz;
	}

	public FrameNodeDebug(ITileModuleProvider node, Class<? extends ITileModule> nodeClazz)
	{
		super();
		this.nodeProvider = node;
		this.nodeClazz = nodeClazz;
	}

	public FrameNodeDebug(ITileModule node)
	{
		super();
		this.node = node;
	}

	/**
	 * Gets the node used for debug
	 */
	public ITileModule getNode()
	{
		if (tile instanceof ITileModuleProvider && nodeClazz != null)
		{
			return ((ITileModuleProvider) tile).getModule(nodeClazz, ForgeDirection.UNKNOWN);
		}
		else if (nodeProvider != null && nodeClazz != null)
		{
			return nodeProvider.getModule(nodeClazz, ForgeDirection.UNKNOWN);
		}
		return node;
	}

	@Override
	public double z()
	{
		if (nodeProvider instanceof TileEntity)
		{
			return ((TileEntity) nodeProvider).zCoord;
		}
		else if (nodeProvider instanceof IPos3D)
		{
			return ((IPos3D) nodeProvider).z();
		}
		return super.z();
	}

	@Override
	public double x()
	{
		if (nodeProvider instanceof TileEntity)
		{
			return ((TileEntity) nodeProvider).xCoord;
		}
		else if (nodeProvider instanceof IPos3D)
		{
			return ((IPos3D) nodeProvider).x();
		}
		return super.x();
	}

	@Override
	public double y()
	{
		if (nodeProvider instanceof TileEntity)
		{
			return ((TileEntity) nodeProvider).yCoord;
		}
		else if (nodeProvider instanceof IPos3D)
		{
			return ((IPos3D) nodeProvider).y();
		}
		return super.y();
	}

	@Override
	public World world()
	{
		if (nodeProvider instanceof TileEntity)
		{
			return ((TileEntity) nodeProvider).getWorldObj();
		}
		else if (nodeProvider instanceof IWorldPosition)
		{
			return ((IWorldPosition) nodeProvider).world();
		}
		return super.world();
	}
}
