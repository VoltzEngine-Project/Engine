package resonant.lib.grid.branch;

import resonant.lib.grid.GridTicking;
import resonant.lib.grid.UpdateTicker;
import resonant.lib.grid.branch.part.Part;

import java.util.HashSet;
import java.util.Set;

/** Version of the grid that uses objects to contain several nodes
 * in order to create a macro level view of the grid. It also allows
 * much better control over the nodes as each branch handles the
 * micro actions of the nodes.
 *
 * Created by robert on 11/5/2014.
 */
public class BranchedGrid extends GridTicking<NodeBranchPart>
{
    private boolean markForMapping = true;
    private Set<Part> parts = new HashSet<Part>();

    public BranchedGrid()
    {
        this(NodeBranchPart.class);
    }

    public BranchedGrid(Class cla)
    {
        super(cla);
        UpdateTicker.threaded().addUpdater(this);
    }

    public Set<Part> getParts()
    {
        return parts;
    }

    @Override
    public void add(NodeBranchPart node)
    {
        super.add(node);
        markForMapping = true;
    }

    @Override
    public void remove(NodeBranchPart node)
    {
        super.remove(node);
        markForMapping = true;
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

    @Override
    public boolean isValidNode(Object node)
    {
        return node instanceof NodeBranchPart;
    }
}
