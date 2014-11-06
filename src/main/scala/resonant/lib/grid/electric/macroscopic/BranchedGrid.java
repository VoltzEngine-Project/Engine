package resonant.lib.grid.electric.macroscopic;

import resonant.api.grid.INode;
import resonant.lib.grid.Grid;
import resonant.lib.grid.GridTicking;
import resonant.lib.grid.electric.macroscopic.part.Part;
import resonant.lib.grid.node.NodeConnector;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by robert on 11/5/2014.
 */
public class BranchedGrid<N extends NodeConnector> extends GridTicking<N>
{
    private boolean markForMapping = true;
    private Set<Part> parts = new HashSet<Part>();

    public BranchedGrid(Class cla)
    {
        super(cla);
    }

    @Override
    public void update(double delta)
    {
        if(markForMapping)
        {
            GridPathfinder pathfinder = new GridPathfinder(this);
            parts = pathfinder.generateParts();
        }
    }
}
