package com.builtbroken.mc.lib.grid;

import com.builtbroken.mc.api.IUpdate;
import com.builtbroken.mc.lib.node.AbstractNode;

public class GridTicking<N extends AbstractNode> extends Grid<N> implements IUpdate {

    public GridTicking(Class node)
    {
        super(node);
    }

    @Override
    public void add(N node)
    {
        super.add(node);
        if(getNodes().size() > 0)
        {
            // TODO UpdateTicker.threaded().addUpdater(this);
        }
    }

    @Override
    public boolean update()
    {
        for(N node : this.getNodes())
        {
            if(node instanceof IUpdate)
            {
                ((IUpdate) node).update();
            }
        }
        return getNodes().size() > 0;
    }
}
