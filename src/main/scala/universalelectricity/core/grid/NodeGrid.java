package universalelectricity.core.grid;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.Map;

/**
 * Created by robert on 8/13/2014.
 */
public class NodeGrid<N extends Node> extends Grid<N>
{
    public NodeGrid(Class node)
    {
        super(node);
    }

    @Override
    protected void reconstructNode(N node)
    {
        node.recache();
        node.setGrid(this);

        Map<N, ForgeDirection> connections = node.connections();

        for(Map.Entry<N, ForgeDirection> entry : connections.entrySet())
        {
            if (isValidNode(entry.getKey()))
            {
                N con = (N)entry.getKey();

                if (con.getGrid() != this)
                {
                    //TODO redo merge grid logic
                    con.getGrid().getNodes().clear();
                    add(con);
                    reconstructNode(con);
                }
            }
        }
    }

    @Override
    public void deconstruct()
    {
        for(N node : getNodes())
        {
            node.setGrid(null);
            node.reconstruct();
        }
        super.deconstruct();
    }
}
