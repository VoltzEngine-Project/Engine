package resonant.lib.grid;

import resonant.api.grid.IUpdate;
import resonant.lib.grid.node.Node;

public class GridTicking<N extends Node> extends GridNode<N> implements IUpdate {

    public GridTicking(Class node)
    {
        super(node);
        UpdateTicker.addUpdater(this);
    }

    @Override
    public void update(double deltaTime)
    {
        for(N node : this.getNodes())
        {
            if(node instanceof IUpdate && ((IUpdate) node).canUpdate())
                ((IUpdate)node).update(deltaTime);
        }
    }

    @Override
    public boolean canUpdate() {return getNodes().size() > 0; }

    @Override
    public boolean continueUpdate() { return canUpdate();}
}
