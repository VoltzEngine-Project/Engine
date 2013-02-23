package universalelectricity.core.path;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnectionProvider;

/**
 * Finds all the possible conductors.
 * 
 * @author Calclavia
 * 
 */
public class PathfinderConductor extends Pathfinder
{
	public PathfinderConductor()
	{
		super(new IPathCallBack()
		{
			@Override
			public boolean isValidNode(Pathfinder finder, ForgeDirection direction, IConnectionProvider provider, IConnectionProvider connectedBlock)
			{
				if (connectedBlock instanceof IConductor)
				{
					if (((IConductor) connectedBlock).canConnect(direction.getOpposite()))
					{
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean onSearch(Pathfinder finder, IConnectionProvider provider)
			{
				return false;
			}
		});
	}
}
