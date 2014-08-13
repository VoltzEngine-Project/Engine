package universalelectricity.core.grid;

import scala.reflect.ClassTag;
import universalelectricity.api.core.grid.IUpdate;

/**
 * Created by robert on 8/11/2014.
 */
public class TickingGrid<N extends Node> extends NodeGrid<N> implements IUpdate {

    public TickingGrid(Class node)
    {
        super(node);
        UpdateTicker.addUpdater(this);
    }

    @Override
    public void update(double deltaTime)
    {
        for(N node : this.getNodes())
        {
            node.update(deltaTime);
        }
    }

    @Override
    public boolean canUpdate() {return getNodes().size() > 0; }

    @Override
    public boolean continueUpdate() { return canUpdate();}
}
