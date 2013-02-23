package universalelectricity.core.electricity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnectionProvider;

/**
 * A path finding class to look for all conductors.
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
			public boolean isValidNode(Pathfinder finder, ForgeDirection direction, IConnectionProvider provider, TileEntity connectedBlock, TileEntity[] connectedBlocks)
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
		});
	}
}
