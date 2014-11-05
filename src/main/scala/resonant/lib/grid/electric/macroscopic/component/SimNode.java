package resonant.lib.grid.electric.macroscopic.component;

import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.grid.IGrid;
import resonant.api.grid.INodeProvider;
import resonant.api.grid.sim.ISimNode;
import resonant.api.grid.sim.SimType;
import resonant.lib.grid.electric.macroscopic.SimulatedGrid;
import resonant.lib.grid.node.NodeConnector;

/**
 * Node prefab for the simulator grid
 * @author Darkguardsman
 */
public class SimNode extends NodeConnector implements ISimNode
{
    protected SimulatedGrid network;

    public SimNode(INodeProvider parent)
    {
        super(parent);
    }

    @Override
    public void setGrid(IGrid grid)
    {
        if(grid instanceof SimulatedGrid)
        {
            network = (SimulatedGrid) grid;
        }
    }

    @Override
    public SimulatedGrid getGrid()
    {
        if(network == null)
        {
            network = new SimulatedGrid(this);
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
