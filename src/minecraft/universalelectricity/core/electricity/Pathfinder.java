package universalelectricity.core.electricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnectionProvider;

/**
 * A class that allows flexible path finding in Minecraft Blocks.
 * 
 * @author Calclavia
 * 
 */
public class Pathfinder
{
	public interface IPathCallBack
	{
		public boolean isValidNode(Pathfinder finder, ForgeDirection direction, IConnectionProvider provider, TileEntity connectedBlock, TileEntity[] connectedBlocks);
	}

	public IPathCallBack callBackCheck;
	public List<IConnectionProvider> nodesInPath;
	public List<IConnectionProvider> iteratedNodes;

	public Pathfinder(IPathCallBack callBack)
	{
		this.callBackCheck = callBack;
		this.nodesInPath = new ArrayList<IConnectionProvider>();
		this.iteratedNodes = new ArrayList<IConnectionProvider>();
	}

	public Pathfinder findAllNodes(IConnectionProvider provider)
	{
		TileEntity[] connectedBlocks = provider.getConnectedBlocks();

		this.iteratedNodes.add(provider);

		for (int i = 0; i < connectedBlocks.length; i++)
		{
			TileEntity connectedBlock = connectedBlocks[i];

			if (connectedBlock != null)
			{
				if (!iteratedNodes.contains(connectedBlock))
				{
					if (this.callBackCheck.isValidNode(this, ForgeDirection.getOrientation(i), provider, connectedBlock, connectedBlocks))
					{
						this.findAllNodes((IConductor) connectedBlock);
					}
				}
			}
		}

		return this;
	}
}
