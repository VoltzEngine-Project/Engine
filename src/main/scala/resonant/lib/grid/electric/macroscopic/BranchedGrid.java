package resonant.lib.grid.electric.macroscopic;

import resonant.lib.grid.GridTicking;
import resonant.lib.grid.UpdateTicker;
import resonant.lib.grid.electric.macroscopic.part.Part;
import resonant.lib.grid.node.NodeConnector;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by robert on 11/5/2014.
 */
public class BranchedGrid<N extends NodeBranchPeace> extends GridTicking<N>
{
    private boolean markForMapping = true;
    private Set<Part> parts = new HashSet<Part>();

    public BranchedGrid()
    {
        this(NodeBranchPeace.class);
    }

    public BranchedGrid(Class cla)
    {
        super(cla);
        UpdateTicker.addUpdater(this);
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
        if(markForMapping)
        {
            GridPathfinder pathfinder = new GridPathfinder(this);
            parts = pathfinder.generateParts();
            markForMapping = false;
        }
    }
}
