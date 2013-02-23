package universalelectricity.core.path;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
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
		/**
		 * Is this a valid node to search for?
		 * 
		 * @return
		 */
		public boolean isValidNode(Pathfinder finder, ForgeDirection direction, IConnectionProvider provider, IConnectionProvider node);

		/**
		 * Called when looping through nodes.
		 * 
		 * @param finder
		 * @param provider
		 * @return True to stop the path finding operation.
		 */
		public boolean onSearch(Pathfinder finder, IConnectionProvider provider);
	}

	public IPathCallBack callBackCheck;
	public List<IConnectionProvider> nodesInPath;
	public List<IConnectionProvider> iteratedNodes;
	public List<IConnectionProvider> findings;

	public Pathfinder(IPathCallBack callBack)
	{
		this.callBackCheck = callBack;
		this.clear();
	}

	public boolean findNodes(IConnectionProvider provider)
	{
		TileEntity[] connectedBlocks = provider.getAdjacentConnections();

		this.iteratedNodes.add(provider);

		if (this.callBackCheck.onSearch(this, provider))
		{
			return false;
		}

		for (int i = 0; i < connectedBlocks.length; i++)
		{
			TileEntity connectedBlock = connectedBlocks[i];

			if (connectedBlock instanceof IConnectionProvider)
			{
				if (!iteratedNodes.contains(connectedBlock))
				{
					if (this.callBackCheck.isValidNode(this, ForgeDirection.getOrientation(i), provider, (IConnectionProvider) connectedBlock))
					{
						if (!this.findNodes((IConnectionProvider) connectedBlock))
						{
							return false;
						}

					}
				}
			}
		}

		return true;
	}

	public Pathfinder init(IConnectionProvider provider)
	{
		this.findNodes(provider);
		return this;
	}

	public Pathfinder clear()
	{
		this.nodesInPath = new ArrayList<IConnectionProvider>();
		this.iteratedNodes = new ArrayList<IConnectionProvider>();
		this.findings = new ArrayList<IConnectionProvider>();
		return this;
	}
}
