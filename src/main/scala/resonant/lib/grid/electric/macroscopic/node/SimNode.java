package resonant.lib.grid.electric.macroscopic.node;

import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.grid.IGrid;
import resonant.api.grid.INodeProvider;
import resonant.api.grid.sim.IPathNode;
import resonant.api.grid.sim.SimType;
import resonant.lib.grid.electric.macroscopic.PathGrid;
import resonant.lib.grid.node.NodeConnector;

/**
 * Node prefab for the simulator grid
 * @author Darkguardsman
 */
public class SimNode extends NodeConnector implements IPathNode
{
    protected PathGrid network;

    public SimNode(INodeProvider parent)
    {
        super(parent);
    }

    @Override
    public void setGrid(IGrid grid)
    {
        if(grid instanceof PathGrid)
        {
            network = (PathGrid) grid;
        }
    }

    @Override
    public PathGrid getGrid()
    {
        if(network == null)
        {
            network = new PathGrid(this);
			network.add(this);
        }
        return network;
    }

    @Override
    public boolean canPassToSide(SimType type, ForgeDirection from, ForgeDirection two)
    {
        return true;
    }
}
